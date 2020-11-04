package br.com.cauezito.app.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.config.Preferencias;
import br.com.cauezito.app.organizze.firebase.movimentacao.despesa.GerenciaDespesa;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.model.TipoEnum;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class DespesaActivity extends AppCompatActivity{

    private static EditText etValorDespesa, etData, etDescricaoDespesa;
    private Movimentacao movimentacao;
    private GerenciaDespesa gerenciaDespesa;
    private Spinner spCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        etValorDespesa = findViewById(R.id.etValorDespesa);
        etData = findViewById(R.id.etValorData);
        etDescricaoDespesa = findViewById(R.id.etDescricaoDespesa);
        spCategoria = findViewById(R.id.spCategoria);
        etData.setText(DateCustom.dataAtual());

        configuraCategorias();
    }

    private void configuraCategorias() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_categorias, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);
    }

    public void salvaDespesa(View view){
        gerenciaDespesa = new GerenciaDespesa(DespesaActivity.this);

        try {
            movimentacao = new Movimentacao();
            movimentacao.setValor(Double.parseDouble(etValorDespesa.getText().toString()));
            movimentacao.setCategoria(spCategoria.getSelectedItem().toString());
            movimentacao.setDescricao(etDescricaoDespesa.getText().toString());
            movimentacao.setData(etData.getText().toString());
            movimentacao.setTipo(TipoEnum.D.getTipo());
            gerenciaDespesa.salvaDespesa(movimentacao);

            if(Preferencias.fechaActivityAposNovaMovimentacao){
                this.finish();
            } else {
                limpaCampos();
            }
        } catch (NumberFormatException | NullPointerException e){
            Toast.makeText(this, "Digite valores válidos", Toast.LENGTH_LONG).show();
        }
    }

    private void limpaCampos() {
        etValorDespesa.setText("");
        etDescricaoDespesa.setText("");
    }

}