package com.example.dssmv_projectdroid_1240908_1241237.ui.agendamento;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dssmv_projectdroid_1240908_1241237.R;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Inscricao;
import java.util.ArrayList;
import java.util.List;

public class MinhasAulasAdapter extends RecyclerView.Adapter<MinhasAulasAdapter.ViewHolder> {

    private List<Inscricao> lista = new ArrayList<>();
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Inscricao inscricao);
    }

    public MinhasAulasAdapter(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.lista = inscricoes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aula, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inscricao inscricao = lista.get(position);

        holder.tvModalidade.setText(inscricao.getModalidade());
        holder.tvProfessor.setText("Data: " + inscricao.getData_aula());
        holder.tvHora.setText("");
        holder.tvDuracao.setText("");

        holder.btnInscrever.setText("Cancelar");
        holder.btnInscrever.setBackgroundColor(Color.parseColor("#D32F2F"));

        holder.btnInscrever.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(inscricao);
        });
    }

    @Override
    public int getItemCount() { return lista != null ? lista.size() : 0; }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvModalidade, tvProfessor, tvHora, tvDuracao;
        Button btnInscrever;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModalidade = itemView.findViewById(R.id.tvAulaModalidade);
            tvProfessor = itemView.findViewById(R.id.tvAulaProfessor);
            tvHora = itemView.findViewById(R.id.tvAulaHora);
            tvDuracao = itemView.findViewById(R.id.tvAulaDuracao);
            btnInscrever = itemView.findViewById(R.id.btnInscrever);
        }
    }
}