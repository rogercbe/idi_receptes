package com.example.rogercendros.receptes;

import java.util.ArrayList;

public class Recepta{

    private String nomRecepta;
    private String descripcio;
    private int imatge;
    private String nombre;


    public Recepta(String nomRecepta, String descripcio, int imatge){

        this.nomRecepta = nomRecepta;
        this.descripcio = descripcio;
        this.imatge = imatge;

    }

    public void setNomRecepta(String nomRecepta)
    {
        this.nomRecepta = nomRecepta;
    }

    public void setDescripcio(String descripcio)
    {
        this.descripcio = descripcio;
    }

    public void setImatge(int imatge)
    {
        this.imatge = imatge;
    }

    public String getNomRecepta(){return nomRecepta;}

    public String getDescripcio(){return descripcio;}

    public int getImatge(){return imatge;}

    @Override
    public String toString(){return nomRecepta + "," + descripcio;}

    public String getNombre() {
        return nombre;
    }
}