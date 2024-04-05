package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.sr.reports.models.RegisteredComplaintModel;

import java.util.ArrayList;

public class RegisterComplaintAdapter extends RecyclerView.Adapter<RegisterComplaintAdapter.ViewHolder> {

    Context context;
    ArrayList<RegisteredComplaintModel> list;

    public RegisterComplaintAdapter(Context context, ArrayList<RegisteredComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RegisterComplaintAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterComplaintAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
