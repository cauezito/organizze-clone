package br.com.cauezito.app.organizze.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.cauezito.app.R;
import br.com.cauezito.app.organizze.adapter.AdapterMovimentacao;
import br.com.cauezito.app.organizze.config.Preferencias;
import br.com.cauezito.app.organizze.firebase.config.FirebaseConfig;
import br.com.cauezito.app.organizze.firebase.movimentacao.despesa.GerenciaDespesa;
import br.com.cauezito.app.organizze.firebase.movimentacao.entrada.GerenciaEntrada;
import br.com.cauezito.app.organizze.firebase.usuario.FirebaseAuthUsuario;
import br.com.cauezito.app.organizze.model.Movimentacao;
import br.com.cauezito.app.organizze.model.TipoEnum;
import br.com.cauezito.app.organizze.model.Usuario;
import br.com.cauezito.app.organizze.utils.Base64Custom;

public class HomeActivity extends AppCompatActivity {

    private MaterialCalendarView calendario;
    private FirebaseAuthUsuario firebaseAuthUsuario;
    private FirebaseAuth autenticacao;
    private TextView tvNomeUsuario, tvSaldo;
    private ImageView ivFiltro;
    private Spinner spFiltro;
    private RecyclerView recyclerView;
    private Usuario usuario = new Usuario();

    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacao;

    private DatabaseReference usuarioRef;
    private DatabaseReference movimentacaoRef;
    private DatabaseReference preferenciasRef;

    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;

    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        firebaseAuthUsuario = new FirebaseAuthUsuario(this);
        autenticacao = FirebaseConfig.getFirebaseAutenticacao();

        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        tvSaldo = findViewById(R.id.tvSaldo);
        calendario = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);
        ivFiltro = findViewById(R.id.ivFiltro);
        spFiltro = findViewById(R.id.spFiltro);

        spFiltro.setVisibility(View.GONE);

        ivFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spFiltro.setVisibility(View.VISIBLE);
            }
        });

        configuraRecyclerView();
        configuraCalendario();
        manipulaCalendario();
        recuperaPreferencias();
        swipe();
    }

    private void recuperaPreferencias() {
        preferenciasRef = FirebaseConfig.getDatabaseReference().child("config").child("preferencias").
                child("fecharAposNovaMovimentacao");

        preferenciasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Preferencias.fechaActivityAposNovaMovimentacao = Boolean.parseBoolean(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        preencheInfoResumo();
        recuperaMovimentacoes();
    }

    private void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    private void excluirMovimentacao(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Você quer excluir esta movimentação?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //exclusão
                int posicao = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(posicao);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String id = Base64Custom.codificaBase64(emailUsuario);
                movimentacaoRef = FirebaseConfig.getDatabaseReference().child("movimentacao").child(id).child(mesAnoSelecionado);
                movimentacaoRef.child(movimentacao.getId()).removeValue();
                adapterMovimentacao.notifyItemRemoved(posicao);

                DatabaseReference firebaseRef = FirebaseConfig.getDatabaseReference();
                usuarioRef = firebaseRef.child("usuarios").child(id);

                alteraSaldo(movimentacao.getTipo() , movimentacao.getValor());

                Toast.makeText(HomeActivity.this, "Movimentação excluída", Toast.LENGTH_SHORT).show();

            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void alteraSaldo(String tipo, Double valor){
        Log.i("tipo", TipoEnum.D.getTipo());
        if(tipo.equals(TipoEnum.D.getTipo())){
            Double despesaTotal = GerenciaDespesa.alteraDespesaTotal(valor);
            usuarioRef.child("despesaTotal").setValue(despesaTotal);

        } else if (tipo.equals(TipoEnum.E.getTipo())){
            Double receitaTotal = GerenciaEntrada.alteraReceitaTotal(valor);
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }
    }



    private void recuperaMovimentacoes(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String id = Base64Custom.codificaBase64(emailUsuario);
        movimentacaoRef = FirebaseConfig.getDatabaseReference().child("movimentacao").child(id).child(mesAnoSelecionado);

        valueEventListenerMovimentacao = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movimentacoes.clear();

                //percorre todas as movimentações
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setId(dados.getKey());
                    movimentacoes.add(movimentacao);
                }

                //notifica que os dados foram atualizados
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
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
            case R.id.menuConfig:
                startActivity(new Intent(this, ConfigActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configuraRecyclerView(){
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    private void configuraCalendario(){
        calendario.state().edit().setMinimumDate(CalendarDay.from(2020, 0, 1))
                .setMaximumDate(CalendarDay.from(2020, 11, 31)).commit();
    }

    private void manipulaCalendario(){
        CalendarDay dataAtual = calendario.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1));
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        calendario.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth() + 1));
                mesAnoSelecionado = String.valueOf((date.getMonth() + 1) + "" + date.getYear());

                movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
                recuperaMovimentacoes();
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
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
    }
}