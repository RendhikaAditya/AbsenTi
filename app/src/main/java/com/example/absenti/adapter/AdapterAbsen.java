package com.example.absenti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absenti.R;
import com.example.absenti.model.ModelKelas;
import com.example.absenti.model.ModelMhs;

import java.util.List;

public class AdapterAbsen extends RecyclerView.Adapter<AdapterAbsen.ListViewHolder> {
    private Context context;
    private List<ModelMhs> mhs;

    public AdapterAbsen(List<ModelMhs> mhs) {
        this.mhs = mhs;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_absen, parent, false);
        AdapterAbsen.ListViewHolder holder = new AdapterAbsen.ListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final ModelMhs model = mhs.get(position);
        holder.nama.setText(model.getNama());
        holder.status.setText(model.getStat());
    }

    @Override
    public int getItemCount() {
        return mhs.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView nama, status;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama = itemView.findViewById(R.id.namaAbsen);
            status = itemView.findViewById(R.id.statAbsen);
        }
    }
}
