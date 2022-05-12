package com.example.magicnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity {


    //Khai báo biến
    private TextView banner;
    private Button registerUser;
    private EditText etFullname,etAge,etEmail,etPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    DBHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        init();
        mAuth= FirebaseAuth.getInstance();
        DB = new DBHelper(this);


        //Quay ve lai trang login
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUser.this,MainActivity.class));
            }
        });
        //Dang ky tai khoan
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    public void init(){
        banner=findViewById(R.id.banner);
        registerUser=findViewById(R.id.btnRegister);
        etFullname=findViewById(R.id.RFullname);
        etAge=findViewById(R.id.RAge);
        etPassword=findViewById(R.id.RPassword);
        etEmail=findViewById(R.id.Remail);
        progressBar = findViewById(R.id.progressBar);
    }

    public void registerUser(){
        String email = etEmail.getText().toString().trim();
        String age=etAge.getText().toString().trim();
        String fullname=etFullname.getText().toString().trim();
        String password=etPassword.getText().toString().trim();

        //Kiem tra cac edittext khong duoc bo trong
        if(fullname.isEmpty()){
            etFullname.setError("Fullname is required");
            etFullname.requestFocus();
            return;
        }
        if(age.isEmpty()){
            etAge.setError("Age is required");
            etAge.requestFocus();
            return;
        }
        if(email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        //Kiem tra dinh dang email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide a valid email");
            etEmail.requestFocus();
            return;
        }
        //Kiem tra do dai cua mat khau
        if (password.length()<8){
            etPassword.setError("Min password length must be 8 characters");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            DB.insertData(email,password,fullname,age);
                            User user=new User(fullname,age,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                        user.sendEmailVerification();
                                        Toast.makeText(RegisterUser.this, "Please Verified your email", Toast.LENGTH_LONG).show();

                                        //Chuyen ve trang dang nhap
                                        startActivity(new Intent(RegisterUser.this,MainActivity.class));

                                    }
                                    else{
                                        Toast.makeText(RegisterUser.this, "Failed to reister", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(RegisterUser.this, "Failed to reister", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}