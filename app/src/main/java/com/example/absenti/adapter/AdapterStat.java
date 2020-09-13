package com.example.absenti.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absenti.R;
import com.example.absenti.model.ModelKelas;
import com.example.absenti.model.ModelMhs;
import com.example.absenti.model.ModelStatus;
import com.example.absenti.util.InterfaceStat;
import com.example.absenti.util.PreferencesKat;

import java.util.List;

public class AdapterStat extends RecyclerView.Adapter<AdapterStat.ListViewHolder> {
    private Context context;
    private List<ModelStatus> status;
    private int row_index = 10;

    public AdapterStat(List<ModelStatus> status) {
        this.status = status;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_stat, parent, false);
        AdapterStat.ListViewHolder holder = new AdapterStat.ListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        final ModelStatus model = status.get(position);
        holder.text.setText(model.getNama());
        holder.cardStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            PreferencesKat.clearUser(context);
            holder.cardStat.setBackgroundColor(Color.parseColor("#216C8F"));
            holder.text.setTextColor(Color.parseColor("#ffffff"));
            PreferencesKat.setId_kategori(context, model.getId());
        }
        else
        {
            holder.cardStat.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.text.setTextColor(Color.parseColor("#216C8F"));
        }

    }

    @Override
    public int getItemCount() {
        return status.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        CardView cardStat;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            text = itemView.findViewById(R.id.statMhs);
            cardStat = itemView.findViewById(R.id.cardStat);
        }
    }
}
