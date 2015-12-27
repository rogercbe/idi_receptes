package com.example.rogercendros.receptes;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class IngredientsActivity extends ActionBarActivity {

    private ListView llista;
    private IngredientsAdapter adaptador;
    private DBManager dbManager;
    private List llistaIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        llistaIngredients = new ArrayList<Ingredient>();

        dbManager = new DBManager(this, null);

        llista = (ListView)findViewById(R.id.listView);
        adaptador = new IngredientsAdapter(this, dbManager.llegirIngredients());
        llista.setAdapter(adaptador);

        // Event on click de la llista
        llista.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Ingredient ingredient = (Ingredient) adaptador.getItem(position);
                        toggleIngredient(ingredient);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredients, menu);
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

    public void toggleIngredient(Ingredient ingredient) {
        if(llistaIngredients.contains(ingredient)) {
            llistaIngredients.remove(ingredient);
        } else {
            llistaIngredients.add(ingredient);
        }
    }
}
