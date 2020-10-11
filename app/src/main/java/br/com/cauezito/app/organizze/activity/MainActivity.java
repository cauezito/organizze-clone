package br.com.cauezito.app.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.firebase.usuario.FirebaseAuthUsuario;

public class MainActivity extends IntroActivity {

    private FirebaseAuthUsuario firebaseAuthUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuthUsuario = new FirebaseAuthUsuario(this);

        /*Remove botões de navegação*/
        setButtonBackVisible(false);
        setButtonNextVisible(false);


        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_1).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_2).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_3).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_4).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_cadastro).canGoBackward(false).canGoForward(false).build());

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuthUsuario.verificaUsuarioLogado();
    }

    public void entrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void cadastrar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }
}