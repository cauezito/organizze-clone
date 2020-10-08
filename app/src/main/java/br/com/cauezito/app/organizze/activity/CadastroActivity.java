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

public class CadastroActivity extends AppCompatActivity {

    EditText etEmail, etSenha, etNome;
    Button btCadastrar;
    FirebaseUsuario firebaseUsuario;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etNome = findViewById(R.id.etNome);
        btCadastrar = findViewById(R.id.btCadastrar);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();
                String nome = etNome.getText().toString();

                if(validaCampos(email, senha, nome)){
                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setSenha(senha);
                    usuario.setEmail(email);

                    firebaseUsuario = new FirebaseUsuario(CadastroActivity.this);
                    firebaseUsuario.cadastrarUsuario(usuario.getEmail(), usuario.getSenha());

                }
            }
        });
    }

    private boolean validaCampos(String email, String senha, String nome){
        if(!email.isEmpty() && !senha.isEmpty() && !nome.isEmpty()){
            return true;
        } else {
            Toast.makeText(CadastroActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}