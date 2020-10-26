package br.com.cauezito.app.organizze.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.firebase.movimentacao.entrada.GerenciaEntrada;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.model.TipoEnum;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class EntradaActivity extends AppCompatActivity {

    private static EditText etValorEntrada, etDataEntrada, etDescricaoEntrada;
    private GerenciaEntrada gerenciaEntrada;
    private Movimentacao movimentacao;
    private Spinner spCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etValorEntrada = findViewById(R.id.etValorEntrada);
        etDataEntrada = findViewById(R.id.etDataEntrada);
        etDescricaoEntrada = findViewById(R.id.etDescricaoEntrada);
        spCategoria = findViewById(R.id.spCategoria);

        etDataEntrada.setText(DateCustom.dataAtual());

        configuraCategorias();
    }

    private void configuraCategorias() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_categorias, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);
    }

    public void salvaEntrada(View view){
        gerenciaEntrada = new GerenciaEntrada(EntradaActivity.this);

        try{
            movimentacao = new Movimentacao();
            movimentacao.setValor(Double.parseDouble(etValorEntrada.getText().toString()));
            movimentacao.setData(etDataEntrada.getText().toString());
            movimentacao.setCategoria(spCategoria.getSelectedItem().toString());
            movimentacao.setDescricao(etDescricaoEntrada.getText().toString());
            movimentacao.setTipo(TipoEnum.E.getTipo());
            gerenciaEntrada.salvaEntrada(movimentacao);
        } catch (NumberFormatException | NullPointerException e){
            Toast.makeText(this, "Digite valores válidos", Toast.LENGTH_LONG).show();
        }

        this.finish();
    }
}