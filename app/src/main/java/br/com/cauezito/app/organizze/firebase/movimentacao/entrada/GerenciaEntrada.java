package br.com.cauezito.app.organizze.firebase.movimentacao.entrada;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.cauezito.app.organizze.activity.DespesaActivity;
import br.com.cauezito.app.organizze.activity.EntradaActivity;
import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.firebase.movimentacao.despesa.GerenciaDespesa;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.model.Usuario;
import br.com.cauezito.app.organizze.utils.Base64Custom;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class GerenciaEntrada implements IGerenciaEntrada {

    private static FirebaseAuth autenticacao;
    private static Double receitaTotal = 0D;
    private static DatabaseReference banco;
    private Activity entradaActivity;
    private Double receitaAtualizada;

    private static String RECEITA_TOTAL = "receitaTotal";
    private static String USUARIOS = "usuarios";
    private static String MOVIMENTACAO = "movimentacao";

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
                        receitaAtualizada = movimentacao.getValor() + GerenciaEntrada.recuperaReceitaTotal();
                        atualizaReceita(receitaAtualizada);

                        Toast.makeText(entradaActivity.getBaseContext(), "Entrada salva", Toast.LENGTH_LONG).show();
                    }
                }
        ).addOnFailureListener(entradaActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(entradaActivity.getBaseContext(), "Não foi possível salvar a entraad", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public static Double recuperaReceitaTotal() {
        String idUsuario = Base64Custom.codificaBase64(autenticacao.getCurrentUser().getEmail());

        banco.child(USUARIOS).child(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return receitaTotal;
    }

    public void atualizaReceita(Double despesa){
        String idUsuario = Base64Custom.codificaBase64(autenticacao.getCurrentUser().getEmail());
        banco.child(USUARIOS).child(idUsuario).child(RECEITA_TOTAL).setValue(despesa);
    }

    public static Double alteraReceitaTotal(Double receita){
        return receitaTotal -= receita;
    }
}

