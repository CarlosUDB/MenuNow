package com.example.cp192194_av180327_cm180362_pa210749;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlatilloAdapter extends RecyclerView.Adapter<PlatilloAdapter.PlatilloViewHolder> {
    private List<Platillo> platillos;
    private LayoutInflater inflater;

    public PlatilloAdapter(Context context, List<Platillo> platillos) {
        this.inflater = LayoutInflater.from(context);
        this.platillos = platillos;
    }

    @Override
    public PlatilloViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_platillo, parent, false);
        return new PlatilloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlatilloViewHolder holder, int position) {
        Platillo platillo = platillos.get(position);
        holder.bind(platillo);
    }

    @Override
    public int getItemCount() {
        return platillos.size();
    }

    class PlatilloViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView, descriptionView, priceView;
        private ImageView imageView;
        private Button btnEditar, btnEliminar;

        PlatilloViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textViewNombre);
            descriptionView = itemView.findViewById(R.id.textViewDescripcion);
            priceView = itemView.findViewById(R.id.textViewPrecio);
            imageView = itemView.findViewById(R.id.imageViewPlatillo);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        void bind(Platillo platillo) {
            nameView.setText(platillo.getName());
            descriptionView.setText(platillo.getDescription());
            priceView.setText("$" + platillo.getPrice());
            Picasso.get().load(platillo.getImageUrl()).into(imageView);

            // Verificar el estado de autenticación para mostrar/ocultar botones
            //DESCOMENTAR SI SE NECESITA OCULTAR EL BOTON DE  AGFREGAR PLATILLO
            /*if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                btnEditar.setVisibility(View.VISIBLE);
                btnEliminar.setVisibility(View.VISIBLE);
            } else {
                btnEditar.setVisibility(View.GONE);
                btnEliminar.setVisibility(View.GONE);
            }*/


            btnEditar.setOnClickListener(v -> {
                // Lógica para editar el platillo
                // donde puedes pasar el ID del platillo o el objeto Platillo completo para editarlo.
            });

            btnEliminar.setOnClickListener(v -> {
                // Lógica para eliminar el platillo
                // en Firebase o en cualquier otra base de datos que estés usando.
            });
        }
    }
}