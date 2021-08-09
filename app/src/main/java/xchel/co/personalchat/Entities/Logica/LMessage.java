package xchel.co.personalchat.Entities.Logica;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xchel.co.personalchat.Entities.FireBase.Message;

public class LMessage {

    private String key;
    private Message message;
    private LClient lClient;

    public LMessage(String key, Message message) {
        this.key = key;
        this.message = message;
    }

    public LClient getlClient() {
        return lClient;
    }

    public void setlClient(LClient lClient) {
        this.lClient = lClient;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Message getMessage() {
        return message;
    }

    public String dateMessage(){
        Date date = new Date(getCreateTimestapLong());
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        return prettyTime.format(date);
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public long getCreateTimestapLong(){
        return (long) message.getCreateTimestamp();
    }

}
