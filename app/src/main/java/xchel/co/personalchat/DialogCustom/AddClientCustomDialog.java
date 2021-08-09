package xchel.co.personalchat.DialogCustom;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import xchel.co.personalchat.Extra.Save;
import xchel.co.personalchat.R;
import xchel.co.personalchat.Adapters.ClientAdapter;
import xchel.co.personalchat.Entities.FireBase.Client;

public class AddClientCustomDialog extends DialogFragment {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy - HH:mm");
    String currentDate = simpleDateFormat.format(new Date());

    private final int id = 0;
    private final String name = "Xchel Alonso";
    private final String lastName = "Carranza de la O";
    private final String emailC = "xchel.co@gmail.com";
    private final String comments = "Admin";
    private final String concept = "Admin";
    private int type = 1;
    private final String password = "xchelcd";

    private Button okButton;
    private Button cancelButton;
    private TextView idTextView;
    private EditText nameEditText;
    private EditText lastNameEditText;
    private EditText commentsEditText;
    private Spinner spinner;
    private EditText emailEditText;
    private EditText conceptoEditText;

    private ClientAdapter clientAdapter = Save.clientAdapter;
    private FirebaseRecyclerAdapter adapterC = Save.adapterChat;


    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

    private String email;
    private String emailPass[];

    private String admin = "Admin";
    private String user = "User";
    private boolean flag = true;


    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_client_custom_dialog, null, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findById(dialogView);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //databaseReference = database.getReference(Save.clientReference);

        //Add button
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarEmail(emailEditText.getText().toString()) &&
                        validarName(nameEditText.getText().toString()) &&
                        validarLastName(lastNameEditText.getText().toString())) {
                    email = emailEditText.getText().toString().trim();
                    emailPass = email.split("@");
                    firebaseAuth.createUserWithEmailAndPassword(email, emailPass[0])
                            //firebaseAuth.createUserWithEmailAndPassword(email, password)//crear usuario maestro
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        taskSuccessful();
                                        //Toast.makeText(getActivity(), "Registro exitoso"+
                                        //        nameEditText.getText().toString()+"\n"+
                                        //        lastNameEditText.getText().toString()+"\n"+
                                        //        emailEditText.getText().toString()+"\n"+
                                        //        emailPass,
                                        //        Toast.LENGTH_LONG).show();
                                        //taskSuccessfulMaster();//crear usuario maestro
                                    } else {
                                    }
                                }
                            });
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Dato incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        //idTextView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        flag = !flag;
        //        String typeString = flag ? user : admin;
        //        idTextView.setText(typeString);
        //        type = flag ? 1 : 0;
        //        Toast.makeText(getContext(), type + "- " + typeString, Toast.LENGTH_SHORT).show();
        //    }
        //});
        idTextView.setText(user);
        alertDialog.show();
        alertDialog.dismiss();
        return alertDialog;
    }

    private void taskSuccessful() {
        Client c = new Client();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        DatabaseReference reference = database.getReference(Save.clientReference + "/" + currentUser.getUid());
        reference.setValue(setClient());

    }

    private void taskSuccessfulMaster() {
        Client c = new Client();
        c.setId(id);
        c.setName(name);
        c.setLastName(lastName);
        c.setEmail(emailC);
        c.setComments(comments);
        c.setConcept(concept);
        c.setType(type);//1 clientes, 0 usuarios
        //c.setType(type);//1 clientes(admin), 0 usuarios
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        DatabaseReference reference = database.getReference(Save.clientReference + "/" + currentUser.getUid());
        reference.setValue(c);
    }


    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private Client setClient() {
        Client c = new Client();
        c.setId(0);
        c.setName(nameEditText.getText().toString());
        c.setLastName(lastNameEditText.getText().toString());
        c.setEmail(emailEditText.getText().toString().trim());
        c.setComments(commentsEditText.getText().toString());
        c.setConcept(conceptoEditText.getText().toString());
        c.setType(1);//1 clientes, 0 usuarios
        //c.setType(type);
        return c;
    }

    private boolean validarName(String name) {
        return !name.isEmpty();
    }

    private boolean validarLastName(String lastName) {
        return !lastName.isEmpty();
    }

    private void findById(View v) {
        spinner = v.findViewById(R.id.spinner);
        okButton = v.findViewById(R.id.okButton);
        cancelButton = v.findViewById(R.id.cancelButton);
        idTextView = v.findViewById(R.id.idTextView);
        nameEditText = v.findViewById(R.id.nameEditText);
        lastNameEditText = v.findViewById(R.id.lastNameEditText);
        commentsEditText = v.findViewById(R.id.commentsEditText);
        emailEditText = v.findViewById(R.id.emailEditText);
        conceptoEditText = v.findViewById(R.id.conceptoEditText);
        //recyclerViewChat = v.findViewById(R.id.recyclerViewMain);
    }
}
