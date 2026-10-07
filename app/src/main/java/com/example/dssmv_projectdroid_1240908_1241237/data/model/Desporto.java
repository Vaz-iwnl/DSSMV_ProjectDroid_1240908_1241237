package com.example.dssmv_projectdroid_1240908_1241237.data.model;

import com.google.gson.annotations.SerializedName;

public class Desporto {
    @SerializedName("id")
    private String id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("lotacaoMaxima")
    private int lotacaoMaxima;

    @SerializedName("imagemUrl")
    private String imagemUrl;

    //Construtor vazio para ler os dados da api
    public Desporto() {
    }

    //Construtor Completo serve para criar na app e manda para api POST
    public Desporto(String id, String nome, String descricao, int lotacaoMaxima, String imagemUrl) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.lotacaoMaxima = lotacaoMaxima;
        this.imagemUrl = imagemUrl;
    }

    // get e sets

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getLotacaoMaxima() {
        return lotacaoMaxima;
    }

    public void setLotacaoMaxima(int lotacaoMaxima) {
        this.lotacaoMaxima = lotacaoMaxima;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}