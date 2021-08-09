package xchel.co.personalchat.Entities.Logica;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xchel.co.personalchat.Entities.FireBase.Client;
import xchel.co.personalchat.Extra.ClientDao;

public class LClient {

    private Client client;
    private String key;

    public LClient(String key, Client client) {
        this.client = client;
        this.key = key;
    }


    public String getDateCreation(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(ClientDao.getInstance().dateCreationClient());
        return simpleDateFormat.format(date);
    }

    public String getLastSignIn(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date = new Date(ClientDao.getInstance().lastSignIn());
        return simpleDateFormat.format(date);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
