package com.chriscursos.usedzip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Notificaction;

public class NotificacionActivity extends AppCompatActivity {
    RecyclerView rvLista;
    ProgressBar carga;
    LinearLayout fondoCarga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);

        rvLista = findViewById(R.id.rvListaMensajes);
        carga = findViewById(R.id.progressBarCarga);
        fondoCarga = findViewById(R.id.fondoCarga);
        carga.setVisibility(View.GONE);
        fondoCarga.setVisibility(View.GONE);

        listarMensajes();
    }



    private void listarMensajes() {
        ArrayList<Notificaction> notificactionArrayList = new ArrayList<>();
        fondoCarga.setVisibility(View.VISIBLE);
        carga.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://wsa.fabricasoftware.co/api/notificaciones";


        //INICIA LA SOLICITUD AL SERVER
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                        JSONObject data = response.getJSONObject("data");
                        JSONArray notificaciones = data.getJSONArray("notifications");
                        for (int i=0;i<notificaciones.length();i++){
                            JSONObject item = notificaciones.getJSONObject(i);

                            Notificaction noti=new Notificaction();
                            noti.setMessage(item.getString("body"));
                            noti.setUser("@"+item.getJSONObject("user").getString("username"));
                            notificactionArrayList.add(noti);
                        }
                        rvLista.setHasFixedSize(true);
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        MessagesAdapter adapter = new MessagesAdapter(notificactionArrayList);
                        rvLista.setLayoutManager(manager);
                        rvLista.setAdapter(adapter);

                    rvLista.setHasFixedSize(true);


                        fondoCarga.setVisibility(View.GONE);
                        carga.setVisibility(View.GONE);

                } catch (JSONException e) {
                    try {
                        String status = response.getString("status");
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                        SharedPreferences preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                        preferences.edit().clear().commit();
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException ex) {
                        Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                    fondoCarga.setVisibility(View.GONE);
                    carga.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fondoCarga.setVisibility(View.GONE);
                carga.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"error 2 "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                SharedPreferences pref = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                String token = pref.getString("token", "");
                headers.put("Authorization","Bearer "+token);
                headers.put("Content-Type","application/json");
                return headers;
            }
        };
        queue.add(request);


    }

    public void goToHome(View v){
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }
}