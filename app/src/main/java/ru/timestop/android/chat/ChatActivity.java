package ru.timestop.android.chat;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Optional;

import ru.timestop.android.myapplication.R;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener
        , CompoundButton.OnCheckedChangeListener {
    private SwitchCompat switchMode;
    private Button bttnSend;
    private ImageButton bttnImg;
    private TextView txtMessage;
    private ListView listMessages;

    private TextView txtUser;
    private TextView txtPassword;
    private Button bttnSignInOut;

    private MessageAdaptors messageAdaptors;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;

    private ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                StorageReference riversRef = storage.getReference("default").child("images/" + uri.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(uri);
                uploadTask.addOnFailureListener(exception -> {
                    Toast.makeText(ChatActivity.this, "Fail",
                            Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(taskSnapshot -> Toast.makeText(ChatActivity.this, "Loaded",
                        Toast.LENGTH_SHORT).show());
            });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getSharedPreferences("SharedFile", MODE_PRIVATE).edit().putString("Shared", "SharedString").apply();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Optional.ofNullable(savedInstanceState)
                .ifPresent(bundle -> Log.d("TAG", bundle.getString("savedValue", "NoValueSaved")));

        String t = this.getSharedPreferences("SharedFile", MODE_PRIVATE).getString("Shared", "NoSharedValue");

        Log.d("TAG", t);
        storage = FirebaseStorage.getInstance("gs://my-tutorial-68bff.appspot.com");
        setContentView(R.layout.activity_chat);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        switchMode = findViewById(R.id.switchMode);
        switchMode.setOnCheckedChangeListener(this);
        switchMode.setEnabled(currentUser == null);

        bttnSignInOut = findViewById(R.id.bttnSignInOut);
        bttnSignInOut.setOnClickListener(this);
        bttnSignInOut.setText((currentUser == null ? "SignIn" : "SignOut"));

        TextWatcher textWatcher = new DefaultTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bttnSignInOut.setEnabled(txtUser.getText().toString().trim().length() > 0
                        && txtPassword.toString().trim().length() > 0);
            }
        };

        txtUser = findViewById(R.id.editUser);
        txtUser.setEnabled(currentUser == null);
        txtUser.addTextChangedListener(textWatcher);

        txtPassword = findViewById(R.id.editPassword);
        txtPassword.setEnabled(currentUser == null);
        txtPassword.addTextChangedListener(textWatcher);

        bttnSend = findViewById(R.id.bttnSend);
        bttnSend.setOnClickListener(this);

        bttnImg = findViewById(R.id.bttnImg);
        bttnImg.setOnClickListener(this);

        txtMessage = findViewById(R.id.editMessage);
        txtMessage.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bttnSend.setEnabled(s.toString().trim().length() > 0);
            }
        });
        txtMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});


        messageAdaptors = new MessageAdaptors(this, R.layout.message);

        listMessages = findViewById(R.id.listMessages);
        listMessages.setAdapter(messageAdaptors);


        myRef = database.getReference().child("message");
        myRef.addChildEventListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("savedValue", "SomeValue");
    }

    @Override
    public void onClick(View v) {
        if (bttnImg.getId() == v.getId()) {
            //Intent choosePhoto = new Intent(Intent.ACTION_GET_CONTENT);
            //choosePhoto.setType("image/jpeg");
            //choosePhoto.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            mGetContent.launch("image/*");
        } else if (bttnSend.getId() == v.getId()) {
            //Log.d("chat", "button send clicked");
            String message = txtMessage.getText().toString();
            //Log.d("chat", "message is:" + message);
            txtMessage.setText("");
            myRef.push().setValue(new ChatMessage().setMessage(message).setUser("User").setPhotoUrl("-"));
        } else if (bttnSignInOut.getId() == v.getId()) {
            if (currentUser == null) {
                bttnSignInOut.setEnabled(false);
                txtUser.setEnabled(false);
                txtPassword.setEnabled(false);
                switchMode.setEnabled(false);
                if (switchMode.isChecked()) {
                    mAuth.createUserWithEmailAndPassword(txtUser.getText().toString().trim(), txtPassword.getText().toString().trim())
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    currentUser = mAuth.getCurrentUser();
                                    bttnSignInOut.setEnabled(true);
                                    bttnSignInOut.setText("SignOut");
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(ChatActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    bttnSignInOut.setEnabled(true);
                                    txtUser.setEnabled(true);
                                    txtPassword.setEnabled(true);
                                    switchMode.setEnabled(true);
                                }
                            });
                } else {
                    mAuth.signInWithEmailAndPassword(txtUser.getText().toString().trim(), txtPassword.getText().toString().trim())
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    currentUser = mAuth.getCurrentUser();
                                    bttnSignInOut.setEnabled(true);
                                    bttnSignInOut.setText("SignOut");
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(ChatActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    bttnSignInOut.setEnabled(true);
                                    txtUser.setEnabled(true);
                                    txtPassword.setEnabled(true);
                                    switchMode.setEnabled(true);
                                }
                            });
                }
            } else {
                FirebaseAuth.getInstance().signOut();
                currentUser = mAuth.getCurrentUser();
                bttnSignInOut.setText("SignIn");
                txtUser.setEnabled(true);
                txtPassword.setEnabled(true);
                switchMode.setEnabled(true);
            }
        } else {
            Toast.makeText(this, "It's the end of click", Toast.LENGTH_LONG).show();
        }
    }

    private void registerForActivityResult() {
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        //Toast.makeText(this, "onChildAdded", Toast.LENGTH_LONG).show();
        messageAdaptors.add(snapshot.getValue(ChatMessage.class));
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Toast.makeText(this, "onChildChanged", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        Toast.makeText(this, "onChildRemoved", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Toast.makeText(this, "onChildMoved", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(this, "onCancelled", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            bttnSignInOut.setText("Create");
        } else {
            bttnSignInOut.setText("SignIn");
        }
    }
}
