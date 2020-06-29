package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button registerbutton;
    private EditText usercemail,usercpassword;
    private TextView alreadyhavenewaccount;

    private FirebaseAuth mAuth;


    private ProgressDialog loadingbar;
    String mmm;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();


        initialise();

        alreadyhavenewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sendusertologin();
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createaccount();
            }
        });

    }

    private void createaccount() {
        String email=usercemail.getText().toString();
        String password=usercpassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingbar.setTitle("Creating Account...");
            loadingbar.setMessage("...WAIT...,Creating Account");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String currentuserid=mAuth.getCurrentUser().getUid();

//                                FirebaseDatabase.getInstance().getReference().child("Users").push()
//                                        .setValue(currentuserid)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                Log.i("lknskdf","on complete");
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.i("lknskdf","message:-- "+e.toString());
//                                    }
//                                });


                                Toast.makeText(RegisterActivity.this, "Account Created Sucessfully", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, "Login Usign Your Credential", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                            else{
                                String message= task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                    });

        }
    }

    private void initialise() {
        registerbutton=(Button)findViewById(R.id.register_button);
        usercemail=(EditText)findViewById(R.id.register_email);
        usercpassword=(EditText)findViewById(R.id.register_password);
        alreadyhavenewaccount=(TextView)findViewById(R.id.already_have_an_account_link);
        loadingbar=new ProgressDialog(this);
    }

    private void sendusertologin() {
        Intent registerintent=new Intent(RegisterActivity.this,loginActivity.class);
        startActivity(registerintent);
    }

}
