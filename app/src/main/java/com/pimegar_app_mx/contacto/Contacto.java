package com.pimegar_app_mx.contacto;

/**
 * Created by Ivan on 05/12/2017.
 */

public class Contacto {
    private int idContacto;
    private String nombre;
    private String apellidos;
    private String ciudad;
    private String telefono;
    private String email;

    public Contacto(int idContacto, String nombre, String apellidos, String ciudad, String telefono, String email) {
        this.idContacto = idContacto;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.email = email;
    }


    public int getIdContacto() {
        return idContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

}
