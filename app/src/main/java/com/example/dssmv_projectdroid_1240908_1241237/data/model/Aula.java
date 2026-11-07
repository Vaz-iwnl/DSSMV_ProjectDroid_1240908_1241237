package com.example.dssmv_projectdroid_1240908_1241237.data.model;

import com.google.gson.annotations.SerializedName;

public class Aula {

    @SerializedName("_id")
    private String id;

    private String modalidade;
    private String professor;

    @SerializedName("dia_semana")
    private String diaSemana;

    private String hora;
    private String duracao;
    private int lotacao;

    // Getters
    public String getId() { return id; }
    public String getModalidade() { return modalidade; }
    public String getProfessor() { return professor; }
    public String getDiaSemana() { return diaSemana; }
    public String getHora() { return hora; }
    public String getDuracao() { return duracao; }
    public int getLotacao() { return lotacao; }

    // Setters (Adicionados para corrigir o erro)
    public void setId(String id) { this.id = id; }
    public void setModalidade(String modalidade) { this.modalidade = modalidade; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public void setHora(String hora) { this.hora = hora; }
    public void setDuracao(String duracao) { this.duracao = duracao; }
    public void setLotacao(int lotacao) { this.lotacao = lotacao; }
}