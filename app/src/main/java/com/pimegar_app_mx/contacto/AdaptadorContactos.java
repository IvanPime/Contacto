package com.pimegar_app_mx.contacto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Ivan on 05/12/2017.
 */

public class AdaptadorContactos extends  RecyclerView.Adapter<AdaptadorContactos.ContactosViewHolder> implements View.OnLongClickListener{
    private ArrayList<Contacto> datos;
    private View.OnLongClickListener listener;
    private int position;
    private int idContacto;
    private Context context;

    @Override
    public boolean onLongClick(View v) {
        if(listener != null) {
            listener.onLongClick(v);
            return true;
        }
        return false;
    }

    public static class ContactosViewHolder
            extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private TextView txtTitulo;
        private TextView txtSubtitulo, txtCiudad, txtEmail;
        private ImageView imagen;
        private String url = "https://images.vexels.com/media/users/3/135247/isolated/preview/e70a6296c2a79dc7a56ab05b103f38e8-signo-del-usuario-con-el-fondo-by-vexels.png";
        Bitmap imagenBM;
        public ContactosViewHolder(View itemView, final Context context) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            txtTitulo = (TextView)itemView.findViewById(R.id.LblTitulo);
            txtSubtitulo = (TextView)itemView.findViewById(R.id.LblSubTitulo);
            imagen = (ImageView)itemView.findViewById(R.id.imagenLs);
            txtCiudad = (TextView)itemView.findViewById(R.id.LblCiudad);
            txtEmail = (TextView)itemView.findViewById(R.id.LblEmail);
            imagenBM = getImageBitmap(context, "contacto");
            if(imagenBM == null) {
                Glide.with(context)
                        .load(url)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(200,200) {
                            @Override public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                saveImage(context, resource, "contacto");
                                imagen.setImageBitmap(resource);
                            }});
            } else {
                imagen.setImageBitmap(imagenBM);
            }

        }

        public void bindContacto(Contacto t) {
            txtTitulo.setText(t.getNombre().trim() + " " + t.getApellidos().trim());
            txtCiudad.setText(t.getCiudad());
            txtSubtitulo.setText(t.getTelefono());
            txtEmail.setText(t.getEmail());

        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo)
        {
            menu.setHeaderTitle("Seleccionar una acción");
            menu.add(0, 0, 0, "Llamar");
            menu.add(0, 1, 0, "Escribir Email");
            menu.add(0, 2, 0, "Editar");
            menu.add(0, 3, 0, "Eliminar");

        }
        public void saveImage(Context context, Bitmap b, String name){

            FileOutputStream out;
            try {
                out = context.openFileOutput(name, Context.MODE_PRIVATE);
                b.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Bitmap getImageBitmap(Context context,String name){
            try{
                FileInputStream fis = context.openFileInput(name);
                Bitmap b = BitmapFactory.decodeStream(fis);
                fis.close();
                return b;
            }
            catch(Exception e){
            }
            return null;
        }
    }


    public AdaptadorContactos(ArrayList<Contacto> datos, Context context) {
        this.datos = datos;
        this.context = context;
    }

    @Override
    public ContactosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_contacto, viewGroup, false);
        itemView.setOnLongClickListener(this);

        ContactosViewHolder tvh = new ContactosViewHolder(itemView, context);

        return tvh;
    }

    @Override
    public void onBindViewHolder(final ContactosViewHolder viewHolder, int pos) {
        final Contacto item = datos.get(pos);
        viewHolder.bindContacto(item);

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                setPosition(viewHolder.getAdapterPosition());
                setIdContacto(item.getIdContacto());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.listener = listener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(int idContacto) {
        this.idContacto = idContacto;
    }

}
