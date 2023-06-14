package com.chriscursos.usedzip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import model.Product;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvLastest, rvProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvLastest=findViewById(R.id.rvLastest);
        rvProducts=findViewById(R.id.rvProducts);

        listaLastest();
        listaProducts();
    }

    public void goToNotifications(View view) {
        Intent intent= new Intent(getApplicationContext(),NotificacionActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSeacrh(View view) {
        Intent intent= new Intent(getApplicationContext(),SearchActivity.class);
        startActivity(intent);
        finish();
    }


    ArrayList<Product> productArrayList = new ArrayList<>();
    private void listaLastest() {

       // fondoCarga.setVisibility(View.VISIBLE);
        //carga.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://wsa.fabricasoftware.co/api/productos?lastest";


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
                        p.setMoneda(item.getString("moneda"));

                        productArrayList.add(p);
                    }
                    rvLastest.setHasFixedSize(true);
                    LinearLayoutManager manager = new GridLayoutManager(getApplicationContext(),1,GridLayoutManager.HORIZONTAL,false);
                    ProductLastestAdapter adapter = new ProductLastestAdapter(productArrayList);
                    rvLastest.setLayoutManager(manager);
                    rvLastest.setAdapter(adapter);



                    //fondoCarga.setVisibility(View.GONE);
                   // carga.setVisibility(View.GONE);

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
                    //fondoCarga.setVisibility(View.GONE);
                    //carga.setVisibility(View.GONE);
                    productArrayList=new ArrayList<>();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productArrayList=new ArrayList<>();
                //fondoCarga.setVisibility(View.GONE);
                //carga.setVisibility(View.GONE);
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

    private void listaProducts() {

        // fondoCarga.setVisibility(View.VISIBLE);
        //carga.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "https://wsa.fabricasoftware.co/api/productos";


        //INICIA LA SOLICITUD AL SERVER
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    productArrayList=new ArrayList<>();
                    JSONObject data = response.getJSONObject("data");
                    JSONArray products=data.getJSONArray("products");
                    for (int i=0;i<products.length();i++){
                        JSONObject item = products.getJSONObject(i);

                        Product p=new Product();
                        p.setId(item.getLong("id"));
                        p.setNombre(item.getString("title"));
                        p.setDescripcion(item.getString("description"));
                        p.setPrecio(item.getLong("price"));
                        p.setImagen(item.getString("thumbnail"));

                        productArrayList.add(p);
                    }
                    rvProducts.setHasFixedSize(true);
                    LinearLayoutManager manager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
                    ProductAllAdapter adapter = new ProductAllAdapter(productArrayList);
                    rvProducts.setLayoutManager(manager);
                    rvProducts.setAdapter(adapter);



                    //fondoCarga.setVisibility(View.GONE);
                    // carga.setVisibility(View.GONE);

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
                    //fondoCarga.setVisibility(View.GONE);
                    //carga.setVisibility(View.GONE);
                    productArrayList=new ArrayList<>();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                productArrayList=new ArrayList<>();
                //fondoCarga.setVisibility(View.GONE);
                //carga.setVisibility(View.GONE);
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

    public void goToAddItem(View view) {
        Intent intent=new Intent(getApplicationContext(),AddItemActivity.class);
        startActivity(intent);
        finish();
    }
}