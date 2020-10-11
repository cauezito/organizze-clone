package br.com.cauezito.app.organizze.firebase.movimentacao.despesa;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import br.com.cauezito.app.organizze.activity.DespesaActivity;
import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.utils.Base64Custom;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class GerenciaDespesa {

    private DatabaseReference banco;
    private FirebaseAuth autenticacao;
    private Activity despesaActivity;

    public GerenciaDespesa(Activity activity){
        this.banco = FirebaseConfig.getDatabaseReference();
        this.autenticacao = FirebaseConfig.getFirebaseAutenticacao();
        this.despesaActivity = activity;
    }

    public void salvaDespesa(Movimentacao movimentacao){
        String id = Base64Custom.codificaBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateCustom.formataDataMesAno(movimentacao.getData().toString());

        banco.child("movimentacao").child(id).child(mesAno).push().setValue(movimentacao).addOnSuccessListener(
                despesaActivity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(despesaActivity.getBaseContext(), "Despesa salva", Toast.LENGTH_LONG).show();
                        DespesaActivity.limpaCampos();
                    }
                }
        ).addOnFailureListener(despesaActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(despesaActivity.getBaseContext(), "Não foi possível salvar a despesa", Toast.LENGTH_LONG).show();
                    }
                }
            );
    }
}
