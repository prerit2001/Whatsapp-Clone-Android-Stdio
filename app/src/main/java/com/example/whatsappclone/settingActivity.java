package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class settingActivity extends AppCompatActivity {

    private Button UpdateAccountSettings;
    private EditText userName, userStatus;
    private CircleImageView userProfileImage;
    private String currentuserid;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        InitializeFields();
        mAuth=FirebaseAuth.getInstance();
        currentuserid=mAuth.getCurrentUser().getUid();
        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatesetting();
            }
        });
        retrieveuserInfo();
    }

    private void retrieveuserInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists())&&(dataSnapshot.hasChild("name")&&dataSnapshot.hasChild("image"))){
                          String retriveusername=dataSnapshot.child("name").getValue().toString();
                            String retriveuserstatus=dataSnapshot.child("status").getValue().toString();
                            String retriveuserimage=dataSnapshot.child("image").getValue().toString();

                            userName.setText(retriveusername);
                            userStatus.setText(retriveuserstatus);
                        }
                        else if((dataSnapshot.exists())&&(dataSnapshot.hasChild("name"))){
                            String retriveusername=dataSnapshot.child("name").getValue().toString();
                            String retriveuserstatus=dataSnapshot.child("status").getValue().toString();

                            userName.setText(retriveusername);
                            userStatus.setText(retriveuserstatus);
                        }
                        else{
                            Toast.makeText(settingActivity.this, "Please update profile", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updatesetting() {
        String setusername=userName.getText().toString();
        String setusersstatus=userStatus.getText().toString();
        if(TextUtils.isEmpty(setusername)||TextUtils.isEmpty(setusersstatus)){
            Toast.makeText(this, "Please fill the blanks", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String,String> profilemap=new HashMap<>();
            profilemap.put("uid",currentuserid);
            profilemap.put("name",setusername);
            profilemap.put("status",setusersstatus);
            FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserid).setValue(profilemap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 sendusertomain();
                                 Toast.makeText(settingActivity.this, "profile uated sucessful", Toast.LENGTH_SHORT).show();
                             }
                             else{
                                 String mess=task.getException().toString();
                                 Toast.makeText(settingActivity.this, "Error: "+mess, Toast.LENGTH_SHORT).show();
                             }
                        }
                    });
        }

    }

    private void InitializeFields() {

        UpdateAccountSettings = (Button) findViewById(R.id.update);
        userName = (EditText) findViewById(R.id.set_user_name);
        userStatus = (EditText) findViewById(R.id.set_profile_status);
        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);
    }
    private void sendusertomain(){
        Intent mainIntent=new Intent(settingActivity.this,MainActivity.class);
        startActivity(mainIntent);
    }
}
