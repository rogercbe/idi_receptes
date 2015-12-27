package com.example.rogercendros.receptes;

import java.util.ArrayList;

public class Ingredient {

    private int id;
    private String nom;

    public Ingredient(){}

    public void setId(int id)
    {
        this.id = id;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public int getId(){return id;}

    public String getNom(){return nom;}

    @Override
    public String toString(){return nom;}

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Ingredient){
            Ingredient i = (Ingredient) v;
            retVal = i.id == this.id;
        }

        return retVal;
    }
}
