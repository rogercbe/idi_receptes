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
        String query = "CREATE TABLE receptes (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_titol TEXT, " +
                "_categoria TEXT, " +
                "_descripcio TEXT," +
                "_data TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS receptes");
        onCreate(db);
    }

    // Afegir una nova recepta
    public void afegirRecepta(Recepta recepta)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valors = new ContentValues();
        valors.put("_titol", recepta.getTitol());
        valors.put("_categoria", recepta.getCategoria());
        valors.put("_descripcio", recepta.getDescripcio());
        db.insert("receptes", null, valors);
        db.close();
    }

    // Esborrar una recepta
    public void esborrarRecepta(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM receptes WHERE _id = " + id);
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
                recepta = new Recepta();
                recepta.setId(id);
                recepta.setTitol(titol);
                recepta.setCategoria(categoria);
                recepta.setDescripcio(descripcio);
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
                recepta.setImatge(R.drawable.image);
                receptes.add(recepta);
            } while(c.moveToNext());
        }

        if (c != null && c.isClosed()) c.close();

        return receptes;
    }
}


