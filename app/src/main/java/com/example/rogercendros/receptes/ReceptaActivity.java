package com.example.rogercendros.receptes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ReceptaActivity extends ActionBarActivity {

    private DBManager dbManager;
    private ImageView imatge;
    private TextView titol;
    private TextView categoria;
    private TextView descripcio;
    private TextView ingredients;
    private Recepta recepta;
    private String llista;

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
        llista = dbManager.getLlistaIngredients(id);

        titol = (TextView)findViewById(R.id.titol);
        categoria = (TextView)findViewById(R.id.categoria);
        descripcio = (TextView)findViewById(R.id.descripcio);
        ingredients = (TextView)findViewById(R.id.ingredients);
        imatge = (ImageView)findViewById(R.id.imatge);

        titol.setText(recepta.getTitol());
        categoria.setText(recepta.getCategoria());
        descripcio.setText(recepta.getDescripcio());
        ingredients.setText(llista);
        imatge.setImageResource(recepta.getImatge());
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
        llista = dbManager.getLlistaIngredients(recepta.getId());

        titol.setText(recepta.getTitol());
        categoria.setText(recepta.getCategoria());
        descripcio.setText(recepta.getDescripcio());
        ingredients.setText(llista);
        imatge.setImageResource(recepta.getImatge());
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
}
