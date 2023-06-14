package com.chriscursos.usedzip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText txtEmail,txtPass;
    LinearLayout fondoCarga;
    ProgressBar progressBarCarga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verificarSesion();

        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);

        fondoCarga = findViewById(R.id.fondoCarga);
        progressBarCarga = findViewById(R.id.progressBarCarga);

        fondoCarga.setVisibility(View.GONE);
        progressBarCarga.setVisibility(View.GONE);
    }

    public void login(View view) {
        if(validar()){
            loginAPI();
        }else{
            Toast.makeText(getApplicationContext(),"Debe ingresar los valores",Toast.LENGTH_LONG).show();
        }
    }

    private void loginAPI() {
        fondoCarga.setVisibility(View.VISIBLE);
        progressBarCarga.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://wsa.fabricasoftware.co/api/login";
        JSONObject datos = new JSONObject();
        try{
            datos.put("email",txtEmail.getText().toString().trim().toLowerCase());
            datos.put("password",txtPass.getText().toString().trim());

            //INICIA LA SOLICITUD AL SERVER
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, datos, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject user = response.getJSONObject("user");
                        String token = response.getString("token");

                        //guardar en preferencias.
                        SharedPreferences pref = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token",token);
                        editor.putString("name",user.getString("name"));
                        editor.putString("email",user.getString("email"));
                        editor.commit();
                        fondoCarga.setVisibility(View.GONE);
                        progressBarCarga.setVisibility(View.GONE);

                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        fondoCarga.setVisibility(View.GONE);
                        progressBarCarga.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    fondoCarga.setVisibility(View.GONE);
                    progressBarCarga.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Datos de acceso incorrectos",Toast.LENGTH_LONG).show();
                }
            });
            queue.add(request);

        }catch (JSONException ex){
            fondoCarga.setVisibility(View.GONE);
            progressBarCarga.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"JSON ERROR:"+ex.getMessage(),Toast.LENGTH_LONG).show();
        }


    }

    private boolean validar() {
        boolean valido = true;
        if(txtEmail.getText().toString().trim().isEmpty()){
            txtEmail.setError("Debe ingresar un correo");
            valido = false;
        }
        if(txtPass.getText().toString().trim().isEmpty()){
            txtPass.setError("Debe ingresar una contraseña");
            valido = false;
        }
        return valido;
    }

    private void verificarSesion() {
        //guardar en preferencias.
        SharedPreferences pref = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String token = pref.getString("token", "");
        String name = pref.getString("name", "");

        if(!token.isEmpty() && !name.isEmpty()){
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goToRegister(View view) {
        Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToReset(View view) {
        Intent intent=new Intent(getApplicationContext(),PasswordResetActivity.class);
        startActivity(intent);
        finish();
    }


}