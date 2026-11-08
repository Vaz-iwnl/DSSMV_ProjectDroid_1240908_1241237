package com.example.dssmv_projectdroid_1240908_1241237.data.model;

import com.google.gson.annotations.SerializedName;

public class Utilizador {
    @SerializedName("_id")
    private String id;
    private String nome;
    private String email;
    private String password;
    private boolean professor;
    private boolean active = true;

    public Utilizador() {
    }
    public Utilizador(String nome, String email, String password, boolean professor) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.professor = professor;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isProfessor() {
        return professor;
    }

    public void setProfessor(boolean professor) {
        this.professor = professor;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}