package com.example.rogercendros.receptes;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    static List receptes = new ArrayList<Recepta>();

    static{

        receptes.add(new Recepta("Macarrons","5 estrelles"));
        receptes.add(new Recepta("Canelons","3 estrelles"));
        receptes.add(new Recepta("Fajitas","4 estrelles"));
        receptes.add(new Recepta("Pollastre","0 estrelles"));
        receptes.add(new Recepta("Polla","5 estrelles"));

    }

}