package com.example.dssmv_projectdroid_1240908_1241237.data.model;

import com.google.gson.annotations.SerializedName;

public class Inscricao {
    @SerializedName("_id")
    private String _id;
    private String id_aula;
    private String username_aluno;
    private String data_aula;
    private String modalidade;

    public Inscricao(String id_aula, String username_aluno, String data_aula, String modalidade) {
        this.id_aula = id_aula;
        this.username_aluno = username_aluno;
        this.data_aula = data_aula;
        this.modalidade = modalidade;
    }

    public String get_id() { return _id; }
    public String getId_aula() { return id_aula; }
    public String getUsername_aluno() { return username_aluno; }
    public String getData_aula() { return data_aula; }
    public String getModalidade() { return modalidade; }
}