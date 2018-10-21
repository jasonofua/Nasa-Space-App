package com.example.dell.helpie.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dell.helpie.Model.ListModel;
import com.example.dell.helpie.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    List<ListModel> listModels = new ArrayList<>();

    public StudentAdapter(List<ListModel> listModels) {
        this.listModels = listModels;
    }

    @NonNull
    @Override
    public StudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.MyViewHolder holder, int position) {
        ListModel model = listModels.get(position);
        holder.name.setText(model.getName());
        holder.priority.setText("Priority: "+model.getPriority());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.check.setChecked(true);
                holder.mView.setEnabled(false);
                holder.check.setEnabled(false);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,priority;
        View mView;
        CheckBox check;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            name = (TextView)mView.findViewById(R.id.listName);
            priority = (TextView)mView.findViewById(R.id.priority);
            check = (CheckBox)mView.findViewById(R.id.checkbox_meat);


        }
    }
}
