package com.chriscursos.usedzip;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import model.Product;

public class ProductAllAdapter extends RecyclerView.Adapter<ProductAllAdapter.ViewHolder> {

    private ArrayList<Product> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombre;
        private TextView txtDescripcion;
        private TextView txtPrecio;
        private ImageView ivImagen;
        private CardView item;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            txtNombre = (TextView) view.findViewById(R.id.txtNombre);
            ivImagen = (ImageView) view.findViewById(R.id.ivImagen);
            txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
            txtPrecio = (TextView) view.findViewById(R.id.txtPrecio);
            item = (CardView) view.findViewById(R.id.itemProductos);
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public ProductAllAdapter(ArrayList<Product> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_product_lastest, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.txtNombre.setText(localDataSet.get(position).getNombre());
        viewHolder.txtDescripcion.setText(localDataSet.get(position).getDescripcion());
        viewHolder.txtPrecio.setText("$"+String.valueOf(localDataSet.get(position).getPrecio()));
        int po=position;

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = localDataSet.get(po).getId();
                Intent intent = new Intent(view.getContext(),DetailsActivity.class);
                intent.putExtra("id",id);
                view.getContext().startActivity(intent);
            }
        });



        Glide.with(viewHolder.ivImagen.getContext()).load(localDataSet.get(position).getImagen()).into(viewHolder.ivImagen);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
