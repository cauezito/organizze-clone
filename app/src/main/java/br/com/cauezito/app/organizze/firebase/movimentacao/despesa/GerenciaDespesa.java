package br.com.cauezito.app.organizze.firebase.movimentacao.despesa;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.utils.Base64Custom;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class GerenciaDespesa {

    private DatabaseReference banco;
    private FirebaseAuth autenticacao;

    public GerenciaDespesa(){
        this.banco = FirebaseConfig.getDatabaseReference();
        this.autenticacao = FirebaseConfig.getFirebaseAutenticacao();
    }

    public void salvaDespesa(Movimentacao movimentacao){
        String id = Base64Custom.codificaBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateCustom.formataDataMesAno(movimentacao.getData().toString());
        banco.child("movimentacao").child(id).child(mesAno).push().setValue(movimentacao);
    }
}
