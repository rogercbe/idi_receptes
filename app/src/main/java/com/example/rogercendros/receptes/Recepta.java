package com.example.rogercendros.receptes;

import java.util.ArrayList;

public class Recepta{

    private String nomRecepta;
    private String descripcio;


    public Recepta(String nomRecepta, String descripcio){

        this.nomRecepta = nomRecepta;
        this.descripcio = descripcio;

    }

    public void setNomRecepta(String nomRecepta)
    {
        this.nomRecepta = nomRecepta;
    }

    public void setDescripcio(String descripcio)
    {
        this.descripcio = descripcio;
    }

    public String getNomRecepta(){return nomRecepta;}

    public String getDescripcio(){return descripcio;}

    @Override
    public String toString(){return nomRecepta + "," + descripcio;}
}