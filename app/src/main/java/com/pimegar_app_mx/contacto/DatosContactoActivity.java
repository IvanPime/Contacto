package com.pimegar_app_mx.contacto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import base_de_datos.ContactoSQLiteHelper;

public class DatosContactoActivity extends AppCompatActivity {
    EditText nombre, apellidos, telefono, email;
    Spinner ciudad;
    Button guardar, cancelar;
    String region, ciudadSeleccionada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_contacto);

        nombre = (EditText) findViewById(R.id.nombre);
        apellidos = (EditText) findViewById(R.id.apellidos);
        telefono = (EditText) findViewById(R.id.telefono);
        email = (EditText) findViewById(R.id.email);
        ciudad = (Spinner)findViewById(R.id.ciudad);
        guardar = (Button) findViewById(R.id.guardar);
        cancelar = (Button) findViewById(R.id.cancelar);


        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        region = prefs.getString("region", "americas");

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("entro");
                String nombre = DatosContactoActivity.this.nombre.getText().toString();
                String apellidos = DatosContactoActivity.this.apellidos.getText().toString();
                String telefono = DatosContactoActivity.this.telefono.getText().toString();
                String email = DatosContactoActivity.this.email.getText().toString();
                String ciudad = DatosContactoActivity.this.ciudadSeleccionada;


                //Abrimos la base de datos 'DBContactos' en modo escritura
                ContactoSQLiteHelper usdbh =
                        new ContactoSQLiteHelper(DatosContactoActivity.this, "DBContactos", null, 1);
                SQLiteDatabase db = usdbh.getWritableDatabase();

                //Si hemos abierto correctamente la base de datos
                if(db != null) {
                    String consulta =  "INSERT INTO Contacto (nombre, apellidos, ciudad, telefono, email) " +
                            "VALUES ('" + nombre + "', '" + apellidos +"', '" + ciudad +"', '" + telefono +"', '" + email +"')";
                    System.out.println(consulta);
                    //Insertamos los datos en la tabla Usuarios
                    db.execSQL(consulta);
                    //Cerramos la base de datos
                    db.close();
                }
                Intent intent =
                        new Intent(DatosContactoActivity.this, ListaContactos.class);
                startActivity(intent);

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(DatosContactoActivity.this, ListaContactos.class);
                startActivity(intent);
            }
        });

        getData();

        ciudad.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        ciudadSeleccionada =  parent.getItemAtPosition(position).toString();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    public void getData(){
        ArrayList<String> datos = new ArrayList<String>();
        datos.add("");
        String sql = "https://restcountries.eu/rest/v2/region/" + region;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection conn;

        try {
            URL url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer response = new StringBuffer();

            String inputLine;
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            String json = response.toString();
            JSONArray jsonArr = new JSONArray(json);

            for(int i = 0;i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                datos.add(jsonObject.optString("capital"));
            }
            ArrayAdapter<String> adaptador =
                    new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, datos);
            ciudad.setAdapter(adaptador);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
