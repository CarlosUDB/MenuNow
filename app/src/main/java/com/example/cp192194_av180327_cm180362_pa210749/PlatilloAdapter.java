package com.example.cp192194_av180327_cm180362_pa210749;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlatilloAdapter extends RecyclerView.Adapter<PlatilloAdapter.PlatilloViewHolder> {
    private List<Platillo> platillos;
    private LayoutInflater inflater;
    private Context context;
    private String category;
    private String Accion;
    private DatabaseReference databaseReference;
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public PlatilloAdapter(Context context, List<Platillo> platillos, String category) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.platillos = platillos;
        this.category = category;

    }

    @Override
    public PlatilloViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_platillo, parent, false);
        return new PlatilloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlatilloViewHolder holder, int position) {
        Platillo platillo = platillos.get(position);
        holder.bind(platillo, category);
    }

    @Override
    public int getItemCount() {
        return platillos.size();
    }

    class PlatilloViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView, descriptionView, priceView;
        private ImageView imageView;
        private Button btnEditar, btnEliminar;

        PlatilloViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textViewNombre);
            descriptionView = itemView.findViewById(R.id.textViewDescripcion);
            priceView = itemView.findViewById(R.id.textViewPrecio);
            imageView = itemView.findViewById(R.id.imageViewPlatillo);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        void bind(Platillo platillo, String category) {
            nameView.setText(platillo.getName());
            descriptionView.setText(platillo.getDescription());
            priceView.setText("$" + platillo.getPrice());
            Picasso.get().load(platillo.getImageUrl()).into(imageView);

            if(FirebaseAuth.getInstance().getCurrentUser() != null){
            btnEditar.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
        }else{
                btnEditar.setVisibility(View.GONE);
                btnEliminar.setVisibility(View.GONE);
        }

            btnEditar.setOnClickListener(v -> editarPlatillo(platillo, category));
            btnEliminar.setOnClickListener(v -> eliminarPlatillo(platillo, category));
        }

        private void editarPlatillo(Platillo platillo, String category) {
            Intent intent = new Intent(context, FoodFormActivity.class);

            // Lógica para editar el platillo
            // donde puedes pasar el ID del platillo o el objeto Platillo completo para editarlo.
            String IdEdit = platillo.getId();
            String NameEdit = platillo.getName();
            String DescriptionEdit = platillo.getDescription();
            String PriceEdit = platillo.getPrice();
            String ImageEdit = platillo.getImageUrl();
            String CategoryEdit = category;
            String DateEdit = platillo.getDate();
            Accion = "Editar";

            eliminarPlatillo(platillo, category);

            intent.putExtra("IdEdit", IdEdit);
            intent.putExtra("NameEdit", NameEdit);
            intent.putExtra("DescriptionEdit", DescriptionEdit);
            intent.putExtra("PriceEdit", PriceEdit);
            intent.putExtra("ImageEdit", ImageEdit);
            intent.putExtra("CategoryEdit", CategoryEdit);
            intent.putExtra("DateEdit",DateEdit);
            intent.putExtra("Accion", Accion);
            context.startActivity(intent); // Utiliza el contexto para iniciar la actividad

        }

        private void eliminarPlatillo(Platillo platillo, String category) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference dbRef = mDatabase.child("platillos").child(category);
            String today = getCurrentDate();
            dbRef.orderByChild("date").equalTo(today).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterar sobre los hijos de "Cena" para obtener solo las claves y nombres de los platillos
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        // Obtener el nombre del platillo
                        String nombrePlatillo = snapshot.child("name").getValue(String.class);

                        if (platillo.getName() == nombrePlatillo) {
                            dbRef.child(key).removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        if (Accion != "Editar") {
                                            Toast.makeText(context, "Platillo eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                        }
                                        })
                                    .addOnFailureListener(e -> {
                                        // Ocurrió un error al intentar eliminar el platillo
                                        // Muestra un mensaje de error
                                        if (Accion != "Editar") {
                                            Toast.makeText(context, "Error al eliminar el platillo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores de lectura de la base de datos
                    Log.e("Firebase", "Error al leer la base de datos", databaseError.toException());
                    Toast.makeText(context, "Error al leer la base de datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}