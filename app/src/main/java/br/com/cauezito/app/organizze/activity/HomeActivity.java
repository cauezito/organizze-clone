package br.com.cauezito.app.organizze.activity;


import android.content.Intent;
import android.os.Bundle;

import com.github.clans.fab.FloatingActionMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import br.com.cauezito.app.R;

public class HomeActivity extends AppCompatActivity {

    FloatingActionMenu fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void adicionaDespesa(View view){
        startActivity(new Intent(this, DespesaActivity.class));
    }

    public void adicionaEntrada(View view){
        startActivity(new Intent(this, EntradaActivity.class));
    }


}