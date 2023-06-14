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

public class RegisterActivity extends AppCompatActivity {

    EditText txtName,txtEmail,txtPass,txtPass2;
    LinearLayout fondoCarga;
    ProgressBar progressBarCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmail = findViewById(R.id.txtEmail);
        txtName = findViewById(R.id.txtName);
        txtPass = findViewById(R.id.txtPass);
        txtPass2 = findViewById(R.id.txtPass2);

        fondoCarga = findViewById(R.id.fondoCarga);
        progressBarCarga = findViewById(R.id.progressBarCarga);

        fondoCarga.setVisibility(View.GONE);
        progressBarCarga.setVisibility(View.GONE);
    }

    public void goToLogin(View view) {
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void register(View view) {
        if(validar()){
            registerAPI();
        }else{
            Toast.makeText(getApplicationContext(),"Debe ingresar los valores",Toast.LENGTH_LONG).show();
        }
    }

    private void registerAPI() {
        fondoCarga.setVisibility(View.VISIBLE);
        progressBarCarga.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://wsa.fabricasoftware.co/api/register";
        JSONObject datos = new JSONObject();
        try{
            datos.put("name",txtName.getText().toString().trim().toUpperCase());
            datos.put("email",txtEmail.getText().toString().trim().toLowerCase());
            datos.put("password",txtPass.getText().toString().trim());
            datos.put("password_confirmation",txtPass2.getText().toString().trim());

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
                        Toast.makeText(getApplicationContext(),"Ya existe un usuario con email "+txtEmail.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    fondoCarga.setVisibility(View.GONE);
                    progressBarCarga.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"ERROR VOLLEY :"+error.getMessage(),Toast.LENGTH_LONG).show();
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
        if(txtName.getText().toString().trim().isEmpty()){
            txtName.setError("Debe ingresar un nombre");
            valido = false;
        }
        if(txtEmail.getText().toString().trim().isEmpty()){
            txtEmail.setError("Debe ingresar un correo");
            valido = false;
        }
        if(txtPass.getText().toString().trim().isEmpty()){
            txtPass.setError("Debe ingresar una contraseña");
            valido = false;
        }
        if(txtPass2.getText().toString().trim().isEmpty()){
            txtPass2.setError("Debe ingresar una contraseña");
            valido = false;
        }
        if(txtPass.getText().toString().trim().length()<6 || txtPass2.getText().toString().trim().length()<6){
            txtPass2.setError("Contraseña muy corta (Min. 6 caracteres)");
            txtPass.getText().clear();
            txtPass2.getText().clear();
            txtPass.requestFocus();
            valido = false;
        }
        if(!txtPass.getText().toString().trim().equals(txtPass2.getText().toString().trim())){
            txtPass.getText().clear();
            txtPass2.getText().clear();
            txtPass.setError("Contraseñas no coinciden");
            txtPass2.setError("Contraseñas no coinciden");

            txtPass.requestFocus();
            valido = false;
        }
        return valido;
    }
}