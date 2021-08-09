package xchel.co.personalchat.Extra;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import xchel.co.personalchat.Entities.FireBase.Client;
import xchel.co.personalchat.Entities.Logica.LClient;

public class ClientDao {

    private static ClientDao clientDao;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ClientDao(){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Save.clientReference);
    }

    public interface IDevolverClient{
        public void devolverClient(LClient lClient);
        public void devolverError(String error);
    }

    public static ClientDao getInstance() {
        if (clientDao == null)
            clientDao = new ClientDao();
        return clientDao;
    }

    public String getKeyClient() {
        return FirebaseAuth.getInstance().getUid();
    }

    public long dateCreationClient(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }
    public long lastSignIn(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }

    public boolean isLoged(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void obtenerInformacionDeClientPorLLave(final String key, final IDevolverClient iDevolverClient){
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Client client = dataSnapshot.getValue(Client.class);
                LClient lClient = new LClient(key, client);
                iDevolverClient.devolverClient(lClient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iDevolverClient.devolverError(databaseError.getMessage());
            }
        });

    }

}
