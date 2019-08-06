package com.dudstecnologia.clientes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dudstecnologia.clientes.adapters.ClienteAdapter;
import com.dudstecnologia.clientes.models.Cliente;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainClientes extends AppCompatActivity {

    Button btnNovo;
    RecyclerView recyclerViewClientes;

    ClienteAdapter clienteAdapter;
    List<Cliente> listaClientes;

    Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_clientes);

        btnNovo = (Button) findViewById(R.id.btnNovo);
        recyclerViewClientes = (RecyclerView) findViewById(R.id.recyclerViewClientes);

        listaClientes = new ArrayList<>();
        clienteAdapter = new ClienteAdapter(MainClientes.this, listaClientes);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainClientes.this);
        recyclerViewClientes.setLayoutManager(layoutManager);
        recyclerViewClientes.addItemDecoration(new DividerItemDecoration(MainClientes.this, LinearLayoutManager.VERTICAL));
        recyclerViewClientes.setAdapter(clienteAdapter);

        listarClientes();

        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent abreForm = new Intent(MainClientes.this, MainFormulario.class);
                startActivity(abreForm);
            }
        });
    }

    private void listarClientes() {

        //String URL = "http://192.168.0.101/clientes/public/api/clientes";

        String URL = util.getHost() + "/clientes";

        Ion.with(MainClientes.this)
                .load(URL)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        try {

                            for(int i = 0; i < result.size(); i++) {

                                JsonObject cli = result.get(i).getAsJsonObject();
                                //Log.d("DCliente", cli.get("nome").getAsString());

                                Cliente c = new Cliente();

                                c.setId(cli.get("id").getAsInt());
                                c.setNome(cli.get("nome").getAsString());
                                c.setTelefone(cli.get("telefone").getAsString());
                                c.setIdade(cli.get("idade").getAsInt());
                                c.setEmail(cli.get("email").getAsString());

                                listaClientes.add(c);

                            }

                            clienteAdapter.notifyDataSetChanged();

                        } catch (Exception erro) {

                            Toast.makeText(MainClientes.this, "Ops! Erro ao listar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
