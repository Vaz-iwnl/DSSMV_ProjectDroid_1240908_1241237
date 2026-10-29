package com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Aula;

import java.util.ArrayList;
import java.util.List;

public class AulasAdapter extends RecyclerView.Adapter<AulasAdapter.AulaViewHolder> {

    private List<Aula> listaAulas = new ArrayList<>();
    private OnAulaClickListener listener;

    public interface OnAulaClickListener {
        void onInscreverClick(Aula aula);
    }

    public AulasAdapter(OnAulaClickListener listener) {
        this.listener = listener;
    }

    public void setAulas(List<Aula> aulas) {
        this.listaAulas = aulas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AulaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aula, parent, false);
        return new AulaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AulaViewHolder holder, int position) {
        Aula aulaAtual = listaAulas.get(position);

        holder.tvModalidade.setText(aulaAtual.getModalidade());
        holder.tvProfessor.setText(aulaAtual.getProfessor());
        holder.tvHora.setText(aulaAtual.getHora());
        holder.tvDuracao.setText(" • " + aulaAtual.getDuracao());

        holder.btnInscrever.setOnClickListener(v -> {
            if (listener != null) {
                listener.onInscreverClick(aulaAtual);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAulas != null ? listaAulas.size() : 0;
    }

    static class AulaViewHolder extends RecyclerView.ViewHolder {
        TextView tvModalidade, tvProfessor, tvHora, tvDuracao;
        android.widget.Button btnInscrever;

        public AulaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModalidade = itemView.findViewById(R.id.tvAulaModalidade);
            tvProfessor = itemView.findViewById(R.id.tvAulaProfessor);
            tvHora = itemView.findViewById(R.id.tvAulaHora);
            tvDuracao = itemView.findViewById(R.id.tvAulaDuracao);

            btnInscrever = itemView.findViewById(R.id.btnInscrever);
        }
    }
}