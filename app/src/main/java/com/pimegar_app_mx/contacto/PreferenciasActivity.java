package com.pimegar_app_mx.contacto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class PreferenciasActivity extends AppCompatActivity {
    EditText urlPreferencias, emailPreferencias, telefonoPreferencias;
    RadioGroup region;
    Button guardar, cancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        urlPreferencias = (EditText) findViewById(R.id.url_preferencias);
        emailPreferencias = (EditText) findViewById(R.id.email_preferencias);
        telefonoPreferencias = (EditText) findViewById(R.id.telefono_preferencias);
        region = (RadioGroup) findViewById(R.id.RadioRegion);
        guardar = (Button) findViewById(R.id.guardar_preferencias);
        cancelar = (Button) findViewById(R.id.cancelar_preferencias);

        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String urlPref = prefs.getString("urlPreferencias", "");
        String emailPref = prefs.getString("emailPreferencias", "");
        String telefonoPref = prefs.getString("telefonoPreferencias", "");
        String regionPref = prefs.getString("region", "");
        urlPreferencias.setText(urlPref);
        emailPreferencias.setText(emailPref);
        telefonoPreferencias.setText(telefonoPref);
        switch(regionPref) {
            case "Africa":
                region.check(R.id.RbOpcion1);
                break;
            case "Americas":
                region.check(R.id.RbOpcion2);
                break;
            case "Asia":
                region.check(R.id.RbOpcion3);
                break;
            case "Europe":
                region.check(R.id.RbOpcion4);
                break;
            case "Oceania":
                region.check(R.id.RbOpcion5);
                break;
            default:
                region.check(R.id.RbOpcion1);
                break;
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("urlPreferencias", PreferenciasActivity.this.urlPreferencias.getText().toString());
                editor.putString("emailPreferencias", PreferenciasActivity.this.emailPreferencias.getText().toString());
                editor.putString("telefonoPreferencias", PreferenciasActivity.this.telefonoPreferencias.getText().toString());
                int idSeleccionado = region.getCheckedRadioButtonId();
                switch(idSeleccionado) {
                    case R.id.RbOpcion1:
                        editor.putString("region", "Africa");
                        break;
                    case R.id.RbOpcion2:
                        editor.putString("region", "Americas");
                        break;
                    case R.id.RbOpcion3:
                        editor.putString("region", "Asia");
                        break;
                    case R.id.RbOpcion4:
                        editor.putString("region", "Europe");
                        break;
                    case R.id.RbOpcion5:
                        editor.putString("region", "Oceania");
                        break;
                }
                editor.commit();
                Intent intent =
                        new Intent(PreferenciasActivity.this, ListaContactos.class);
                startActivity(intent);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(PreferenciasActivity.this, ListaContactos.class);
                startActivity(intent);
            }
        });
    }
}
