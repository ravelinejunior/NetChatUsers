package br.com.ravelineNetUsers.Activities.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.ravelineNetUsers.Activities.BancodeDados.ConfiguracaoFirebase;

public class Usuarios {
    String id;
    String nome;
    String email;
    String senha;

    public Usuarios(){

    }

    //metodo para salvar os dados dos usuarios
    public void salvarUsuarios(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getReferenciaFirebase();
        referenciaFirebase.child("usuarios").child(getId()).setValue(this);
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

    //@Exclude para nao mostrar info no Firebase
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}


