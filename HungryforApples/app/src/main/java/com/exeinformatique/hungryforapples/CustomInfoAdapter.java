package com.exeinformatique.hungryforapples;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomInfoAdapter {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titre;
        TextView description;
        TextView heureOuverture;
        TextView adresse;

        public MyViewHolder(View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.textView_titre);
            description = itemView.findViewById(R.id.textView_description);
            heureOuverture = itemView.findViewById(R.id.textView_heure_ouverture);
            adresse = itemView.findViewById(R.id.textView_adresse);
        }
    }

    public CustomInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_info_window, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
}