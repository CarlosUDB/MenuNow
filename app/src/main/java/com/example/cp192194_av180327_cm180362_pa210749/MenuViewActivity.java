package com.example.cp192194_av180327_cm180362_pa210749;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuViewActivity extends AppCompatActivity {
    ImageButton btnNewDish;
    ImageButton btnReturn;
    private RecyclerView recyclerView;
    private PlatilloAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);

        btnReturn = findViewById(R.id.btnReturn);
        btnNewDish = findViewById(R.id.btnNewDish);

        recyclerView = findViewById(R.id.recyclerViewPlatillos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Platillo> platillos = new ArrayList<>();
        adapter = new PlatilloAdapter(this, platillos);
        recyclerView.setAdapter(adapter);

        String category = getIntent().getStringExtra("category");
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("platillos").child(category);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                platillos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Platillo platillo = snapshot.getValue(Platillo.class);
                    platillos.add(platillo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MenuViewActivity.this, "Failed to load data.", Toast.LENGTH_LONG).show();
            }

        });

        // Verificar el estado de autenticación para mostrar/ocultar botones
        //DESCOMENTAR SI SE NECESITA OCULTAR EL BOTON DE  AGFREGAR PLATILLO

        /* if(FirebaseAuth.getInstance().getCurrentUser() != null){
            btnNewDish.setVisibility(View.VISIBLE);
        }else{
            btnNewDish.setVisibility(View.GONE);
        } */

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