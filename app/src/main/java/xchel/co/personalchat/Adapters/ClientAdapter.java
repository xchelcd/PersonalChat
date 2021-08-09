package xchel.co.personalchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xchel.co.personalchat.Entities.FireBase.Client;
import xchel.co.personalchat.R;
import xchel.co.personalchat.Holders.ClientHolder;

public class ClientAdapter extends RecyclerView.Adapter<ClientHolder> {

    private Context context;
    List<Client> clientList = new ArrayList<>();


    public ClientAdapter(Context context) {
        this.context = context;
    }

    public void addClient(Client client){
        clientList.add(client);
        notifyItemInserted(clientList.size());
    }
    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_view_client, parent, false);
        return new ClientHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientHolder holder, int position) {
        //ponener los valores los elementos del cardView
        holder.getNameClientTextView().setText(clientList.get(position).getName());
        //holder.getDateClientTextView().setText(clientList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }
}
