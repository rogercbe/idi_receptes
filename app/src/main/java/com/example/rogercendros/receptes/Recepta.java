package com.example.rogercendros.receptes;

import java.util.ArrayList;

public class Recepta{

    private String imatge;
    private String nomRecepta;
    private String descripcio;
    private ArrayList ingredients;


    public Recepta(String imatge, String nomRecepta, String descripcio, ArrayList ingredients){

        this.imatge = imatge;
        this.nomRecepta = nomRecepta;
        this.descripcio = descripcio;
        this.ingredients = ingredients;
        ;

    }

    public void setImatge(String imatge)
    {
        this.imatge = imatge;
    }

    public void setDescripcio(String descripcio)
    {
        this.descripcio = descripcio;
    }

    public void setNomRecepta(String nomRecepta)
    {
        this.nomRecepta = nomRecepta;
    }

    public void setIngredients(ArrayList ingredients)
    {
        this.ingredients = ingredients;
    }

    public String getImatge(){return imatge;}

    public String getNomRecepta(){return nomRecepta;}

    public String getDescripcio(){return descripcio;}

    public ArrayList getIngredients(){return ingredients;}

}