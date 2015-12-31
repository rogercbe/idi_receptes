package com.example.rogercendros.receptes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Filtre extends ActionBarActivity {

    private DBManager dbManager;
    public static List resultat;
    public static List llistaIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre);

        llistaIngredients = new ArrayList<Ingredient>();

        dbManager = new DBManager(this, null);

        inicialitzarSpinner();
        initSpinnerIngredients();
        inicialitzarTipoFiltre();
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

    public void inicialitzarTipoFiltre()
    {
        List<String> spinnerArray =  new ArrayList<String>();

        spinnerArray.add("Per Categoria");
        spinnerArray.add("Amb Ingredient");
        spinnerArray.add("Sense Ingredient");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.stipo);
        sItems.setAdapter(adapter);

        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner spinner = (Spinner)findViewById(R.id.stipo);
                String tipo = spinner.getSelectedItem().toString();
                //mostrar diferents layouts depenent de la seleccio
                if(tipo == "Per Categoria") {

                    LinearLayout perCategoria = (LinearLayout) findViewById(R.id.filtre_categoria);
                    perCategoria.setVisibility(View.VISIBLE);
                    LinearLayout ambIngredient = (LinearLayout) findViewById(R.id.filtre_amb_ingredient);
                    ambIngredient.setVisibility(View.INVISIBLE);
                    LinearLayout senseIngredient = (LinearLayout) findViewById(R.id.filtre_sense_ingredient);
                    senseIngredient.setVisibility(View.INVISIBLE);

                } else if(tipo == "Amb Ingredient") {

                    LinearLayout perCategoria = (LinearLayout) findViewById(R.id.filtre_categoria);
                    perCategoria.setVisibility(View.INVISIBLE);
                    LinearLayout ambIngredient = (LinearLayout) findViewById(R.id.filtre_amb_ingredient);
                    ambIngredient.setVisibility(View.VISIBLE);
                    LinearLayout senseIngredient = (LinearLayout) findViewById(R.id.filtre_sense_ingredient);
                    senseIngredient.setVisibility(View.INVISIBLE);

                } else {

                    LinearLayout perCategoria = (LinearLayout) findViewById(R.id.filtre_categoria);
                    perCategoria.setVisibility(View.INVISIBLE);
                    LinearLayout ambIngredient = (LinearLayout) findViewById(R.id.filtre_amb_ingredient);
                    ambIngredient.setVisibility(View.INVISIBLE);
                    LinearLayout senseIngredient = (LinearLayout) findViewById(R.id.filtre_sense_ingredient);
                    senseIngredient.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

    }

    public void filtratge(){
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String categoria = spinner.getSelectedItem().toString();
        resultat = dbManager.filtrarCategoria(categoria);
        if(resultat.size() > 0) {
            Intent intent = new Intent(this, ResultatFiltre.class);
            startActivity(intent);
        } else Toast.makeText(this, "No s'han trobat resultats.", Toast.LENGTH_SHORT).show();
    }

    public void filtratgeIngredient(){
        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        Ingredient ing = (Ingredient)spinner2.getSelectedItem();
        resultat = dbManager.filtrarIngredients(ing.getId());

        if(resultat.size() > 0) {
            Intent intent = new Intent(this, ResultatFiltre.class);
            startActivity(intent);
        } else Toast.makeText(this, "No s'han trobat resultats.", Toast.LENGTH_SHORT).show();
    }

    public void aplicar(View v) {
        Spinner spinner = (Spinner)findViewById(R.id.stipo);
        String tipo = spinner.getSelectedItem().toString();
        if(tipo == "Per Categoria") filtratge();
        else if(tipo == "Amb Ingredient") filtratgeIngredient();
        else filtratgeSenseIngredients();
    }

    public void seleccionarIngredients(View view)
    {
        if(dbManager.llegirIngredients().size() > 0) {
            Intent intent = new Intent(this, FiltreIngredients.class);
            startActivityForResult(intent, 40);
        } else Toast.makeText(Filtre.this, "No hi ha ingredients!", Toast.LENGTH_SHORT).show();
    }

    public void filtratgeSenseIngredients(){

        resultat = dbManager.filtrarSenseIngredients(llistaIngredients);

        if(resultat.size() > 0) {
            Intent intent = new Intent(this, ResultatFiltre.class);
            startActivity(intent);
        } else Toast.makeText(this, "No s'han trobat resultats.", Toast.LENGTH_SHORT).show();
    }
}
