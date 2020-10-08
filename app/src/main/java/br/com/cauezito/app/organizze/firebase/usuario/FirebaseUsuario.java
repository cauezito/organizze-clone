package br.com.cauezito.app.organizze.firebase.usuario;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.cauezito.app.organizze.activity.CadastroActivity;
import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;

public class FirebaseUsuario {
    private FirebaseAuth autenticacao;
    Activity activity;

    public FirebaseUsuario(Activity activity) {
         this.autenticacao = FirebaseConfig.getFirebaseAutenticacao();
         this.activity = activity;
    }

    public void cadastrarUsuario(String email, String senha){
        autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) Toast.makeText(activity, "Cadastro realizado com sucesso! :)", Toast.LENGTH_LONG).show();
                        else Toast.makeText(activity, "Erro ao concluir cadastro", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
