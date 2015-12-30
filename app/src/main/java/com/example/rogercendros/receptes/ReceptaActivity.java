package com.example.rogercendros.receptes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ReceptaActivity extends ActionBarActivity {

    private DBManager dbManager;
    private ImageView imatge;
    private TextView titol;
    private TextView categoria;
    private TextView descripcio;
    private Recepta recepta;
    private List llista;
    private List llistaAlternatius;
    private ListView listingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepta);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbManager = new DBManager(this, null);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");

        recepta = dbManager.llegirReceptaPerId(id);
        llista = dbManager.getIngredientsDeRecepta(id);
        llistaAlternatius = dbManager.llegirSubstitutsDeRecepta(id);

        listingredients = (ListView)findViewById(R.id.listView);
        titol = (TextView)findViewById(R.id.titol);
        categoria = (TextView)findViewById(R.id.categoria);
        descripcio = (TextView)findViewById(R.id.descripcio);
        imatge = (ImageView)findViewById(R.id.imatge);

        titol.setText(recepta.getTitol());
        categoria.setText(recepta.getCategoria());
        descripcio.setText(recepta.getDescripcio());
        imatge.setImageResource(recepta.getImatge());
        initAlternatius();
        initListView(id);
    }

    private void initListView(int id) {
        llista = dbManager.getIngredientsDeRecepta(id);
        ArrayAdapter<String> list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, llista);
        listingredients.setAdapter(list_adapter);
        ViewGroup.LayoutParams params = listingredients.getLayoutParams();
        params.height = llista.size() * 100;
        listingredients.setLayoutParams(params);
        listingredients.requestLayout();

        final int idRecepta = id;

        listingredients.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Ingredient ingredient = (Ingredient) listingredients.getItemAtPosition(position);
                        String substituts = dbManager.getSubstitutsDeIngredient(idRecepta, ingredient.getId());

                        AlertDialog alertDialog = new AlertDialog.Builder(ReceptaActivity.this).create();
                        alertDialog.setTitle("Ingredients alternatius");
                        alertDialog.setMessage(substituts);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "D'ACORD",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recepta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editar) {
            onButtonClick();
            return true;
        }
        if(id == R.id.esborrar) {
            suprimir();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick()
    {
        Intent i = new Intent(this, EditarRecepta.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", recepta.getId());
        i.putExtras(bundle);
        startActivityForResult(i, 8);
    }

    @Override
    public void onResume(){
        super.onResume();
        dbManager = new DBManager(this, null);

        recepta = dbManager.llegirReceptaPerId(recepta.getId());
        llista = dbManager.getIngredientsDeRecepta(recepta.getId());
        llistaAlternatius = dbManager.llegirSubstitutsDeRecepta(recepta.getId());

        titol.setText(recepta.getTitol());
        categoria.setText(recepta.getCategoria());
        descripcio.setText(recepta.getDescripcio());
        imatge.setImageResource(recepta.getImatge());
        initAlternatius();
        initListView(recepta.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 8) {
            if(resultCode == ReceptaActivity.RESULT_OK){
                Toast.makeText(ReceptaActivity.this, "S'ha actualitzat la recepta!", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == ReceptaActivity.RESULT_CANCELED) {
            }
        }
    }

    public void suprimir()
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Esborrar Recepta");
        adb.setMessage("Segur que vols esborrar aquesta recepta: "+recepta.getTitol()+"?");
        adb.setPositiveButton("D'ACORD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // esborrar ingredients de la recepta id
                dbManager.esborrarIngredientsDeRecepta(recepta.getId());
                // esborrar recepta id
                dbManager.esborrarRecepta(recepta.getId());
                // tornar amb resultat ok
                Intent returnIntent = new Intent();
                setResult(MainActivity.RESULT_OK, returnIntent);
                finish();
            }
        });
        adb.setNegativeButton("CANCEL·LAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } });
        adb.show();
    }

    public void initAlternatius()
    {
        Substitut s;
        String vell, nou;
        String llista = "";
        for(int i = 0; i < llistaAlternatius.size(); ++i) {
            s = (Substitut)llistaAlternatius.get(i);
            vell = dbManager.getIngredientById(s.getIdOriginal()).getNom();
            nou = dbManager.getIngredientById(s.getIdNou()).getNom();
            llista += nou + " pot substituir " + vell + "\n";
        }
    }
}
