package com.example.rogercendros.receptes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class FiltreIngredients extends ActionBarActivity {

    private ListView llista;
    private FiltreIngredientAdapter adaptador;
    private DBManager dbManager;
    private List llistaIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre_ingredients);

        llistaIngredients = Filtre.llistaIngredients;

        dbManager = new DBManager(this, null);

        llista = (ListView)findViewById(R.id.listView);
        adaptador = new FiltreIngredientAdapter(this, dbManager.llegirIngredients());
        llista.setAdapter(adaptador);

        View v = llista.getAdapter().getView(1, null, null);
        CheckBox checkbox = (CheckBox)v.findViewById(R.id.checkbox);
        checkbox.setChecked(true);

        // Event on click de la llista
        llista.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Ingredient ingredient = (Ingredient) adaptador.getItem(position);
                        toggleIngredient(ingredient);
                        if (view != null) {
                            CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox);
                            checkBox.setChecked(!checkBox.isChecked());
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filtre_ingredients, menu);
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

    public void toggleIngredient(Ingredient ingredient) {
        if(llistaIngredients.contains(ingredient)) {
            llistaIngredients.remove(ingredient);
        } else {
            llistaIngredients.add(ingredient);
        }
    }

    public void seleccionar(View view)
    {
        Intent returnIntent = new Intent();
        setResult(Filtre.RESULT_OK, returnIntent);
        finish();
    }

    public void inicialitzarChecks()
    {
        View v;
        CheckBox checkbox;
        for (int i = 0; i < llista.getCount(); i++) {
            v = llista.getAdapter().getView(i, null, null);
            checkbox = (CheckBox)v.findViewById(R.id.checkbox);

            Ingredient ing = (Ingredient) adaptador.getItem(i);

            if(llistaIngredients.contains(ing)) {
                checkbox.setChecked(true);
                adaptador.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        llista = (ListView)findViewById(R.id.listView);
        adaptador = new FiltreIngredientAdapter(this, dbManager.llegirIngredients());
        llista.setAdapter(adaptador);
    }
}
