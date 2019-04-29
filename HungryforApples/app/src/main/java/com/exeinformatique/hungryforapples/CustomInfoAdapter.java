package com.exeinformatique.hungryforapples;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CustomInfoAdapter extends RecyclerView.Adapter<CustomInfoAdapter.MyViewHolder> {
    List<Todo> todoList;

    public CustomInfoAdapter(List<Todo> todoList){
        this.todoList = todoList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titre;
        TextView description;
        public MyViewHolder(View itemView) {
            super(itemView);
            titre = itemView.findViewById(R.id.textView_title);
            description = itemView.findViewById(R.id.textView_description);
        }
    }
    @NonNull
    @Override
    public CustomInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_info_window, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomInfoAdapter.MyViewHolder holder, int position) {
        Todo todoToDisplay = todoList.get(position);
        holder.titre.setText(todoToDisplay.getTitle());
        holder.description.setText(todoToDisplay.getDescription());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
