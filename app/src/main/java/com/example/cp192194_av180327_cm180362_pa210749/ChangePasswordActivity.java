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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText edtNewPassword, edtOldPassword;
    Button btnChangePassword;
    ImageButton btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth = FirebaseAuth.getInstance();
        btnReturn = findViewById(R.id.btnReturn);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);

        btnReturn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            changePassword(edtOldPassword.getText().toString(), edtNewPassword.getText().toString());
        });
    }

    private void changePassword(String oldPassword, String newPassword){

        if(TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword)){//checking passwords
            Toast.makeText(ChangePasswordActivity.this, "Rellenar campos correctamente", Toast.LENGTH_LONG).show();
        }else{
            if(newPassword.length() < 6){
                Toast.makeText(ChangePasswordActivity.this, "La nueva contraseña debe contener al menos 6 caracteres", Toast.LENGTH_LONG).show();
            }else{

                FirebaseUser user = auth.getCurrentUser();
                AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
                user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {//reauthenticating user with old password to check if it is correct
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {//update to new password
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ChangePasswordActivity.this, "Contraseña cambiada", Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChangePasswordActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(ChangePasswordActivity.this, "Contraseña vieja incorrecta", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ChangePasswordActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }
    }
}