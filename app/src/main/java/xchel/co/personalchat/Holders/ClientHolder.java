package xchel.co.personalchat.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import xchel.co.personalchat.R;

public class ClientHolder extends RecyclerView.ViewHolder {

    private ImageView imageViewClient;
    //private ImageView checkImageView;
    private TextView nameClientTextView;
    private TextView conceptClientTextView;
    private TextView dateClientTextView;
    private TextView dateClientCreationTextView;
    private CardView linearLayoutClients;

    public TextView getDateClientTextView() {
        return dateClientTextView;
    }

    public void setDateClientTextView(TextView dateClientTextView) {
        this.dateClientTextView = dateClientTextView;
    }

    public TextView getDateClientCreationTextView() {
        return dateClientCreationTextView;
    }

    public void setDateClientCreationTextView(TextView dateClientCreationTextView) {
        this.dateClientCreationTextView = dateClientCreationTextView;
    }

    public ClientHolder(@NonNull View itemView) {
        super(itemView);
        imageViewClient = itemView.findViewById(R.id.imageViewClient);
        //checkImageView = itemView.findViewById(R.id.checkImageView);
        nameClientTextView = itemView.findViewById(R.id.nameClientTextView);
        conceptClientTextView = itemView.findViewById(R.id.messageClientTextView);
        dateClientTextView = itemView.findViewById(R.id.dateClientTextView);
        dateClientCreationTextView = itemView.findViewById(R.id.dateClientCreationTextView);
        linearLayoutClients = itemView.findViewById(R.id.linearLayoutClients);
    }

    public CardView getLinearLayoutClients() {
        return linearLayoutClients;
    }

    public void setLinearLayoutClients(CardView linearLayoutClients) {
        this.linearLayoutClients = linearLayoutClients;
    }

    public ImageView getImageViewClient() {
        return imageViewClient;
    }

    public void setImageViewClient(ImageView imageViewClient) {
        this.imageViewClient = imageViewClient;
    }

    public TextView getNameClientTextView() {
        return nameClientTextView;
    }

    public void setNameClientTextView(TextView nameClientTextView) {
        this.nameClientTextView = nameClientTextView;
    }

    public TextView getConceptClientTextView() {
        return conceptClientTextView;
    }

    public void setConceptClientTextView(TextView conceptClientTextView) {
        this.conceptClientTextView = conceptClientTextView;
    }

}
