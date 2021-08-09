package xchel.co.personalchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xchel.co.personalchat.Entities.FireBase.Message;
import xchel.co.personalchat.Entities.Logica.LMessage;
import xchel.co.personalchat.Extra.ClientDao;
import xchel.co.personalchat.R;
import xchel.co.personalchat.Holders.MessageHolder;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {

    private Context context;
    private List<LMessage> messageList = new ArrayList<>();

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public int addMessage(LMessage lMessage){
        messageList.add(lMessage);
        int position = messageList.size() - 1;
        notifyItemInserted(messageList.size());
        return position;
    }
    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0)
            return new MessageHolder(LayoutInflater.from(context).inflate(R.layout.card_view_message_emisor, parent,false));
        else
            return new MessageHolder(LayoutInflater.from(context).inflate(R.layout.card_view_message_receptor, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        //ponener los valores los elementos del cardView
        holder.getMessageTextView().setText(messageList.get(position).getMessage().getMessage());
        holder.getDateTextView().setText(messageList.get(position).dateMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getlClient() != null)
            if(messageList.get(position).getlClient().getKey().equals(ClientDao.getInstance().getKeyClient()))
                return 1;
            else return 0;
        else return 0;
    }

    public void updateMessage(int position, LMessage lMessage) {
        messageList.set(position, lMessage);
        notifyItemChanged(position);
    }
}
