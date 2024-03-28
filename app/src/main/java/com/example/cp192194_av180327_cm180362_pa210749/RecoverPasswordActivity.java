package com.example.cp192194_av180327_cm180362_pa210749;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    ImageButton btnReturn;
    EditText edtEmail;
    Button btnRequestRecoverPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        auth = FirebaseAuth.getInstance();
        btnReturn = findViewById(R.id.btnReturn);
        edtEmail = findViewById(R.id.edtEmail);
        btnRequestRecoverPassword = findViewById(R.id.btnRequestRecoverPassword);

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        btnRequestRecoverPassword.setOnClickListener(v -> {
            recoverPassword(edtEmail.getText().toString());
        });
    }

    private void recoverPassword(String email){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(RecoverPasswordActivity.this, "Rellenar campo correctamente", Toast.LENGTH_LONG).show();
        }else{
            auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(RecoverPasswordActivity.this, "URL para recuperar contraseña enviado al correo indicado", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RecoverPasswordActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}