package br.com.cauezito.app.organizze.activity;


import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.adapter.AdapterMovimentacao;
import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.firebase.usuario.FirebaseAuthUsuario;
import br.com.cauezito.app.organizze.firebase.usuario.FirebaseDatabaseUsuario;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.model.Usuario;
import br.com.cauezito.app.organizze.utils.Base64Custom;

public class HomeActivity extends AppCompatActivity {

    private MaterialCalendarView calendario;
    private FirebaseAuthUsuario firebaseAuthUsuario;
    private FirebaseDatabaseUsuario firebaseDatabaseUsuario;
    private FirebaseAuth autenticacao;
    private TextView tvNomeUsuario, tvSaldo;
    private RecyclerView recyclerView;
    private Usuario usuario = new Usuario();
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference usuarioRef;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        firebaseAuthUsuario = new FirebaseAuthUsuario(this);
        firebaseDatabaseUsuario = new FirebaseDatabaseUsuario();
        autenticacao = FirebaseConfig.getFirebaseAutenticacao();

        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        tvSaldo = findViewById(R.id.tvSaldo);
        calendario = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);

        configuraRecyclerView();
        configuraCalendario();
        manipulaCalendario();

    }

    @Override
    protected void onStart() {
        super.onStart();
        preencheInfoResumo();
    }

    private void preencheInfoResumo(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String id = Base64Custom.codificaBase64(emailUsuario);
        DatabaseReference firebaseRef = FirebaseConfig.getDatabaseReference();

        usuarioRef = firebaseRef.child("usuarios").child(id);

        tvNomeUsuario.setText("Carregando...");
        tvSaldo.setText("R$0.00");

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                Double resumo = usuario.getReceitaTotal() - usuario.getDespesaTotal();

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String saldoTotal = decimalFormat.format(resumo);

                tvNomeUsuario.setText("Olá, " + usuario.getNome() + "!");
                tvSaldo.setText("R$" + saldoTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


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

    private void configuraRecyclerView(){
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
    }
}