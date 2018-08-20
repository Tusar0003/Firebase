package com.example.no0ne.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button saveButton;
    private Button showButton;
    private EditText nameEditText;
    private EditText emailEditText;
    private ListView userListView;

    private ArrayList<String> userArrayList = new ArrayList<>();
    private ArrayList<String> keysArrayList = new ArrayList<>();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = (Button) findViewById(R.id.button_save);
        showButton = (Button) findViewById(R.id.button_show);
        nameEditText = (EditText) findViewById(R.id.edit_text_name);
        emailEditText = (EditText) findViewById(R.id.edit_text_email);

        userListView = (ListView) findViewById(R.id.user_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userArrayList);
        userListView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mDatabase = FirebaseDatabase.getInstance().getReference().child("Name");

                // First, create a child under root object
                // Then, Assign some to that child object
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();

//                HashMap<String, String> dataMap = new HashMap<String, String>();
//                dataMap.put("Name", name);
//                dataMap.put("Email", email);

                mDatabase.push().setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Stored Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                mDatabase.push().setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Stored Successfully", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String value = dataSnapshot.getValue(String.class);
                        userArrayList.add(value);

                        String key = dataSnapshot.getKey();
                        keysArrayList.add(key);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        String value = dataSnapshot.getValue(String.class);
                        String key = dataSnapshot.getKey();

                        int index = keysArrayList.indexOf(key);

                        userArrayList.set(index, value);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
