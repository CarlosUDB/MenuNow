package com.example.cp192194_av180327_cm180362_pa210749;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class MenuViewActivity extends AppCompatActivity {

    ImageButton btnNewDish;
    ImageButton btnReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);

        btnReturn = findViewById(R.id.btnReturn);
        btnNewDish = findViewById(R.id.btnNewDish);


        //hiding stuff if no one is logged
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