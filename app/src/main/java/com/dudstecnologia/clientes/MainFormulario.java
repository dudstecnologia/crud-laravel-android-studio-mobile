package com.dudstecnologia.clientes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainFormulario extends AppCompatActivity {

    EditText editId, editNome, editTelefone, editIdade, editEmail;
    Button btnSalvar;

    Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_formulario);

        editId = (EditText) findViewById(R.id.editId);
        editNome = (EditText) findViewById(R.id.editNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        editIdade = (EditText) findViewById(R.id.editIdade);
        editEmail = (EditText) findViewById(R.id.editEmail);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        Bundle extras = getIntent().getExtras();

        try {
            int id = extras.getInt("id");
            //Toast.makeText(this, "Recebeu: " + id, Toast.LENGTH_LONG).show();
            getCliente(id);
            
        } catch (Exception erro) {

        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = editId.getText().toString();
                String nome = editNome.getText().toString();
                String telefone = editTelefone.getText().toString();
                String idade = editIdade.getText().toString();
                String email = editEmail.getText().toString();

                if (nome.isEmpty() || telefone.isEmpty() || idade.isEmpty() || email.isEmpty()) {
                    //editNome.setError("Obrigatório");
                    Toast.makeText(MainFormulario.this,
                            "Todos os campos são obrigatórios",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(id.isEmpty()) {

                    salvarCliente(nome, telefone, idade, email);
                } else {
                    atualizarCliente(id, nome, telefone, idade, email);
                }
            }
        });
    }

    private void limparCampos() {
        editNome.setText("");
        editTelefone.setText("");
        editIdade.setText("");
        editEmail.setText("");

        editNome.requestFocus();
    }

    private void salvarCliente(String nome, String telefone, String idade, String email) {

        //String URL = "http://192.168.0.101/clientes/public/api/clientes";

        String URL = util.getHost() + "/clientes";

        Ion.with(MainFormulario.this)
                .load("POST", URL)
                .setBodyParameter("nome_app", nome)
                .setBodyParameter("telefone_app", telefone)
                .setBodyParameter("idade_app", idade)
                .setBodyParameter("email_app", email)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {

                            String retorno = result.get("insert").getAsString();

                            if(retorno.equals("ok")) {
                                limparCampos();
                                Toast.makeText(MainFormulario.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainFormulario.this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception erro) {

                            Toast.makeText(MainFormulario.this, "Ops! Erro ao Salvar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void atualizarCliente(String id, String nome, String telefone, String idade, String email) {

        String URL = util.getHost() + "/clientes/" + id;

        Ion.with(MainFormulario.this)
                .load("PUT", URL)
                .setBodyParameter("nome_app", nome)
                .setBodyParameter("telefone_app", telefone)
                .setBodyParameter("idade_app", idade)
                .setBodyParameter("email_app", email)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {

                            String retorno = result.get("update").getAsString();

                            if(retorno.equals("ok")) {
                                limparCampos();
                                Toast.makeText(MainFormulario.this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainFormulario.this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception erro) {

                            Toast.makeText(MainFormulario.this, "Ops! Erro ao atualizar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getCliente(int id) {

        //String URL = "http://192.168.0.101/clientes/public/api/clientes/" + id;

        String URL = util.getHost() + "/clientes/" + id;

        Ion.with(MainFormulario.this)
                .load(URL)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {

                            String id = result.get("id").getAsString();
                            String nome = result.get("nome").getAsString();
                            String telefone = result.get("telefone").getAsString();
                            String idade = result.get("idade").getAsString();
                            String email = result.get("email").getAsString();

                            editId.setText(id);
                            editNome.setText(nome);
                            editTelefone.setText(telefone);
                            editIdade.setText(idade);
                            editEmail.setText(email);

                        } catch (Exception erro) {

                            Toast.makeText(MainFormulario.this, "Ops! Erro ao buscar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
