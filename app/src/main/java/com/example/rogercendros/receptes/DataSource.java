package com.example.rogercendros.receptes;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    static List receptes = new ArrayList<Recepta>();

    static{

        receptes.add(new Recepta("Macarrons gratinats de l'àvia Maria", "","Cuina tradicional", R.drawable.macarrons));
        receptes.add(new Recepta("Macarrons", "","Cuina mediterrània", R.drawable.image));
        receptes.add(new Recepta("Macarrons", "","Cuina occidental", R.drawable.image));
        receptes.add(new Recepta("Macarrons", "","Altres cuines", R.drawable.image));
        receptes.add(new Recepta("Macarrons", "","Altres cuines", R.drawable.image));
    }

}

