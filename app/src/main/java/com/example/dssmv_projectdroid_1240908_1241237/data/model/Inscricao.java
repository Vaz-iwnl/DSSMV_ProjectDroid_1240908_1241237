package com.example.dssmv_projectdroid_1240908_1241237.data.model;

public class Inscricao {
    private String id_aula;
    private String username_aluno;

    public Inscricao(String id_aula, String username_aluno) {
        this.id_aula = id_aula;
        this.username_aluno = username_aluno;
    }

    public String getId_aula() { return id_aula; }
    public String getUsername_aluno() { return username_aluno; }
}