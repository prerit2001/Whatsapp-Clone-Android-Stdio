package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {


    FirebaseAuth mAuth;

    private ProgressDialog pd;

    private Button loginbutton,phoneloginbutton;
    private EditText useremail,userpassword;
    private TextView neednewaccount,forgetpasswordlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();




        pd=new ProgressDialog(this);
        initialize();
        neednewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendusertoregisterctivity();
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowusertologin();
            }
        });

        phoneloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneactivity=new Intent(loginActivity.this,phoneLoginActivity.class);
                startActivity(phoneactivity);
            }
        });

    }

    private void allowusertologin() {
        String email=useremail.getText().toString();
        String password=userpassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else {
            pd.setTitle("Loading...");
            pd.setMessage("...WAIT...,..");
            pd.setCanceledOnTouchOutside(true);
            pd.show();
           mAuth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()){

                              Toast.makeText(loginActivity.this, "Logged In Sucessful", Toast.LENGTH_SHORT).show();
                              sendusertoamainctivity();
                          }
                          else{
                              String message= task.getException().toString();
                              Toast.makeText(loginActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                              pd.dismiss();
                          }
                       }
                   });
        }
    }

    private void initialize() {
        loginbutton=(Button)findViewById(R.id.login_button);
        phoneloginbutton=(Button)findViewById(R.id.phone_login_button);
        useremail=(EditText)findViewById(R.id.login_email);
        userpassword=(EditText)findViewById(R.id.login_password);
        neednewaccount=(TextView)findViewById(R.id.dont_have_an_account_link);
        forgetpasswordlink=(TextView)findViewById(R.id.forgot_password_link);
    }


    private void sendusertoamainctivity() {
        Intent loginIntent=new Intent(loginActivity.this,MainActivity.class);
        startActivity(loginIntent);
    }

    private void sendusertoregisterctivity() {
        Intent loginIntent=new Intent(loginActivity.this,RegisterActivity.class);
        startActivity(loginIntent);
    }


}
