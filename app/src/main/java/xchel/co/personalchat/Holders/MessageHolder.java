package xchel.co.personalchat.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import xchel.co.personalchat.R;

public class MessageHolder extends RecyclerView.ViewHolder {

    //elementos del cardView, de cada mensaje enviado
    private TextView messageTextView;
    private TextView dateTextView;

    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.dateMessageTextView);
        messageTextView = itemView.findViewById(R.id.messageTextView);
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public void setMessageTextView(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    public TextView getDateTextView() {
        return dateTextView;
    }

    public void setDateTextView(TextView dateTextView) {
        this.dateTextView = dateTextView;
    }
}
