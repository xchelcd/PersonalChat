package xchel.co.personalchat.Extra;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import xchel.co.personalchat.Adapters.ClientAdapter;
import xchel.co.personalchat.Entities.FireBase.Client;
import xchel.co.personalchat.Entities.Logica.LClient;


public class Save {

    public static final String adminName = "Admin Test";

    public static Client client;
    public static RecyclerView recyclerViewMain;
    public static ClientAdapter clientAdapter;
    public static FirebaseRecyclerAdapter adapterChat;
    public static Context context;

    public static final String clientReference = "Cliente";
    public static final String chatReference = "Messages";

    public static final String keyAdmin = "n0TfqX44BTPuopEEqr2dwrKP7sA2";
    public static String keyClient;

    public static LClient lClient;
}
