package com.pimegar_app_mx.contacto;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {



    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Button mEmailSignInButton;
    private Boolean inicioSesion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        inicioSesion = false;

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEmailView.getText().length() > 0 && mPasswordView.getText().length() > 0) {
                    mProgressView = findViewById(R.id.login_progress);
                    mProgressView.setVisibility(View.VISIBLE);
                    SharedPreferences prefs = getSharedPreferences("MisPrefe", Context.MODE_PRIVATE);
                    String correoLoginPref = prefs.getString("correoLoginPreferencias", "");
                    String passLoginPref = prefs.getString("passLoginPreferencias", "");
                    if(correoLoginPref.length() > 0 && passLoginPref.length() > 0) {
                        if(mEmailView.getText().toString().equals(correoLoginPref) && mPasswordView.getText().toString().equals(passLoginPref)) {
                            inicioSesion = true;
                            Intent intent =
                                    new Intent(LoginActivity.this, ListaContactos.class);
                            startActivity(intent);
                            mProgressView.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(LoginActivity.this, "¡Correo o Contraseña equivocados!",
                                    Toast.LENGTH_LONG).show();
                            mProgressView.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        SharedPreferences.Editor editor = prefs.edit();
                        if (correoLoginPref.length() == 0) {
                            editor.putString("correoLoginPreferencias", mEmailView.getText().toString());
                            System.out.println(mEmailView.getText().toString());
                        }
                        if (passLoginPref.length() == 0) {
                            editor.putString("passLoginPreferencias", mPasswordView.getText().toString());
                            System.out.println(mPasswordView.getText().toString());
                        }
                        editor.commit();
                        inicioSesion = true;
                        Intent intent =
                                new Intent(LoginActivity.this, ListaContactos.class);
                        startActivity(intent);
                        mProgressView.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Ha dejado campos vacios",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(this.inicioSesion) {
            Intent intent =
                    new Intent(LoginActivity.this, ListaContactos.class);
            startActivity(intent);
        }
    }

    public void setInicioSesion(Boolean inicioSesion) {
        this.inicioSesion = inicioSesion;
    }
}

