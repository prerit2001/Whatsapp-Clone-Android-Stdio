package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class groupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton SendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;
    private String CurrentGroupName,CurrentUserID,CurrentuserName;
    private FirebaseAuth mAuth;

    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);




        currentGroupName=getIntent().getExtras().get("groupName").toString();


        Toast.makeText(groupChatActivity.this, CurrentGroupName, Toast.LENGTH_SHORT).show();
        mAuth=  FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        InitializeFields();
        getuserinfo();

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Savemessageinfotodatabase();
                userMessageInput.setText("");
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                      if(dataSnapshot.exists()){
                          displaymessages(dataSnapshot);
                      }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.exists()){
                            displaymessages(dataSnapshot);
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void displaymessages(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            String chatmessage=(String)((DataSnapshot)iterator.next()).getValue();
            //String chatdate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatname=(String)((DataSnapshot)iterator.next()).getValue();
            String chattime=(String)((DataSnapshot)iterator.next()).getValue();
            displayTextMessages.append(chatname+" : \n"+chatmessage + " \n" +chattime+"\n\n\n\n");
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }

    }

    private void Savemessageinfotodatabase() {
    String message=userMessageInput.getText().toString();
    String messagekey=FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).push().getKey();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "please write message", Toast.LENGTH_SHORT).show();
        }
        else{
//            Calendar callfordate=Calendar.getInstance();
//            SimpleDateFormat curedataformat=new SimpleDateFormat("yyyyy-MM-dd");
//            currentDate=curedataformat.format(callfordate);

            Calendar callfortime=Calendar.getInstance();
            SimpleDateFormat currenttimeformat=new SimpleDateFormat("hh:mm a");
            currentTime=currenttimeformat.format(callfortime.getTime());

            HashMap<String,Object> groupmessagekey=new HashMap<>();
            FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).updateChildren(groupmessagekey);

            HashMap<String,Object> messageInfomap =new HashMap<>();
            messageInfomap.put("name",currentUserName);
            messageInfomap.put("message",message);
           // messageInfomap.put("date",currentDate);
            messageInfomap.put("time",currentTime);

            FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child(messagekey).updateChildren(messageInfomap);

        }
    }

    private void getuserinfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                           currentUserName= dataSnapshot.child("name").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void InitializeFields() {
        mToolbar = (Toolbar) findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);
        displayTextMessages = (TextView) findViewById(R.id.group_chat_text_display);
        mScrollView = (ScrollView) findViewById(R.id.my_scroll_view);
    }
}
