package br.com.cauezito.app.organizze.firebase.config;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseConfig {
    private static FirebaseAuth autenticacao;

    //retorna a instância do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null) autenticacao = FirebaseAuth.getInstance();
        return autenticacao;
    }
}
