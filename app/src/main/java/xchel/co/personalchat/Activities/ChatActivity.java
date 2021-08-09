package xchel.co.personalchat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import xchel.co.personalchat.Adapters.MessageAdapter;
import xchel.co.personalchat.Entities.FireBase.Client;
import xchel.co.personalchat.Entities.Logica.LClient;
import xchel.co.personalchat.Entities.Logica.LMessage;
import xchel.co.personalchat.Extra.ClientDao;
import xchel.co.personalchat.Extra.MessageDao;
import xchel.co.personalchat.R;
import xchel.co.personalchat.Extra.Save;
import xchel.co.personalchat.Entities.FireBase.Message;

import static xchel.co.personalchat.Extra.Save.lClient;

public class ChatActivity extends AppCompatActivity {

    private String currentDate;

    private String message;
    private Message m;
    private String emisorKey, receptorKey;

    private String KEYRECEPT;
    private static final String KEYRECEPTRESULT = "keyreceptresult";
    private static final String NAMEUIDKEY = "nameuidkey";

    private ImageButton backImageButton;
    private ImageButton optionsButton;
    private RecyclerView recyclerViewChat;
    private ImageButton sendMessageImageButton;
    private EditText writeMessageEditText;
    private TextView nameClientTextView;


    FirebaseAuth firebaseAuth;

    private MessageAdapter adapterMessage;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#7C008DFF"));
        setContentView(R.layout.activity_chat);
        currentDate = new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date());
        bundle = getIntent().getExtras();

        KEYRECEPT = bundle.getString(KEYRECEPTRESULT);
        if(KEYRECEPT == null){
            KEYRECEPT = bundle.getString("aux");
        }
        inits();

        firebaseAuth = FirebaseAuth.getInstance();

        adapterMessage = new MessageAdapter(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        recyclerViewChat.setLayoutManager(l);
        recyclerViewChat.setAdapter(adapterMessage);
        listeners();
        adapterMessage.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        if (Save.client.getType() == 0) {
            nameClientTextView.setText(bundle.getString(NAMEUIDKEY));
        //    KEYRECEPT = bundle.getString(KEYRECEPTRESULT);
        //    emisor = Save.keyAdmin;
        //    receptor = KEYRECEPT;
        //    databaseReference = database.getReference
        //            (Save.chatReference + "/" + Save.keyAdmin + "/" + KEYRECEPT);
        //    databaseReferenceAux = database.getReference
        //            (Save.chatReference + "/" + KEYRECEPT + "/" + Save.keyAdmin);
        } else if (Save.client.getType() == 1) {
            nameClientTextView.setText(Save.adminName);
        //    emisor = Save.keyClient;
        //    receptor = Save.keyAdmin;
        //    databaseReference = database.getReference
        //            (Save.chatReference + "/" + Save.keyClient + "/" + Save.keyAdmin);
        //    databaseReferenceAux = database.getReference
        //            (Save.chatReference + "/" + Save.keyAdmin + "/" + Save.keyClient);
        }
        //Toast.makeText(this, "admin :"+Save.keyAdmin
        //        +"\n\nrecept: "+KEYRECEPT
        //        +"\n\nclient: "+Save.keyClient
        //        , Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference(Save.chatReference).
                child(ClientDao.getInstance().getKeyClient()).
                child(KEYRECEPT).
                addChildEventListener(new ChildEventListener() {
                    Map<String, LClient> mapClientTemporales = new HashMap<>();

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final Message message = dataSnapshot.getValue(Message.class);
                        final LMessage lMessage = new LMessage(dataSnapshot.getKey(), message);
                        final int position = adapterMessage.addMessage(lMessage);
                        if (mapClientTemporales.get(message.getKeyEmisor()) != null) {
                            lMessage.setlClient(mapClientTemporales.get(message.getKeyEmisor()));
                            adapterMessage.updateMessage(position, lMessage);
                        } else {
                            ClientDao.getInstance().obtenerInformacionDeClientPorLLave(message.getKeyEmisor(), new ClientDao.IDevolverClient() {
                                @Override
                                public void devolverClient(LClient lClient) {
                                    mapClientTemporales.put(message.getKeyEmisor(), lClient);
                                    lMessage.setlClient(lClient);
                                    adapterMessage.updateMessage(position, lMessage);
                                }

                                @Override
                                public void devolverError(String error) {
                                    Toast.makeText(ChatActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    private void listeners() {
        sendMessageImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = writeMessageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    m = new Message();
                    m.setMessage(message);
                    m.setPictureSent(false);
                    m.setKeyEmisor(ClientDao.getInstance().getKeyClient());
                    //databaseReference.push().setValue(m);
                    //databaseReferenceAux.push().setValue(m);
                    MessageDao.getInstance().insertNewMessage(ClientDao.getInstance().getKeyClient(), KEYRECEPT, m);
                    writeMessageEditText.setText("");
                }
            }
        });
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Save.client.getType() == 0) {
                    //FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ChatActivity.this, MainActivity.class));
                    finish();
                } else if (Save.client.getType() == 1) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ChatActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatActivity.this, "option", Toast.LENGTH_SHORT).show();
            }
        });
        writeMessageEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setScrollbar();
                return false;
            }
        });
    }

    private void setScrollbar() {
        recyclerViewChat.scrollToPosition(adapterMessage.getItemCount() - 1);
    }

    private void inits() {
        backImageButton = findViewById(R.id.backImageButton);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        sendMessageImageButton = findViewById(R.id.sendMessageImageButton);
        writeMessageEditText = findViewById(R.id.writeMessageEditText);
        optionsButton = findViewById(R.id.optionsButton);
        nameClientTextView = findViewById(R.id.nameClientTextView);
    }
}
