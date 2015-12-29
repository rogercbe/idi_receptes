package com.example.rogercendros.receptes;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class IngredientSubstitut extends ActionBarActivity {

    private DBManager dbManager;
    private Spinner original;
    private Spinner substitut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_substitut);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        original = (Spinner)findViewById(R.id.original);
        substitut = (Spinner)findViewById(R.id.substitut);

        dbManager = new DBManager(this, null);

        initSpinnerIngredients();
        initSpinnerIngredientsRecepta();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredient_substitut, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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
        Spinner sItems = (Spinner) findViewById(R.id.substitut);
        sItems.setAdapter(adapter);
    }

    public void initSpinnerIngredientsRecepta()
    {
        // get llista tots els ingredients
        List ingredients = NovaRecepta.llistaIngredients;
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
        Spinner sItems = (Spinner) findViewById(R.id.original);
        sItems.setAdapter(adapter);
    }

    public void afegirSubstitut(View v)
    {
        Ingredient ori = (Ingredient)original.getSelectedItem();
        Ingredient nou = (Ingredient)substitut.getSelectedItem();
        Substitut s = new Substitut(ori.getId(), nou.getId());
        if(!NovaRecepta.llistaSubstituts.contains(s)) {
            NovaRecepta.llistaSubstituts.add(s);
            Toast.makeText(IngredientSubstitut.this, "Afegit "+nou.getNom()+" com a substitut de "+ori.getNom()+"!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(IngredientSubstitut.this, nou.getNom() + " ja substitueix a " + ori.getNom() +"!", Toast.LENGTH_SHORT).show();
        // mostrar en una llista les substitucions
    }
}
