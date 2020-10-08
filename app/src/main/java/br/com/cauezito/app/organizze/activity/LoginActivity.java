package br.com.cauezito.app.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import br.com.cauezito.app.R;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);

    }
}