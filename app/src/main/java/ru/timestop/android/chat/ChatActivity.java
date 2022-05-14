package ru.timestop.android.chat;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ru.timestop.android.myapplication.R;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, ChildEventListener {
    private Button bttnSend;
    private TextView txtMessage;
    private ListView listMessages;
    private MessageAdaptors messageAdaptors;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database = FirebaseDatabase.getInstance();

        txtMessage = findViewById(R.id.editMessage);
        txtMessage.addTextChangedListener(this);
        txtMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

        bttnSend = findViewById(R.id.bttnSend);
        bttnSend.setOnClickListener(this);

        messageAdaptors = new MessageAdaptors(this, R.layout.message);

        listMessages = findViewById(R.id.listMessages);
        listMessages.setAdapter(messageAdaptors);


        myRef = database.getReference().child("message");
        myRef.addChildEventListener(this);
    }

    @Override
    public void onClick(View v) {
        //Log.d("chat", "button send clicked");
        String message = txtMessage.getText().toString();
        //Log.d("chat", "message is:" + message);
        txtMessage.setText("");
        myRef = database.getReference().child("message");
        myRef.push().setValue(new ChatMessage().setMessage(message).setUser("User").setPhotoUrl("-"));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //SKIP
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        bttnSend.setEnabled(s.toString().trim().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
        //SKIP
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        messageAdaptors.add(snapshot.getValue(ChatMessage.class));
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
