package com.example.rogercendros.receptes;

import java.util.ArrayList;
import java.util.List;

public class Foto {
    private int idDrawable;

    public Foto(int idDrawable) {
        this.idDrawable = idDrawable;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public static Foto[] ITEMS = {
            new Foto(R.drawable.arroz),
            new Foto(R.drawable.bistec),
            new Foto(R.drawable.burger),
            new Foto(R.drawable.caldo),
            new Foto(R.drawable.canelons),
            new Foto(R.drawable.ensalada),
            new Foto(R.drawable.entrecot),
            new Foto(R.drawable.espaguetis),
            new Foto(R.drawable.fajas),
            new Foto(R.drawable.india),
            new Foto(R.drawable.lasagna),
            new Foto(R.drawable.macarrons),
            new Foto(R.drawable.paella),
            new Foto(R.drawable.pescado),
            new Foto(R.drawable.pollastre),
            new Foto(R.drawable.susi),
            new Foto(R.drawable.sopa),
            new Foto(R.drawable.tacos),
            new Foto(R.drawable.tapas),
            new Foto(R.drawable.vegetariana)
    };
}
