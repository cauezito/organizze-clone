package br.com.cauezito.app.organizze.firebase.usuario;


import com.google.firebase.database.DatabaseReference;

import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.model.Usuario;

public class FirebaseDatabaseUsuario {

    private DatabaseReference banco;

    public FirebaseDatabaseUsuario(){
        this.banco = FirebaseConfig.getDatabaseReference();
    }

    public void salvaUsuario(Usuario usuario){
        banco.child("usuarios").child(usuario.getId()).setValue(usuario);
    }
}
