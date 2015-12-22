package com.example.rogercendros.receptes;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    static List receptes = new ArrayList<Recepta>();

    static{

        receptes.add(new Recepta("Macarrons gratinats de l'àvia Maria","Cuina tradicional", R.drawable.macarrons));
        receptes.add(new Recepta("Macarrons","5 estrelles", R.drawable.image));
        receptes.add(new Recepta("Macarrons","5 estrelles", R.drawable.image));
        receptes.add(new Recepta("Macarrons","5 estrelles", R.drawable.image));
    }

}

