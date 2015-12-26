package com.example.rogercendros.receptes;

import java.util.ArrayList;

public class Recepta{

    private String nomRecepta;
    private String descripcio;
    private String categoria;
    private int imatge;


    public Recepta(String nomRecepta, String descripcio, String categoria, int imatge){

        this.nomRecepta = nomRecepta;
        this.descripcio = descripcio;
        this.categoria = categoria;
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

    public void setCategoria(String categoria)
    {
        this.categoria = categoria;
    }

    public void setImatge(int imatge)
    {
        this.imatge = imatge;
    }

    public String getNomRecepta(){return nomRecepta;}

    public String getDescripcio(){return descripcio;}

    public String getCategoria(){return categoria;}

    public int getImatge(){return imatge;}

    @Override
    public String toString(){return nomRecepta + "," + descripcio;}
}