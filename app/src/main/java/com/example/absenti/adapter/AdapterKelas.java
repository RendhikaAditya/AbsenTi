package com.example.absenti.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.absenti.R;
import com.example.absenti.actifity.AddActivity;
import com.example.absenti.actifity.DetailAbsen;
import com.example.absenti.model.ModelKelas;

import java.util.List;

public class AdapterKelas extends RecyclerView.Adapter<AdapterKelas.ListViewHolder> {
    private List<ModelKelas> kelas;
    private Context context;

    public AdapterKelas(List<ModelKelas> kelas) {
        this.kelas = kelas;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_kelas, parent, false);
        AdapterKelas.ListViewHolder holder = new AdapterKelas.ListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        final  ModelKelas model = kelas.get(position);
        holder.matkul.setText(model.getMatkul());
        holder.kelas.setText(model.getKelas());
        holder.tgl.setText(model.getTgl());



            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailAbsen.class);
                    intent.putExtra("IDK", model.getId());
                    v.getContext().startActivity(intent);
                }
            });


    }

    @Override
    public int getItemCount() {
        return kelas.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView tgl, matkul, kelas;
        private RelativeLayout row;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tgl = itemView.findViewById(R.id.txtTgl);
            kelas = itemView.findViewById(R.id.txtKelas);
            matkul = itemView.findViewById(R.id.txtMatkul);
            row = itemView.findViewById(R.id.rowKls);
        }
    }
}
