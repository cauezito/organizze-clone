package br.com.cauezito.app.organizze.firebase.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig {
    private static FirebaseAuth autenticacao;
    private static DatabaseReference banco;

    //retorna a instância do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null) autenticacao = FirebaseAuth.getInstance();
        return autenticacao;
    }

    //retorna a instância do DatabaseReference
    public static DatabaseReference getDatabaseReference(){
        if(banco == null) {
            banco = FirebaseDatabase.getInstance().getReference();
        }

        return banco;
    }
}
