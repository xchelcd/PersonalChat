package xchel.co.personalchat.Extra;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import xchel.co.personalchat.Entities.FireBase.Message;

public class MessageDao {

    private static MessageDao messageDao;

    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceMessage;

    public static MessageDao getInstance() {
        if (messageDao == null)
            messageDao = new MessageDao();
        return messageDao;
    }

    private MessageDao(){
        database = FirebaseDatabase.getInstance();
        databaseReferenceMessage = database.getReference(Save.chatReference);
    }

    public void insertNewMessage(String emisorKey, String receptorKey, Message message){
        DatabaseReference referenceEmisor = databaseReferenceMessage.child(emisorKey).child(receptorKey);
        DatabaseReference referenceReceptor = databaseReferenceMessage.child(receptorKey).child(emisorKey);
        referenceEmisor.push().setValue(message);
        referenceReceptor.push().setValue(message);
    }
}
