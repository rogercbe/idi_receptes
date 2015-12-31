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

        db.execSQL("PRAGMA foreign_keys = ON");

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

    // Esborrar un ingredient
    public void esborrarIngredient(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM ingredients WHERE _id = " + id);
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
    public int afegirIngredient(String ingredient)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put("_ingredient", ingredient.toLowerCase());
        long id = db.insert("ingredients", null, valors);
        db.close();
        return (int)id;
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
                "Llobarro", "Vedella", "Porc", "Galets", "Turrons", "Castanyes", "Pastanaga", "Mongetes", "Cava", "Cola", "Caldo", "Gambes", "Sardina", "Butifarra", "Tomaquet", "Ceba", "Formatge" };

        for(int i = 0; i < ingredients.length; ++i) {
            afegirIngredient(ingredients[i]);
        }
    }

    public Ingredient getIngredientById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM ingredients WHERE _id="+id;
        Cursor c = db.rawQuery(query, null);
        Ingredient ingredient = new Ingredient();

        if (c.moveToFirst()) {
            do {
                ingredient.setId(c.getInt((c.getColumnIndexOrThrow("_id"))));
                ingredient.setNom(c.getString(c.getColumnIndexOrThrow("_ingredient")));
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        return ingredient;
    }

    public List llegirSubstitutsDeRecepta(int id)
    {
        List ingredients = new ArrayList<Substitut>();
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM substitucions WHERE _idRecepta="+id;
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                int idNou = c.getInt(c.getColumnIndexOrThrow("_idNou"));
                int idOriginal = c.getInt(c.getColumnIndexOrThrow("_idOriginal"));
                Substitut ingredient = new Substitut(idOriginal, idNou);
                ingredients.add(ingredient);
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        return ingredients;
    }

    public List getIngredientsDeRecepta(int id)
    {
        List ingredients = new ArrayList<Ingredient>();
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM receptes_ingredients a JOIN ingredients b ON a._idIngredient=b._id WHERE a._idRecepta="+id;
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Ingredient i = new Ingredient();
                i.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                i.setNom(c.getString(c.getColumnIndexOrThrow("_ingredient")));
                ingredients.add(i);
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        return ingredients;
    }

    public String getSubstitutsDeIngredient(long id_recepta, int id_ingredient)
    {
        String llista = "";
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM substitucions s JOIN ingredients i ON s._idNou=i._id WHERE s._idRecepta="+id_recepta+" AND s._idOriginal ="+id_ingredient;
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                String ing = c.getString(c.getColumnIndexOrThrow("_ingredient"));
                ing = ing.substring(0, 1).toUpperCase() + ing.substring(1);
                llista += ing + "\n";
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        if (llista == "") return "Aquest ingredient és essencial!";
        return llista;
    }

    public boolean getIngredientByNom(String ingredient) {
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM ingredients i WHERE i._ingredient = '"+ingredient.toLowerCase()+"'";
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            return true;
        }

        if (c != null && c.isClosed()) c.close();

        return false;
    }

    public void seedReceptes()
    {

        Recepta macarrons = new Recepta();
        macarrons.setImatge(R.drawable.macarrons);
        macarrons.setTitol("Macarrons casolans");
        macarrons.setDescripcio("Macarrons a la bolonyesa amb salsa de tomaquet i carn picada.");
        macarrons.setCategoria("Cuina Mediterrània");
        afegirRecepta(macarrons);

        Recepta arros = new Recepta();
        arros.setImatge(R.drawable.arroz);
        arros.setTitol("Arros a les mil delicies");
        arros.setDescripcio("Arros guarnit amb verdures i espècies.");
        arros.setCategoria("Cuina Asiàtica");
        afegirRecepta(arros);

        Recepta tacos = new Recepta();
        tacos.setImatge(R.drawable.fajas);
        tacos.setTitol("Fajitas Mejicanas");
        tacos.setDescripcio("Carn de vedella acompanyada amb un sofregit de pebrot, tomaquet i ceba embolicat en tortitas.");
        tacos.setCategoria("Cuina Mexicana");
        afegirRecepta(tacos);

        Recepta amanida = new Recepta();
        amanida.setImatge(R.drawable.ensalada);
        amanida.setTitol("Pensament Verd");
        amanida.setDescripcio("La millor recepta per soprendre a casa, amanida verda amb verdures i condiments.");
        amanida.setCategoria("Cuina Moderna");
        afegirRecepta(amanida);

        Recepta bistec = new Recepta();
        bistec.setImatge(R.drawable.entrecot);
        bistec.setTitol("Filet de Vedella");
        bistec.setDescripcio("Carn de vedella de primera qualitat acompanyat amb pataes fregides i amanida.");
        bistec.setCategoria("Cuina Occidental");
        afegirRecepta(bistec);

        // afegir ingredients
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 1 + ", " + 10 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 1 + ", " + 39 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 1 + ", " + 40 +")");

        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 2 + ", " + 9 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 2 + ", " + 39 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 2 + ", " + 36 +")");

        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 3 + ", " + 5 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 3 + ", " + 8 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 3 + ", " + 39 +")");

        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 5 + ", " + 1 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 5 + ", " + 4 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 5 + ", " + 26 +")");

        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 4 + ", " + 1 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 4 + ", " + 31 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 4 + ", " + 23 +")");
        db.execSQL("INSERT INTO receptes_ingredients VALUES("+ 4 + ", " + 24 +")");

        //afegir substitucions

        db.execSQL("INSERT INTO substitucions VALUES("+ 1 + ", " + 39 + ", " + 24 +")");
        db.execSQL("INSERT INTO substitucions VALUES("+ 2 + ", " + 36 + ", " + 5 +")");
        db.execSQL("INSERT INTO substitucions VALUES("+ 3 + ", " + 5 + ", " + 26 +")");
        db.execSQL("INSERT INTO substitucions VALUES("+ 4 + ", " + 31 + ", " + 41 +")");
        db.execSQL("INSERT INTO substitucions VALUES("+ 5 + ", " + 4 + ", " + 9 +")");
        db.execSQL("INSERT INTO substitucions VALUES("+ 5 + ", " + 4 + ", " + 32 +")");



    }

    public ArrayList<Recepta> filtrarCategoria(String categoria) {

        ArrayList<Recepta> receptes = new ArrayList<Recepta>();
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        Cursor c = db.query("receptes", null, "_categoria = '" + categoria +"'", null, null, null, "_data DESC");

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

    public List filtrarIngredients(int id) {
        ArrayList<Recepta> receptes = new ArrayList<Recepta>();
        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM receptes_ingredients s JOIN receptes r ON s._idRecepta=r._id WHERE s._idIngredient="+id;
        Cursor c = db.rawQuery(query, null);

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

    public List filtrarSenseIngredients(List llistaIngredients) {

        ArrayList<Recepta> receptes = new ArrayList<Recepta>();

        //construir el where
        String where;
        if (llistaIngredients.size() > 0) {
            where = "WHERE s._idIngredient=";

            for(int i = 0; i < llistaIngredients.size(); ++i) {
                Ingredient ing = (Ingredient) llistaIngredients.get(i);
                int id = ing.getId();
                where += id + " OR s._idIngredient=";
            }

            where = where.substring(0, where.length()-20);

        } else return llegirReceptes();

        //get receptes que tenen aquests ingredients

        SQLiteDatabase db = getWritableDatabase();
        //query("table", tableColumns, whereClause, whereArgs, null, null, orderBy);
        String query = "SELECT * FROM receptes_ingredients s JOIN receptes r ON s._idRecepta=r._id " + where;
        Cursor c = db.rawQuery(query, null);

        List<Integer> id_receptes = new ArrayList<Integer>();

        if (c.moveToFirst()) {
            do {
                id_receptes.add(c.getInt(c.getColumnIndexOrThrow("_id")));
            } while(c.moveToNext());
        }

        //obtenir receptes que no son aquestes receptes
        String clause;
        clause = " WHERE _id <> ";

        for(int i = 0; i <id_receptes.size(); ++i) {
            int id = id_receptes.get(i);
            clause += id + " AND _id <> ";
        }

        clause = clause.substring(0, clause.length()-12);

        query = "SELECT * FROM receptes " + clause;
        c = db.rawQuery(query, null);

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
}


