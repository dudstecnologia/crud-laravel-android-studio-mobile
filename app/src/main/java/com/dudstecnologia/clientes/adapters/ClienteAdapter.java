package com.dudstecnologia.clientes.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dudstecnologia.clientes.MainFormulario;
import com.dudstecnologia.clientes.R;
import com.dudstecnologia.clientes.Util;
import com.dudstecnologia.clientes.models.Cliente;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.MeuViewHolder> {

    Context ctx;
    List<Cliente> lista;

    Util util = new Util();

    public class MeuViewHolder extends RecyclerView.ViewHolder {

        TextView txtNome;
        ImageButton btnEditar, btnExcluir;

        public MeuViewHolder(@NonNull View view) {
            super(view);

            txtNome = (TextView) view.findViewById(R.id.txtNome);
            btnEditar = (ImageButton) view.findViewById(R.id.btnEditar);
            btnExcluir = (ImageButton) view.findViewById(R.id.btnExcluir);
        }
    }

    public ClienteAdapter(Context ctx1, List<Cliente> lista1) {
        this.ctx = ctx1;
        this.lista = lista1;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cliente, viewGroup, false);

        return new ClienteAdapter.MeuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder meuViewHolder, final int i) {

        final Cliente c = lista.get(i);

        meuViewHolder.txtNome.setText(c.getNome());

        meuViewHolder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent abreForm = new Intent(ctx, MainFormulario.class);
                abreForm.putExtra("id", c.getId());
                ctx.startActivity(abreForm);

            }
        });

        meuViewHolder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertaExcluir(c.getNome(), String.valueOf(c.getId()), i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    private void alertaExcluir(String nome, final String id, final int posicao) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setTitle("Atenção");
        builder.setMessage("Tem certeza que deseja excluir o cliente " + nome);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                excluirCliente(id, posicao);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void excluirCliente(String id, final int posicao) {

        String URL = util.getHost() + "/clientes/" + id;

        Ion.with(ctx)
                .load("DELETE", URL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {

                            String retorno = result.get("delete").getAsString();

                            if(retorno.equals("ok")) {

                                Toast.makeText(ctx, "Excluido com sucesso", Toast.LENGTH_SHORT).show();

                                lista.remove(posicao);
                                notifyDataSetChanged();

                            } else {
                                Toast.makeText(ctx, "Erro ao excluir", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception erro) {

                            Toast.makeText(ctx, "Ops! Erro ao excluir", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
