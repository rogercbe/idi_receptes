package com.example.rogercendros.receptes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Filtre extends ActionBarActivity {

    private DBManager dbManager;
    public static List resultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);

        dbManager = new DBManager(this, null);

        inicialitzarSpinner();
        initSpinnerIngredients();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filtre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void inicialitzarSpinner()
    {
        List<String> spinnerArray =  new ArrayList<String>();

        spinnerArray.add("Cuina Asiàtica");
        spinnerArray.add("Cuina Creativa");
        spinnerArray.add("Cuina Mediterrània");
        spinnerArray.add("Cuina Mexicana");
        spinnerArray.add("Cuina Moderna");
        spinnerArray.add("Cuina Occidental");
        spinnerArray.add("Cuina Oriental");
        spinnerArray.add("Cuina Tradicional");
        spinnerArray.add("Cuina Vegetariana");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);
    }

    public void initSpinnerIngredients()
    {
        // get llista tots els ingredients
        List ingredients = dbManager.llegirIngredients();
        // per cada ingredient afegirlo al spinner
        List spinnerArray =  new ArrayList<Ingredient>();
        Ingredient ing;

        for(int i = 0; i < ingredients.size(); ++i) {
            ing = (Ingredient)ingredients.get(i);
            spinnerArray.add(ing);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner2);
        sItems.setAdapter(adapter);
    }

    public void filtratge(View v){
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String categoria = spinner.getSelectedItem().toString();
        resultat = dbManager.filtrarCategoria(categoria);
        if(resultat.size() > 0) {
            Intent intent = new Intent(this, ResultatFiltre.class);
            startActivity(intent);
        } else Toast.makeText(this, "No s'han trobat resultats.", Toast.LENGTH_SHORT).show();
    }

    public void filtratgeIngredient(View v){
        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        Ingredient ing = (Ingredient)spinner2.getSelectedItem();
        resultat = dbManager.filtrarIngredients(ing.getId());

        if(resultat.size() > 0) {
            Intent intent = new Intent(this, ResultatFiltre.class);
            startActivity(intent);
        } else Toast.makeText(this, "No s'han trobat resultats.", Toast.LENGTH_SHORT).show();
    }
}
