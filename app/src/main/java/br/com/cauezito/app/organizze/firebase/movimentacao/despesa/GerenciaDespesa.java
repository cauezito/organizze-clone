package br.com.cauezito.app.organizze.firebase.movimentacao.despesa;

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

import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.model.Usuario;
import br.com.cauezito.app.organizze.utils.Base64Custom;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class GerenciaDespesa implements IGerenciaDespesa{

    private static Double despesaTotal = 0D;
    private static FirebaseAuth autenticacao;
    private static DatabaseReference banco;
    private Activity despesaActivity;
    private Double despesaAtualizada;

    private static String DESPESA_TOTAL = "despesaTotal";
    private static String USUARIOS = "usuarios";
    private static String MOVIMENTACAO = "movimentacao";


    public GerenciaDespesa(Activity activity){
        this.banco = FirebaseConfig.getDatabaseReference();
        this.autenticacao = FirebaseConfig.getFirebaseAutenticacao();
        this.despesaActivity = activity;
    }

    public void salvaDespesa(Movimentacao movimentacao){
        String id = Base64Custom.codificaBase64(autenticacao.getCurrentUser().getEmail());
        String mesAno = DateCustom.formataDataMesAno(movimentacao.getData().toString());

        banco.child(MOVIMENTACAO).child(id).child(mesAno).push().setValue(movimentacao).addOnSuccessListener(
                despesaActivity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        despesaAtualizada = movimentacao.getValor() + GerenciaDespesa.recuperaDespesaTotal();
                        atualizaDespesa(despesaAtualizada);

                        Toast.makeText(despesaActivity.getBaseContext(), "Despesa salva", Toast.LENGTH_LONG).show();
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

    public static Double recuperaDespesaTotal() {
        String idUsuario = Base64Custom.codificaBase64(autenticacao.getCurrentUser().getEmail());

        banco.child(USUARIOS).child(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return despesaTotal;
    }

    public static void atualizaDespesa(Double despesa){
        String idUsuario = Base64Custom.codificaBase64(autenticacao.getCurrentUser().getEmail());
        banco.child(USUARIOS).child(idUsuario).child(DESPESA_TOTAL).setValue(despesa);
    }

    public static Double alteraDespesaTotal(Double despesa){
        return despesaTotal -= despesa;
    }
}
