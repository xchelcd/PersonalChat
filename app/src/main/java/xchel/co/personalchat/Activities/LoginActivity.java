package xchel.co.personalchat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import xchel.co.personalchat.R;
import xchel.co.personalchat.Entities.FireBase.Client;
import xchel.co.personalchat.Extra.Save;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser currentUser;

    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private CheckBox rememberCheckBox;
    private ImageButton viewPassWordImageView;

    //private String userET = "prueba@gmail.com";
    //private String passET = "xchelcd";

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findById();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Save.chatReference);
        firebaseAuth = FirebaseAuth.getInstance();
        //startNextActivity();

        //getUser();

        listeners();
    }



    private void listeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = userNameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                if(validarEmail(email) && !password.isEmpty()){
                    loginSuccessful();
                }else{
                    Toast.makeText(LoginActivity.this, "Campos incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewPassWordImageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                passwordEditText.setSelection(passwordEditText.getText().length());
                return true;
            }
        });
    }

    private void loginSuccessful() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getUser();
                        } else {

                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){// y si es administrador type 0
            getUser();
            //startNextActivity();
        }
    }

    private void startNextActivity(){
        try {
            Intent intent = null;
            if(Save.client.getType() == 0){
                Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginActivity.this, MainActivity.class);
            }else if(Save.client.getType() == 1){
                intent = new Intent(LoginActivity.this, ChatActivity.class);
                intent.putExtra("aux", Save.keyAdmin);
                Toast.makeText(this, "Cliente", Toast.LENGTH_SHORT).show();
            }
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void findById() {
        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);
        viewPassWordImageView = findViewById(R.id.viewPassWordImageView);
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void getUser() {
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Save.keyClient = currentUser.getUid();
            userNameEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
            loginButton.setEnabled(false);
            DatabaseReference reference = database.getReference(Save.clientReference+"/"+currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Save.client = dataSnapshot.getValue(Client.class);
                    userNameEditText.setEnabled(true);
                    passwordEditText.setEnabled(true);
                    loginButton.setEnabled(true);
                    startNextActivity();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
