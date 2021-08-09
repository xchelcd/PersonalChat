package xchel.co.personalchat.Entities.FireBase;

import com.google.firebase.database.ServerValue;

public class Message {
    private String message;
    private boolean pictureSent;
    private String keyEmisor;
    private Object createTimestamp;

    public Object getCreateTimestamp() {
        return createTimestamp;
    }

    public Message() {
        createTimestamp = ServerValue.TIMESTAMP;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

     public boolean isPictureSent() {
        return pictureSent;
    }

    public void setPictureSent(boolean pictureSent) {
        this.pictureSent = pictureSent;
    }

    public String getKeyEmisor() {
        return keyEmisor;
    }

    public void setKeyEmisor(String keyEmisor) {
        this.keyEmisor = keyEmisor;
    }
}
