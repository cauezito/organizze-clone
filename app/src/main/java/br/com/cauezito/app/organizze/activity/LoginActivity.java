package br.com.cauezito.app.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.firebase.usuario.FirebaseUsuario;
import br.com.cauezito.app.organizze.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etSenha;
    Button entrar;

    FirebaseUsuario firebaseUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmailLogin);
        etSenha = findViewById(R.id.etSenhaLogin);
        entrar = findViewById(R.id.btEntrar);

        entrar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();

                if(validaCampos(email, senha)){
                    Usuario usuario = new Usuario();
                    usuario.setSenha(senha);
                    usuario.setEmail(email);

                    firebaseUsuario = new FirebaseUsuario(LoginActivity.this);
                    firebaseUsuario.autenticaUsuario(usuario);
                }
            }
        });

    }

    private boolean validaCampos(String email, String senha){
        if(!email.isEmpty() && !senha.isEmpty()){
            return true;
        } else {
            Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}