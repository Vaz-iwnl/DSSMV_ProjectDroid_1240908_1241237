package com.example.dssmv_projectdroid_1240908_1241237.data.model;

import com.google.gson.annotations.SerializedName;

public class Aula {

    @SerializedName("_id")
    private String id;

    private String modalidade;
    private String professor;
    private String data;
    private String hora;
    private String duracao;

    public String getId() { return id; }
    public String getModalidade() { return modalidade; }
    public String getProfessor() { return professor; }
    public String getData() { return data; }
    public String getHora() { return hora; }
    public String getDuracao() { return duracao; }
}