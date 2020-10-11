package br.com.cauezito.app.organizze.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.firebase.movimentacao.despesa.GerenciaDespesa;
import br.com.cauezito.app.organizze.firebase.movimentacao.entrada.GerenciaEntrada;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.model.TipoEnum;

public class EntradaActivity extends AppCompatActivity {

    private static EditText etValorEntrada, etDataEntrada, etCategoriaEntrada, etDescricaoEntrada;
    private GerenciaEntrada gerenciaEntrada;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etValorEntrada = findViewById(R.id.etValorEntrada);
        etDataEntrada = findViewById(R.id.etDataEntrada);
        etCategoriaEntrada = findViewById(R.id.etCategoriaEntrada);
        etDescricaoEntrada = findViewById(R.id.etDescricaoEntrada);

        FloatingActionButton fab = findViewById(R.id.btNovaEntrada);

    }

    public void salvaEntrada(View view){
        gerenciaEntrada = new GerenciaEntrada(EntradaActivity.this);

        try{
            movimentacao = new Movimentacao();
            movimentacao.setValor(Double.parseDouble(etValorEntrada.getText().toString()));
            movimentacao.setData(etDataEntrada.getText().toString());
            movimentacao.setCategoria(etCategoriaEntrada.getText().toString());
            movimentacao.setDescricao(etDescricaoEntrada.getText().toString());
            movimentacao.setTipo(TipoEnum.E.getTipo());
            gerenciaEntrada.salvaEntrada(movimentacao);
        } catch (NumberFormatException | NullPointerException e){
            Toast.makeText(this, "Digite valores válidos", Toast.LENGTH_LONG).show();
        }
    }

    public static void limpaCampos(){
        etValorEntrada.setText("");
        etCategoriaEntrada.setText("");
        etDescricaoEntrada.setText("");
    }
}