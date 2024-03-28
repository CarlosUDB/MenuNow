package com.example.cp192194_av180327_cm180362_pa210749;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    ImageButton btnReturn;
    Button btnLogin;
    EditText edtEmail, edtPassword;
    TextView txtForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        btnReturn = findViewById(R.id.btnReturn);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            login(edtEmail.getText().toString(), edtPassword.getText().toString());
        });

        txtForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RecoverPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void login(String email, String password){

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Rellenar campos correctamente", Toast.LENGTH_LONG).show();
        }else{
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "Sesión iniciada", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}