package br.com.cauezito.app.organizze.activity;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.firebase.usuario.FirebaseAuthUsuario;
import br.com.cauezito.app.organizze.model.Usuario;

public class HomeActivity extends AppCompatActivity {

    private MaterialCalendarView calendario;
    private FirebaseAuthUsuario firebaseAuthUsuario;
    private TextView tvNomeUsuario, tvSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        firebaseAuthUsuario = new FirebaseAuthUsuario(this);

        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        tvSaldo = findViewById(R.id.tvSaldo);
        calendario = findViewById(R.id.calendarView);

        configuraCalendario();
        manipulaCalendario();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                firebaseAuthUsuario.deslogaUsuario();
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configuraCalendario(){
        calendario.state().edit().setMinimumDate(CalendarDay.from(2020, 0, 1))
                .setMaximumDate(CalendarDay.from(2020, 11, 31)).commit();
    }

    private void manipulaCalendario(){
        calendario.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });
    }

    public void adicionaDespesa(View view){
        startActivity(new Intent(this, DespesaActivity.class));
    }

    public void adicionaEntrada(View view){
        startActivity(new Intent(this, EntradaActivity.class));
    }


}