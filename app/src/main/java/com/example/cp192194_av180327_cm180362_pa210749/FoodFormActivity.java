package com.example.cp192194_av180327_cm180362_pa210749;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class FoodFormActivity extends AppCompatActivity {

    // Constantes y variables para manejar la selección de imagen y la interfaz de usuario.
    private static final int PICK_IMAGE_REQUEST = 22;
    private ImageView imageViewPlatillo;
    private Uri imageUri;
    private EditText editTextNombre, editTextPrecio, editTextDescripcion;
    private RadioGroup radioGroupTipo;

    // Variables para Firebase, permitiendo el acceso a la base de datos y al almacenamiento.
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_form);

        // Inicialización de los componentes de la interfaz de usuario
        imageViewPlatillo = findViewById(R.id.imageView_platillo);
        Button btnAddImage = findViewById(R.id.btnAddImage);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        ImageButton btnReturn = findViewById(R.id.btnReturn);
        editTextNombre = findViewById(R.id.nombre);
        editTextPrecio = findViewById(R.id.precio);
        editTextDescripcion = findViewById(R.id.comentario);
        radioGroupTipo = findViewById(R.id.radioGroup);


        // Configuración de Firebase.
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("platillos");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Establecimiento de los listeners para los botones.
        btnAddImage.setOnClickListener(v -> openGallery());
        btnSave.setOnClickListener(v -> uploadImage());
        btnCancel.setOnClickListener(v -> finish());

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

    }

    // Método para abrir la galería y seleccionar una imagen.
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Método que se llama después de que el usuario selecciona una imagen, para manejar el resultado.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewPlatillo.setImageURI(imageUri);
        }
    }

    // Método para subir la imagen a Firebase Storage y validar los inputs antes de guardar.
    private void uploadImage() {
        if (imageUri != null && validateInputs()) {
            StorageReference fileRef = storageRef.child("FoodImages/" + UUID.randomUUID().toString());
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        savePlatillo(imageUrl);
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(FoodFormActivity.this, "Fallo al cargar la imagen", Toast.LENGTH_SHORT).show();
                    });
        } else {
            if (imageUri == null) {
                Toast.makeText(this, "Por favor, seleccione una imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para validar que los campos de texto y la selección de categoría no están vacíos.
    private boolean validateInputs() {
        String name = editTextNombre.getText().toString().trim();
        String price = editTextPrecio.getText().toString().trim();
        String description = editTextDescripcion.getText().toString().trim();
        int selectedId = radioGroupTipo.getCheckedRadioButtonId();

        if (name.isEmpty()) {
            editTextNombre.setError("El nombre no puede estar vacío");
            return false;
        }
        if (price.isEmpty()) {
            editTextPrecio.setError("El precio no puede estar vacío");
            return false;
        }
        if (description.isEmpty()) {
            editTextDescripcion.setError("La descripción no puede estar vacía");
            return false;
        }
        if (selectedId == -1) {
            Toast.makeText(this, "Por favor, seleccione una categoría", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true; // Todos los campos están correctamente llenados
    }

    // Método para guardar la información del platillo en Firebase Database bajo la categoría elegida.
    private void savePlatillo(String imageUrl) {
        String name = editTextNombre.getText().toString();
        String price = editTextPrecio.getText().toString();
        String description = editTextDescripcion.getText().toString();
        int selectedId = radioGroupTipo.getCheckedRadioButtonId();
        final String[] categoriaPath = new String[1]; // Usando un array de un elemento

        if (selectedId == R.id.radioButtonDesayuno) {
            categoriaPath[0] = "Desayuno";
        } else if (selectedId == R.id.radioButtonAlmuerzo) {
            categoriaPath[0] = "Almuerzo";
        } else if (selectedId == R.id.radioButtonCena) {
            categoriaPath[0] = "Cena";
        } else {
            Toast.makeText(this, "Seleccione una categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!categoriaPath[0].isEmpty()) {
            DatabaseReference categoryRef = dbRef.child(categoriaPath[0]);
            Platillo platillo = new Platillo(name, description, price, imageUrl);
            categoryRef.push().setValue(platillo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(FoodFormActivity.this, "Platillo guardado en " + categoriaPath[0], Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FoodFormActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
