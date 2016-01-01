package com.example.rogercendros.receptes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EditIngredientsActivity extends ActionBarActivity {

    private ListView llista;
    private EditIngredientsAdapter adaptador;
    private DBManager dbManager;
    private List llistaIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        llistaIngredients = EditarRecepta.llistaIngredients;

        dbManager = new DBManager(this, null);

        llista = (ListView)findViewById(R.id.listView);
        adaptador = new EditIngredientsAdapter(this, dbManager.llegirIngredients());
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

        // mantenir apretat
        llista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Ingredient ingredient = (Ingredient) adaptador.getItem(pos);
                esborrarIngredient(ingredient);
                return true;
            }
        });
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

        if (id == R.id.afegir) {
            afegirIngredient();
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

    public void seleccionar(View view)
    {
        Intent returnIntent = new Intent();
        setResult(NovaRecepta.RESULT_OK, returnIntent);
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

    private void esborrarIngredient(final Ingredient ingredient) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Esborrar Ingredient");
        adb.setMessage("Segur que vols esborrar aquest ingredient: "+ingredient.getNom()+"?");
        adb.setPositiveButton("D'ACORD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // esborrar ingredient
                // tornar amb resultat ok
                dbManager.esborrarIngredient(ingredient.getId());
                redrawLlista();
                Toast.makeText(EditIngredientsActivity.this, "Has esborrat l'ingredient " +ingredient.getNom(), Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNegativeButton("CANCEL·LAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } });
        adb.show();
    }

    private void afegirIngredient() {
        final EditText txtIngredient = new EditText(this);
        InputFilter[] fa= new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(20);
        txtIngredient.setFilters(fa);

        new AlertDialog.Builder(this)
                .setTitle("Afegir Ingredient")
                .setMessage("Nom de l'Ingredient:")
                .setView(txtIngredient)
                .setPositiveButton("Afegir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String ingredient = txtIngredient.getText().toString();
                        if (ingredientValid(ingredient)) {
                            int nou_ing = dbManager.afegirIngredient(ingredient);
                            Ingredient nou = dbManager.getIngredientById(nou_ing);
                            EditarRecepta.llistaIngredients.add(nou);
                            Toast.makeText(EditIngredientsActivity.this, "Has afegit " + ingredient + " a la llista!", Toast.LENGTH_SHORT).show();
                            redrawLlista();
                        }
                    }
                })
                .setNegativeButton("Cancel·lar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void redrawLlista() {
        llista = (ListView)findViewById(R.id.listView);
        adaptador = new EditIngredientsAdapter(this, dbManager.llegirIngredients());
        llista.setAdapter(adaptador);
    }

    public boolean ingredientValid(String ingredient){
        //es valid si no es buit i no esta a la base de dades
        if(!ingredient.isEmpty()) {
            if (!dbManager.getIngredientByNom(ingredient)) {
                return true;
            }  Toast.makeText(EditIngredientsActivity.this, "Aquest ingredient ja està disponible!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(EditIngredientsActivity.this, "Aquest camp és obligatori!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        llista = (ListView)findViewById(R.id.listView);
        adaptador = new EditIngredientsAdapter(this, dbManager.llegirIngredients());
        llista.setAdapter(adaptador);
    }
}
