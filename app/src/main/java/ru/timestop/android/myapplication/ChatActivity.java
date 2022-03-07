package ru.timestop.android.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.timestop.android.adaptors.MessageAdaptors;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bttnSend;
    private TextView txtMessage;
    private ListView listMessages;
    private MessageAdaptors messageAdaptors;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        txtMessage = findViewById(R.id.editMessage);

        bttnSend = findViewById(R.id.bttnSend);
        bttnSend.setOnClickListener(this);

        listMessages = findViewById(R.id.listMessages);

    }

    @Override
    public void onClick(View v) {
        Log.d("chat", "button send clicked");
        String message = txtMessage.getText().toString();
        Log.d("chat", "message is:" + message);
        txtMessage.setText("");
        //listMessages.setAdapter();
    }
}
