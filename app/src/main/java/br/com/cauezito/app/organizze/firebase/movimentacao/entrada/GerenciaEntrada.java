package br.com.cauezito.app.organizze.firebase.movimentacao.entrada;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import br.com.cauezito.app.organizze.activity.DespesaActivity;
import br.com.cauezito.app.organizze.activity.EntradaActivity;
import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.utils.Base64Custom;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class GerenciaEntrada implements IGerenciaEntrada {
    private DatabaseReference banco;
    private FirebaseAuth autenticacao;
    private Activity entradaActivity;

    public GerenciaEntrada(Activity activity) {
        this.banco = FirebaseConfig.getDatabaseReference();
        this.autenticacao = FirebaseConfig.getFirebaseAutenticacao();
        this.entradaActivity = activity;
    }


    @Override
    public void salvaEntrada(Movimentacao movimentacao) {
        String id = Base64Custom.codificaBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateCustom.formataDataMesAno(movimentacao.getData().toString());

        banco.child("movimentacao").child(id).child(mesAno).push().setValue(movimentacao).addOnSuccessListener(
                entradaActivity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(entradaActivity.getBaseContext(), "Entrada salva", Toast.LENGTH_LONG).show();
                        EntradaActivity.limpaCampos();
                    }
                }
        ).addOnFailureListener(entradaActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(entradaActivity.getBaseContext(), "Não foi possível salvar a despesa", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}

