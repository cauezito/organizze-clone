package br.com.cauezito.app.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.utils.DateCustom;

public class DespesaActivity extends AppCompatActivity {

    EditText valorDespesa, etData, etCategoriaDespesa, etDescricaoDespesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        valorDespesa = findViewById(R.id.etValorDespesa);
        etData = findViewById(R.id.etValorData);
        etCategoriaDespesa = findViewById(R.id.etCategoriaDespesa);
        etDescricaoDespesa = findViewById(R.id.etDescricaoDespesa);

        etData.setText(DateCustom.dataAtual());

    }
}