package br.com.cauezito.app.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.config.Preferencias;
import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;

public class ConfigActivity extends AppCompatActivity {

    private SwitchCompat swFecharActv;
    private Button btSalvar;

    private DatabaseReference banco;
    private Boolean fecharActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        swFecharActv = findViewById(R.id.swFecharActv);
        btSalvar = findViewById(R.id.btSalvar);

        swFecharActv.setChecked(Preferencias.fechaActivityAposNovaMovimentacao);

        banco = FirebaseConfig.getDatabaseReference().child("config").child("preferencias").
                child("fecharAposNovaMovimentacao");

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swFecharActv.isChecked() != Preferencias.fechaActivityAposNovaMovimentacao) {
                    banco.setValue(swFecharActv.isChecked());
                    Preferencias.fechaActivityAposNovaMovimentacao = swFecharActv.isChecked();
                }
            }
        });
    }
}