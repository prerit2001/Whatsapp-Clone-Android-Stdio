package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdaptor myTabsAccessorAdaptor;

    FirebaseUser currentuser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();


        mToolbar=(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("WhatsAppClone");

        myViewPager=(ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdaptor=new TabsAccessorAdaptor(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdaptor);

        myTabLayout=(TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentuser==null){
            sendusertologinscreen();
        }
        else{
            verifyuserexistence();
        }
    }

    private void verifyuserexistence() {
        String currentUserId=mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists())){
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendusertosettingscreen();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendusertologinscreen() {
        Intent loginIntent=new Intent(MainActivity.this,loginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.main_logout_option){
            mAuth.signOut();
            sendusertologinscreen();
         }
        if(item.getItemId()==R.id.main_setting_option){
            sendusertosettingscreen();
        }
        if(item.getItemId()==R.id.main_find_friend_option){
            sendusertofindfriendscreen();
        }
        if(item.getItemId()==R.id.main_create_group_option){
             requestnewgroup();
        }
        return true;
    }

    private void sendusertofindfriendscreen() {
        Intent findintent=new Intent(MainActivity.this,findFriendActivity.class);
        startActivity(findintent);
    }

    private void requestnewgroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group name");
        final EditText groupnamefeild=new EditText(MainActivity.this);
        groupnamefeild.setHint("e.g Friend Zone ");
        builder.setView(groupnamefeild);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String groupname=groupnamefeild.getText().toString();
               if(TextUtils.isEmpty(groupname)){
                   Toast.makeText(MainActivity.this, "Please write group name", Toast.LENGTH_SHORT).show();
               }
               else{
                   createnewgroup(groupname);
               }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void createnewgroup(String groupname) {
        FirebaseDatabase.getInstance().getReference().child("Groups").child(groupname).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Sucessfully created group", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendusertosettingscreen() {
        Intent setting=new Intent(MainActivity.this,settingActivity.class);
        startActivity(setting);
    }
}
