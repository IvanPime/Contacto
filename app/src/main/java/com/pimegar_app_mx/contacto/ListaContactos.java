package com.pimegar_app_mx.contacto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import base_de_datos.ContactoSQLiteHelper;

public class ListaContactos extends AppCompatActivity {

    private RecyclerView recView;
    private ArrayList<Contacto> datos;
    private Button insertar, buscar;
    private ContactoSQLiteHelper usdbh;
    private SQLiteDatabase db;
    private AdaptadorContactos adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        insertar = (Button) findViewById(R.id.insertar);
        buscar = (Button) findViewById(R.id.buscar);

        //Inicialización de la lista de datos de contactos
        datos = new ArrayList<Contacto>();

        //Abrimos la base de datos 'DBContactos' en modo escritura
        usdbh = new ContactoSQLiteHelper(this, "DBContactos", null, 1);
        db = usdbh.getWritableDatabase();

        //Si hemos abierto correctamente la base de datos
        if(db != null) {
            Cursor c = db.rawQuery("SELECT id_contacto,nombre,apellidos,ciudad,telefono,email FROM Contacto",null);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    int id_contacto = c.getInt(0);
                    String nombre = c.getString(1);
                    String apellidos = c.getString(2);
                    String ciudad = c.getString(3);
                    String telefono = c.getString(4);
                    String email = c.getString(5);
                    datos.add(new Contacto(id_contacto, nombre, apellidos, ciudad, telefono, email));
                } while(c.moveToNext());
            }

           /* //Cerramos la base de datos
            db.close();*/
        }

        //Inicialización RecyclerView
        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);
        adaptador = new AdaptadorContactos(datos, this);
        /*adaptador.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + recView.indexOfChild(v));
                return true;
            }
        });*/
        recView.setAdapter(adaptador);
        recView.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recView.setItemAnimator(new DefaultItemAnimator());

        //Evento del botón insertar
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(ListaContactos.this, DatosContactoActivity.class);
                startActivity(intent);
            }
        });
        registerForContextMenu(recView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                db.close();
                Intent intent =
                        new Intent(ListaContactos.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_preferences:
                Intent intent2 =
                        new Intent(ListaContactos.this, PreferenciasActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String[] id_contacto2 = new String[] {String.valueOf(((AdaptadorContactos) recView.getAdapter()).getIdContacto())};
        int positionItem = ((AdaptadorContactos) recView.getAdapter()).getPosition();
        if(db != null) {
            Cursor c = db.rawQuery("SELECT id_contacto,nombre,apellidos,ciudad,telefono,email FROM Contacto " +
                    "WHERE id_contacto = ?", id_contacto2);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    int id_contacto = c.getInt(0);
                    String nombre = c.getString(1);
                    String apellidos = c.getString(2);
                    String ciudad = c.getString(3);
                    String telefono = c.getString(4);
                    String email = c.getString(5);
                    switch (item.getItemId()) {
                        case 0:
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:"+telefono));
                            startActivity(callIntent);
                            break;
                        case 1:
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_TEXT,"Hola "+nombre.trim()+ " " +apellidos);
                            i.putExtra(Intent.EXTRA_EMAIL,
                                    new String[]{email.trim()});
                            try{
                                startActivity(Intent.createChooser(i,
                                        "¿Quien puede enviar un email?"));
                            }catch (android.content.ActivityNotFoundException e){
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            //Creamos el Intent
                            Intent intent =
                                    new Intent(ListaContactos.this, EditarContactoActivity.class);
                            Bundle b = new Bundle();
                            b.putString("nombre", nombre);
                            b.putString("apellidos", apellidos);
                            b.putString("ciudad", ciudad);
                            b.putString("telefono", telefono);
                            b.putString("email", email);
                            b.putInt("id_contacto", id_contacto);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case 3:
                            db.execSQL("DELETE FROM Contacto WHERE id_contacto=?",id_contacto2);
                            datos.remove(positionItem);
                            adaptador.notifyItemRemoved(positionItem);
                            break;
                    }
                } while (c.moveToNext());
            }
        }

        return super.onContextItemSelected(item);
    }


}

