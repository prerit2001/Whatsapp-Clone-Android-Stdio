package com.example.whatsappclone;
import de.hdodenhof.circleimageview.CircleImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class findFriendActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView findfriendrecyclerlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        findfriendrecyclerlist=(RecyclerView) findViewById(R.id.find_friend_recyler_list);
        findfriendrecyclerlist.setLayoutManager(new LinearLayoutManager(this));

        mToolbar=(Toolbar)findViewById(R.id.find_friend_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");




    }

    @Override
    protected void onStart() {

        FirebaseRecyclerOptions<contacts> options =
                new FirebaseRecyclerOptions.Builder<contacts>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"),contacts.class)
                .build();

        super.onStart();
        FirebaseRecyclerAdapter<contacts,Findfriendviewholder> adapter=
                new FirebaseRecyclerAdapter<contacts, Findfriendviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Findfriendviewholder holder, final int position, @NonNull contacts model) {
                           holder.username.setText(model.getName());

                           holder.itemView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   String visit_userid=getRef(position).getKey();
                                   Intent profileintent=new Intent(findFriendActivity.this,ProfileActivity.class);
                                   profileintent.putExtra("visit_userid",visit_userid);
                                   startActivity(profileintent);
                               }
                           });
                    }

                    @NonNull
                    @Override
                    public Findfriendviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                        Findfriendviewholder viewholder=new Findfriendviewholder(view);
                        return viewholder;
                    }
                };
        findfriendrecyclerlist.setAdapter(adapter);
        adapter.startListening();
    }
    class Findfriendviewholder extends RecyclerView.ViewHolder{

        TextView username,ustatus;

        public Findfriendviewholder(@NonNull View itemView) {



            super(itemView);
            username=itemView.findViewById(R.id.users_profile_name);
         //   ustatus=itemView.findViewById(R.id.user_online_status);
        }
    }



}
