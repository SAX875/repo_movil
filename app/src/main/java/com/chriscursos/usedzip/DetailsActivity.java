package com.chriscursos.usedzip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Product;

public class DetailsActivity extends AppCompatActivity {
    TextView txtNombre,txtTitulo,txtPrecio,txtDescripcion,txtCategoria;
    ImageView ivImagen,ivImagen2;

    LinearLayout fondoCarga;
    ProgressBar carga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destails);

        txtNombre=findViewById(R.id.txtNombre);
        txtTitulo=findViewById(R.id.txtTitulo);
        txtPrecio=findViewById(R.id.txtPrecio);
        txtDescripcion=findViewById(R.id.txtDescripcion);
        txtCategoria=findViewById(R.id.txtCategoria);
        ivImagen=findViewById(R.id.ivImagen);

        fondoCarga = findViewById(R.id.fondoCarga);
        carga = findViewById(R.id.progressBarCarga);

        long id = getIntent().getExtras().getLong("id",0);
        getDetalle(id);
    }

    public void goToHome(View v){
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDetalle(long id) {

         fondoCarga.setVisibility(View.VISIBLE);
        carga.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://wsa.fabricasoftware.co/api/productos/"+id;


        //INICIA LA SOLICITUD AL SERVER
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject item) {
                try {

                        Product p=new Product();
                        p.setId(item.getLong("id"));
                        p.setNombre(item.getString("title"));
                        p.setDescripcion(item.getString("description"));
                        p.setPrecio(item.getLong("price"));
                        p.setImagen(item.getString("thumbnail"));
                        p.setCategoria(item.getString("category"));

                        txtDescripcion.setText(p.getDescripcion());
                        txtTitulo.setText(p.getNombre());
                        txtNombre.setText(p.getNombre());
                        txtPrecio.setText(String.valueOf(p.getPrecio()));
                        txtCategoria.setText(p.getCategoria());

                    Glide.with(getApplicationContext()).load(p.getImagen()).into(ivImagen);
                    fondoCarga.setVisibility(View.GONE);
                    carga.setVisibility(View.GONE);
                } catch (JSONException ex) {

                        Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    goToHome(null);
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
}