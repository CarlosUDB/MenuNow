package com.example.cp192194_av180327_cm180362_pa210749;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ImageButton btnAccount, btnNewDish;
    Button btnChangePassword, btnLogout, btnBreakfasts,btnLunches,btnDinners;

    Dialog accountDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewDish = findViewById(R.id.btnNewDish);
        btnAccount = findViewById(R.id.btnAccount);
        Button btnBreakfasts = findViewById(R.id.btnBreakfasts);
        Button btnLunches = findViewById(R.id.btnLunches);
        Button btnDinners = findViewById(R.id.btnDinners);


        //hiding stuff if no one is logged
        //DESCOMENTAR SI SE NECESITA OCULTAR EL BOTON DE  AGFREGAR PLATILLO

        /* if(FirebaseAuth.getInstance().getCurrentUser() != null){
            btnNewDish.setVisibility(View.VISIBLE);
        }else{
            btnNewDish.setVisibility(View.GONE);
        } */

        //dialog settings and funtionality
        accountDialog = new Dialog(MainActivity.this);
        accountDialog.setContentView(R.layout.dialog_account);
        accountDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        accountDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg_dialog_account));
        accountDialog.setCancelable(true);
        //dialog's buttons
        btnChangePassword = accountDialog.findViewById(R.id.btnChangePassword);
        btnLogout = accountDialog.findViewById(R.id.btnLogout);

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_LONG).show();
            accountDialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        //changing functionality if there is a logged user
        btnAccount.setOnClickListener(v -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                accountDialog.show();
            }else{
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        });

        btnBreakfasts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuViewActivity.class);
            intent.putExtra("category", "Desayuno");
            startActivity(intent);
        });
        btnLunches.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuViewActivity.class);
            intent.putExtra("category", "Almuerzo");
            startActivity(intent);
        });
        btnDinners.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuViewActivity.class);
            intent.putExtra("category", "Cena");
            startActivity(intent);
        });

        btnNewDish.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FoodFormActivity.class);
            startActivity(intent);
        });

    }
}