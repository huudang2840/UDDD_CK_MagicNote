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

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    //Khai bao bien
    private TextView register;
    private EditText etEmail,etPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        mAuth=FirebaseAuth.getInstance();
        DB = new DBHelper(this);
        //Chuyen qua trang dang ky
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterUser.class));

            }
        });
        //login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLoign();
            }
        });

    }
    //Khoi tao cac bien theo id da dat
    public void init(){
        register=findViewById(R.id.Register);
        btnLogin=findViewById(R.id.btnLogin);
        etEmail=findViewById(R.id.email);
        etPassword=findViewById(R.id.Password);
        progressBar=findViewById(R.id.progressBar);
    }
    //Ham dang nhap
    public void userLoign(){
        String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString().trim();

        //Kiem tra cac edit text co rong hay khong
        if(email.isEmpty())
        {
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }

        //Kiem tra dinh dang email
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //Ket noi vao database va dang nhap
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this,TrangChu.class));
                        progressBar.setVisibility(View.GONE);

                        //Set lai cac edit text thành rỗng
                        etEmail.setText("");
                        etPassword.setText("");
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Please verified your email first !!!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}