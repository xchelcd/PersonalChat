package xchel.co.personalchat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xchel.co.personalchat.Entities.Logica.LClient;
import xchel.co.personalchat.R;
import xchel.co.personalchat.Adapters.ClientAdapter;
import xchel.co.personalchat.Entities.FireBase.Client;
import xchel.co.personalchat.Holders.ClientHolder;

import xchel.co.personalchat.DialogCustom.AddClientCustomDialog;
import xchel.co.personalchat.Extra.Save;
import xchel.co.personalchat.Adapters.MessageAdapter;

public class MainActivity extends AppCompatActivity {

    String a = "";

    private static final String KEYRECEPTRESULT = "keyreceptresult";
    private static final int KEYRECEPTREQUESTCODE = 615;
    private static final String NAMEUIDKEY = "nameuidkey";

    private String currentDate;
    private Boolean navigateFlag = true;

    private MessageAdapter messageAdapter;
    private ClientAdapter clientAdapter;


    private RecyclerView recyclerViewMain;
    private ImageButton navigateButton;
    private ImageButton addButton;
    private ImageButton searchButton;
    //private ImageButton optionsButton;
    private ImageButton logoutButton;
    private TextView searchEditText;
    private TextView textViewChat;
    private ImageButton backSearchImageButton;
    private LinearLayout linearLayout;

    private AddClientCustomDialog addClientCustomDialog;

    private FirebaseRecyclerAdapter adapter;

    private FirebaseDatabase db;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#7C008DFF"));
        setContentView(R.layout.activity_main);
        currentDate = new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date());
        inits();
        textViewChat.setText(Save.client.getName() + " :)");

        FirebaseMessaging.getInstance().subscribeToTopic("allDevice");




        Save.recyclerViewMain = recyclerViewMain;
        final LinearLayoutManager l = new LinearLayoutManager(this);
        recyclerViewMain.setLayoutManager(l);


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                //ordenar por ultima fecha
                .child(Save.clientReference);
        FirebaseRecyclerOptions<Client> options =
                new FirebaseRecyclerOptions.Builder<Client>()
                        .setQuery(query, Client.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Client, ClientHolder>(options) {
            @Override
            public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_client, parent, false);
                return new ClientHolder(view);
            }
            @Override
            protected void onBindViewHolder(final ClientHolder holder, int position, final Client model) {
                final LClient lc = new LClient(getSnapshots().getSnapshot(position).getKey(), model);
                holder.getNameClientTextView().setText(model.getName());
                lc.getDateCreation();
                holder.getDateClientTextView().setText("Last "+lc.getLastSignIn());
                holder.getDateClientCreationTextView().setText("Creation "+lc.getDateCreation());

                holder.getConceptClientTextView().setText(model.getConcept());//cambié mensaje por concepto el el main donde e muestran los clientes

                holder.getLinearLayoutClients().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        intent.putExtra(KEYRECEPTRESULT, lc.getKey());
                        intent.putExtra(NAMEUIDKEY, model.getName());
                        startActivityForResult(intent, KEYRECEPTREQUESTCODE);
                    }
                });
            }
        };
        Save.adapterChat = adapter;
        recyclerViewMain.setAdapter(adapter);

        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference(Save.chatReference);
        firebaseAuth = FirebaseAuth.getInstance();


        listeners();


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void listeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClientCustomDialog = new AddClientCustomDialog();
                addClientCustomDialog.setCancelable(false);
                addClientCustomDialog.show(getSupportFragmentManager(), "nombreDialog");
            }
        });
        //dbReference.push().setValue(new Message());
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FABSelected(navigateFlag);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
                FABSelected(false);
                navigateButton.setVisibility(View.INVISIBLE);
                textViewChat.setVisibility(View.INVISIBLE);
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        backSearchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButtonPressed();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }



    private void performSearch() {
        searchEditText.clearFocus();
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        Toast.makeText(this, "ENTER", Toast.LENGTH_SHORT).show();
        //...perform search
    }

    private void FABSelected(boolean flag){
        if (flag) {
            navigateFlag = false;
            addButton.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            //optionsButton.setVisibility(View.VISIBLE);
            navigateButton.setBackgroundResource(R.drawable.ic_navigate_next_black_24dp);
        }else{
            navigateFlag = true;
            logoutButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.INVISIBLE);
            //optionsButton.setVisibility(View.INVISIBLE);
            navigateButton.setBackgroundResource(R.drawable.ic_navigate_before_black_24dp);
        }
    }

    private void backButtonPressed() {
        linearLayout.setVisibility(View.INVISIBLE);
        textViewChat.setVisibility(View.VISIBLE);
        navigateButton.setVisibility(View.VISIBLE);
        searchEditText.setText("");
    }

    private void inits() {
        recyclerViewMain = findViewById(R.id.recyclerViewMain);
        navigateButton = findViewById(R.id.navigateButton);
        addButton = findViewById(R.id.addButton);
        searchButton = findViewById(R.id.searchButton);
        //optionsButton = findViewById(R.id.optionsButton);
        searchEditText = findViewById(R.id.searchEditText);
        textViewChat = findViewById(R.id.textViewChat);
        backSearchImageButton  = findViewById(R.id.backSearchImageButton);
        linearLayout = findViewById(R.id.linearLayout);
        logoutButton = findViewById(R.id.logoutButton);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getUser(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            DatabaseReference reference = db.getReference(Save.clientReference+"/"+currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Client c = dataSnapshot.getValue(Client.class);
                    Save.client = c;
                    textViewChat.setText(c.getName() + " :)");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
