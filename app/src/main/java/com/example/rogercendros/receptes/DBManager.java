package com.example.rogercendros.receptes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "receptari.db";

    public DBManager(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryReceptes = "CREATE TABLE receptes (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_titol TEXT, " +
                "_categoria TEXT, " +
                "_descripcio TEXT," +
                "_imatge INTEGER," +
                "_data TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(queryReceptes);

        String queryIngredients = "CREATE TABLE ingredients (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_ingredient TEXT)";
        db.execSQL(queryIngredients);

        String queryAsso = "CREATE TABLE receptes_ingredients (" +
                "_idRecepta INTEGER, " +
                "_idIngredient INTEGER," +
                "FOREIGN KEY(_idRecepta) REFERENCES receptes(_id)," +
                "FOREIGN KEY(_idIngredient) REFERENCES ingredients(_id))";
        db.execSQL(queryAsso);

        String querySubs = "CREATE TABLE substitucions (" +
                "_idRecepta INTEGER, " +
                "_idOriginal INTEGER," +
                "_idNou INTEGER," +
                "FOREIGN KEY(_idRecepta) REFERENCES receptes(_id)," +
                "FOREIGN KEY(_idOriginal) REFERENCES ingredients(_id)," +
                "FOREIGN KEY(_idNou) REFERENCES ingredients(_id))";
        db.execSQL(querySubs);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS receptes");
        onCreate(db);
    }

    // Afegir una nova recepta
    public int afegirRecepta(Recepta recepta)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put("_titol", recepta.getTitol());
        valors.put("_categoria", recepta.getCategoria());
        valors.put("_descripcio", recepta.getDescripcio());
        valors.put("_imatge", recepta.getImatge());
        long id = db.insert("receptes", null, valors);
        db.close();
        return (int)id;
    }

    public void actualitzarRecepta(Recepta recepta)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put("_titol", recepta.getTitol());
        valors.put("_categoria", recepta.getCategoria());
        valors.put("_descripcio", recepta.getDescripcio());
        valors.put("_imatge", recepta.getImatge());
        db.update("receptes", valors, "_id = " + recepta.getId(), null);
        db.close();
    }

    // Esborrar una recepta
    public void esborrarRecepta(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM receptes WHERE _id = " + id);
    }

    // Esborrar ingredients d'una recepta
    public void esborrarIngredientsDeRecepta(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM receptes_ingredients WHERE _idRecepta = " + id);
    }

    public Recepta llegirReceptaPerId(int _id)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor =  db.rawQuery("select * from receptes where _id =" + _id  , null);
        Recepta recepta = null;
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String titol = cursor.getString(cursor.getColumnIndex("_titol"));
                String categoria = cursor.getString(cursor.getColumnIndex("_categoria"));
                String descripcio = cursor.getString(cursor.getColumnIndex("_descripcio"));
                int imatge = cursor.getInt(cursor.getColumnIndex("_imatge"));
                recepta = new Recepta();
                recepta.setId(id);
                recepta.setTitol(titol);
                recepta.setCategoria(categoria);
                recepta.setDescripcio(descripcio);
                recepta.setImatge(imatge);
            }
            cursor.close();
        }
        return recepta;

    }

    // llegir totes les receptes
    public List llegirReceptes()
    {
        List receptes = new ArrayList<Recepta>();
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        Cursor c = db.query("receptes", null, null, null, null, null, "_data DESC");

        if (c.moveToFirst()) {
            do {
                Recepta recepta = new Recepta();
                recepta.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                recepta.setTitol(c.getString(c.getColumnIndexOrThrow("_titol")));
                recepta.setCategoria(c.getString(c.getColumnIndexOrThrow("_categoria")));
                recepta.setDescripcio(c.getString(c.getColumnIndexOrThrow("_descripcio")));
                recepta.setImatge(c.getInt(c.getColumnIndexOrThrow("_imatge")));
                receptes.add(recepta);
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        return receptes;
    }

    // llegir totes les receptes
    public List llegirIngredients()
    {
        List ingredients = new ArrayList<Ingredient>();
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        Cursor c = db.query("ingredients", null, null, null, null, null, "_ingredient ASC");

        if (c.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                ingredient.setNom(c.getString(c.getColumnIndexOrThrow("_ingredient")));
                ingredients.add(ingredient);
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        return ingredients;
    }

    public List llegirIngredientsDeRecepta(int id)
    {
        List ingredients = new ArrayList<Ingredient>();
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM receptes_ingredients a JOIN ingredients b ON a._idIngredient=b._id WHERE a._idRecepta="+id;
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                ingredient.setNom(c.getString(c.getColumnIndexOrThrow("_ingredient")));
                ingredients.add(ingredient);
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        return ingredients;
    }

    // Afegir un nou ingredient
    public void afegirIngredient(String ingredient)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put("_ingredient", ingredient);
        db.insert("ingredients", null, valors);
        db.close();
    }

    public void afegirIngredientsARecepta(int id, List ingredients)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valors = new ContentValues();
        Ingredient ing;

        for (int i = 0; i < ingredients.size(); ++i) {
            ing = (Ingredient)ingredients.get(i);
            valors.put("_idRecepta", id);
            valors.put("_idIngredient", ing.getId());
            db.insert("receptes_ingredients", null, valors);
        }

        db.close();
    }

    //String amb tots els ingredients donat una recepta
    public String getLlistaIngredients(int id)
    {
        String llista = "";
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM receptes_ingredients a JOIN ingredients b ON a._idIngredient=b._id WHERE a._idRecepta="+id;
        Cursor c = db.rawQuery(query, null);
        //Cursor c = db.query("receptes_ingredients", null, "_idRecepta = " + id, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                llista += c.getString(c.getColumnIndexOrThrow("_ingredient")) + ", ";
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        //treure la ultima coma i posar un punt, tot en minúscules menys la primera.
        if (llista == "") return "Aquesta recepta no te ingredients afegits!";
        llista.toLowerCase();
        llista = llista.substring(0, llista.length()-2) + ".";
        return llista.substring(0, 1).toUpperCase() + llista.substring(1);
    }

    public void afegirIngredientsSubstituts(int id, List ingredients)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valors = new ContentValues();
        Substitut ing;

        for (int i = 0; i < ingredients.size(); ++i) {
            ing = (Substitut)ingredients.get(i);
            valors.put("_idRecepta", id);
            valors.put("_idOriginal", ing.getIdOriginal());
            valors.put("_idNou", ing.getIdNou());
            db.insert("substitucions", null, valors);
        }

        db.close();
    }

    public void seedIngredients()
    {
        String[] ingredients = {"Enciam", "Sal", "Sucre", "Patates", "Pollastre", "Pebre", "Llimona", "Pebrot", "Arros", "Macarrons",
        "Espaguetis", "Orenga", "Ous", "Llet", "Aigua", "Farina", "Llevat", "Xocolata", "Galetes", "Cervesa", "Vi", "Fruits Secs", "Vinagre", "Oli",
        "Llobarro", "Vedella", "Porc", "Galets", "Turrons", "Castanyes", "Pastanaga", "Mongetes", "Cava", "Cola", "Caldo", "Gambes", "Sardina", "Butifarra" };

        for(int i = 0; i < ingredients.length; ++i) {
            afegirIngredient(ingredients[i]);
        }
    }
}


