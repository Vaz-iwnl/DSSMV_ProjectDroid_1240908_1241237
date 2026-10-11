package com.example.dssmv_projectdroid_1240908_1241237.ui.desportos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Desporto;

import java.util.List;

public class DesportosAdapter extends RecyclerView.Adapter<DesportosAdapter.DesportoViewHolder> {

    private List<Desporto> listaDesportos;

    public DesportosAdapter(List<Desporto> listaDesportos) {
        this.listaDesportos = listaDesportos;
    }

    @NonNull
    @Override
    public DesportoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desporto, parent, false);
        return new DesportoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DesportoViewHolder holder, int position) {
        Desporto desporto = listaDesportos.get(position);

        holder.tvNome.setText(desporto.getNome());
        holder.tvDescricao.setText(desporto.getDescricao());
        holder.tvLotacao.setText("Lotação máxima: " + desporto.getLotacaoMaxima());

        Glide.with(holder.itemView.getContext())
                .load(desporto.getImagemUrl())
                .centerCrop()
                .into(holder.ivImagem);
    }

    @Override
    public int getItemCount() {
        return listaDesportos != null ? listaDesportos.size() : 0;
    }

    public static class DesportoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagem;
        TextView tvNome, tvDescricao, tvLotacao;

        public DesportoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagem = itemView.findViewById(R.id.ivImagemDesporto);
            tvNome = itemView.findViewById(R.id.tvNomeDesporto);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoDesporto);
            tvLotacao = itemView.findViewById(R.id.tvLotacao);
        }
    }
}