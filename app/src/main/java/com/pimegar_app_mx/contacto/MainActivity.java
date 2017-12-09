package com.pimegar_app_mx.contacto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent =
                new Intent(MainActivity.this, ListaContactos.class);

        startActivity(intent);
    }


}
