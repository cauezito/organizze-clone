package br.com.cauezito.app.organizze.firebase.usuario;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.cauezito.app.organizze.activity.HomeActivity;
import br.com.cauezito.app.organizze.activity.LoginActivity;
import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.model.Usuario;

public class FirebaseUsuario {
    private FirebaseAuth autenticacao;
    Activity activity;

    public FirebaseUsuario(Activity activity) {
         this.autenticacao = FirebaseConfig.getFirebaseAutenticacao();
         this.activity = activity;
    }

    public void cadastraUsuario(Usuario usuario){

        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(activity, "Cadastro realizado com sucesso! :)", Toast.LENGTH_LONG).show();
                            irParaHome();
                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                excecao = "Digite uma senha mais forte";
                            } catch (FirebaseAuthInvalidCredentialsException e2){
                                excecao = "Digite um e-mail válido";
                            } catch (FirebaseAuthUserCollisionException e3){
                                excecao = "Essa conta já existe";
                            } catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(activity, excecao, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void autenticaUsuario(Usuario usuario){
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            irParaHome();
                        }else{
                            String excecao = "";
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e){
                                excecao = "Essa conta não existe";
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "O e-mail e a senha não correspondem";
                            } catch (Exception e){
                                excecao = "Erro ao autenticar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(activity, excecao, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void irParaHome() {
        activity.startActivity(new Intent(activity, HomeActivity.class));
        activity.finish();
    }

    public void verificaUsuarioLogado(){
        if(autenticacao.getCurrentUser() != null){
            irParaHome();
        }
    }

    public void deslogaUsuario(){
        autenticacao.signOut();
    }

}
