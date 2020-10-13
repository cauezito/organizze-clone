package br.com.cauezito.app.organizze.firebase.usuario;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.model.Usuario;
import br.com.cauezito.app.organizze.utils.Base64Custom;

public class FirebaseDatabaseUsuario {

    private DatabaseReference banco;
    private FirebaseAuth autenticacao;
    private DatabaseReference bancoUsuarios;

    public FirebaseDatabaseUsuario(){
        this.banco = FirebaseConfig.getDatabaseReference();
        this.autenticacao = FirebaseConfig.getFirebaseAutenticacao();
        bancoUsuarios = banco.child("usuarios");
    }

    public void salvaUsuario(Usuario usuario){
        bancoUsuarios.child(usuario.getId()).setValue(usuario);
    }

    public Usuario recuperaEmailId(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String id = Base64Custom.codificaBase64(emailUsuario);

        Usuario usuario = new Usuario();
        usuario.setEmail(emailUsuario);
        usuario.setId(id);

        return usuario;
    }

    public DatabaseReference getBancoUsuarios() {
        return bancoUsuarios;
    }

}
