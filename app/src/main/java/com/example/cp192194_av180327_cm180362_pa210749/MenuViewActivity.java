package com.example.cp192194_av180327_cm180362_pa210749;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuViewActivity extends AppCompatActivity {
    ImageButton btnNewDish;
    ImageButton btnReturn;
    private RecyclerView recyclerView;
    private PlatilloAdapter adapter;
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);

        btnReturn = findViewById(R.id.btnReturn);
        btnNewDish = findViewById(R.id.btnNewDish);
        recyclerView = findViewById(R.id.recyclerViewPlatillos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Platillo> platillos = new ArrayList<>();
        String category = getIntent().getStringExtra("category");
        adapter = new PlatilloAdapter(this, platillos, category);
        recyclerView.setAdapter(adapter);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("platillos").child(category);
        String today = getCurrentDate();
        boolean userLoggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;

        btnNewDish.setVisibility(userLoggedIn ? View.VISIBLE : View.INVISIBLE);

        if (userLoggedIn) {
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    platillos.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Platillo platillo = snapshot.getValue(Platillo.class);
                            platillos.add(platillo);
                        }
                    } else {
                        // No se encontraron platillos
                        Toast.makeText(MenuViewActivity.this, "No hay platillos disponibles.", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MenuViewActivity.this, "Failed to load data.", Toast.LENGTH_LONG).show();
                }
            });
        } else {

            databaseRef.orderByChild("date").equalTo(today).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    platillos.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Platillo platillo = snapshot.getValue(Platillo.class);
                            platillos.add(platillo);
                        }
                    } else {
                        // No se encontraron platillos para la fecha actual
                        Toast.makeText(MenuViewActivity.this, "No hay platillos para el día de hoy.", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MenuViewActivity.this, "Failed to load data.", Toast.LENGTH_LONG).show();
                }
            });

        }

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        btnNewDish.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FoodFormActivity.class);
            startActivity(intent);
        });
    }
}