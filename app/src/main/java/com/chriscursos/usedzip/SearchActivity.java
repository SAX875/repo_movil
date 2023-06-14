package com.chriscursos.usedzip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Notificaction;
import model.Product;

public class SearchActivity extends AppCompatActivity {
    TextView txtName;
    EditText txtSearch;
    RecyclerView rvLista;
    ProgressBar carga;
    LinearLayout fondoCarga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txtName=findViewById(R.id.txtName);
        txtSearch=findViewById(R.id.txtSearch);
        carga = findViewById(R.id.progressBarCarga);
        fondoCarga = findViewById(R.id.fondoCarga);
        carga.setVisibility(View.GONE);
        fondoCarga.setVisibility(View.GONE);
        rvLista = findViewById(R.id.rvProducts);

        SharedPreferences pref=getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String name= pref.getString("name","");
        txtName.setText(name);
        txtSearch.requestFocus();

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()>2){
                    buscar(charSequence.toString().trim());
                }else {
                    Toast.makeText(getApplicationContext(),"Debe ingresar 3 letras para buscar",Toast.LENGTH_LONG).show();
                    productArrayList=new ArrayList<>();
                    ProductAdapter adapter = new ProductAdapter(productArrayList);

                    rvLista.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    ArrayList<Product> productArrayList = new ArrayList<>();
    private void buscar(String texto) {

        fondoCarga.setVisibility(View.VISIBLE);
        carga.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://wsa.fabricasoftware.co/api/productos?search="+texto;


        //INICIA LA SOLICITUD AL SERVER
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    productArrayList=new ArrayList<>();
                    JSONArray data = response.getJSONArray("data");
                    for (int i=0;i<data.length();i++){
                        JSONObject item = data.getJSONObject(i);

                        Product p=new Product();
                        p.setId(item.getLong("id"));
                        p.setNombre(item.getString("nombre"));
                        p.setDescripcion(item.getString("descripcion"));
                        p.setPrecio(item.getLong("precio"));

                        productArrayList.add(p);
                    }
                    rvLista.setHasFixedSize(true);
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                    ProductAdapter adapter = new ProductAdapter(productArrayList);
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
                    productArrayList=new ArrayList<>();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productArrayList=new ArrayList<>();
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
        Intent i=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(i);
        finish();
    }

}