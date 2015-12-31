package com.example.rogercendros.receptes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recepta {

    private int id;
    private String titol;
    private String descripcio;
    private String categoria;
    private int imatge;


    public Recepta(){}

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTitol(String titol)
    {
        this.titol = titol;
    }

    public void setDescripcio(String descripcio)
    {
        this.descripcio = descripcio;
    }

    public void setCategoria(String categoria)
    {
        this.categoria = categoria;
    }

    public void setImatge(int imatge)
    {
        this.imatge = imatge;
    }

    public int getId(){return id;}

    public String getTitol(){return titol;}

    public String getDescripcio(){return descripcio;}

    public String getCategoria(){return categoria;}

    public int getImatge(){return imatge;}

    @Override
    public String toString(){return titol;}

}