package br.com.cauezito.app.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.firebase.movimentacao.despesa.GerenciaDespesa;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.model.TipoEnum;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class DespesaActivity extends AppCompatActivity {

    private static EditText etValorDespesa, etData, etCategoriaDespesa, etDescricaoDespesa;
    private Movimentacao movimentacao;
    private GerenciaDespesa gerenciaDespesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        etValorDespesa = findViewById(R.id.etValorDespesa);
        etData = findViewById(R.id.etValorData);
        etCategoriaDespesa = findViewById(R.id.etCategoriaDespesa);
        etDescricaoDespesa = findViewById(R.id.etDescricaoDespesa);

        etData.setText(DateCustom.dataAtual());

    }

    public void salvaDespesa(View view){
        gerenciaDespesa = new GerenciaDespesa(DespesaActivity.this);

        try {
            movimentacao = new Movimentacao();
            movimentacao.setValor(Double.parseDouble(etValorDespesa.getText().toString()));
            movimentacao.setCategoria(etCategoriaDespesa.getText().toString());
            movimentacao.setDescricao(etDescricaoDespesa.getText().toString());
            movimentacao.setData(etData.getText().toString());
            movimentacao.setTipo(TipoEnum.D.getTipo());

            gerenciaDespesa.salvaDespesa(movimentacao);
        } catch (NumberFormatException | NullPointerException e){
            Toast.makeText(this, "Digite valores válidos", Toast.LENGTH_LONG).show();
        }

    }

    public static void limpaCampos(){
        etValorDespesa.setText("");
        etCategoriaDespesa.setText("");
        etDescricaoDespesa.setText("");
    }

}