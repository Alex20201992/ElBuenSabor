package com.example.elbuensabor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elbuensabor.MainActivity;
import com.example.elbuensabor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsuario, edtPassword;
    Button btnLogin, btnRegistrar,btnRegistrate;
    TextView tvRecuperarPassword;
    String URL = "http://192.168.1.96:8080/apppedido/api/usuario/login";
    String URL_EMAIL="http://192.168.1.96:8080/apppedido/api/usuario/restablecepassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsuario=findViewById(R.id.edtUsuario);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        tvRecuperarPassword=findViewById(R.id.tvRecuperarPassword);

        recuperarPreferencias();
        btnRegistrate=findViewById(R.id.btnRegistrate);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), RegistroActivity.class);
                startActivityForResult(intent, 0);
                //alertView();
                //recuperarPassword();
            }
        });

        tvRecuperarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRecuperarPassword();
            }
        });
    }

    public void iniciarSesion() {
        final ProgressDialog loading = ProgressDialog.show(this, "Iniciando Sesión..", "Espere por favor");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", edtUsuario.getText().toString());
            postData.put("password", edtPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL,postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    //JSONObject jsonObject=response.getJSONObject("success");
                    //Toast.makeText(LoginActivity.this, jsonObject.getString("nomrol"), Toast.LENGTH_SHORT).show();
                    //JSONObject jsonObject=response.getJSONObject("data");
                    if(response.getBoolean("success")){
                        JSONArray array=response.getJSONArray("data");
                        JSONObject jsonObject=array.getJSONObject(0);
                        String idusuario=jsonObject.getString("idusuario");
                        String nomrol=jsonObject.getString("nomrol");
                        guardarPreferencias(edtUsuario.getText().toString(),edtPassword.getText().toString(),idusuario,nomrol);

                        /*SharedPreferences preferences=getSharedPreferences("Usuario",Context.MODE_PRIVATE);
                        String id= preferences.getString("idusuario","");
                        Toast.makeText(LoginActivity.this, id, Toast.LENGTH_SHORT).show();*/
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Usuario y/o Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*try {
                    Toast.makeText(getApplication(), response.getString("text"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplication(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }


    private void alertView( ) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this);

        // set title
        alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void guardarPreferencias(String email,String password,String iduser,String nomrol){
        SharedPreferences preferences=this.getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.putString("idusuario",iduser);
        editor.putString("nomrol",nomrol);
        editor.commit();
    }

    private void recuperarPreferencias(){
        SharedPreferences preferences=this.getSharedPreferences("Usuario",Context.MODE_PRIVATE);
        edtUsuario.setText(preferences.getString("email","ejemplo@gmail.com"));
        edtPassword.setText(preferences.getString("password","123456"));
    }

    public void dialogRecuperarPassword(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("OLVIDÓ SU CONTRASEÑA");
        alert.setMessage("Envienos su direccion Email y luego revise su bandeja de entrada para su recuperación.");

        final EditText input = new EditText(this);
        input.setGravity(Gravity.CENTER);
        alert.setView(input);

        alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = input.getText().toString();
                //Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
                recuperarPassword(email);
            }
        });

        alert.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

    public void recuperarPassword(String email) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //alert.setTitle("CONTRASEÑA ENVIADA CORRECTAMENTE");
        //alert.setMessage("Revise su bandeja de entrada");

        final ProgressDialog loading = ProgressDialog.show(this, "RECUPERANDO PASSWORD...", "Espere un momento por favor");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL_EMAIL,postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(LoginActivity.this, "Operación Exitosa revise su bansdeja de entrada", Toast.LENGTH_SHORT).show();
                    }else{
                        alert.setMessage(response.getString("text"));
                        alert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplication(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}