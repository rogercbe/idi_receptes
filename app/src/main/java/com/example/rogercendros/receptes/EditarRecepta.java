package com.example.rogercendros.receptes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EditarRecepta extends ActionBarActivity {

    private DBManager dbManager;
    private ImageView imatge;
    private EditText titol;
    private Spinner categoria;
    private EditText descripcio;
    private int idDrawable;
    private Recepta recepta;
    public static List llistaIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_recepta);

        inicialitzarSpinner();

        dbManager = new DBManager(this, null);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");

        recepta = dbManager.llegirReceptaPerId(id);
        llistaIngredients = dbManager.llegirIngredientsDeRecepta(id);
        //llistaIngredients = new ArrayList<Ingredient>();

        titol = (EditText)findViewById(R.id.titol);
        categoria = (Spinner)findViewById(R.id.categoria);
        descripcio = (EditText)findViewById(R.id.descripcio);
        imatge = (ImageView)findViewById(R.id.imatge);

        titol.setText(recepta.getTitol());
        categoria.setSelection(((ArrayAdapter<String>)categoria.getAdapter()).getPosition(recepta.getCategoria()));
        descripcio.setText(recepta.getDescripcio());
        imatge.setImageResource(recepta.getImatge());
        idDrawable = recepta.getImatge();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar_recepta, menu);
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

    public void actualitzarRecepta(View view)
    {
        recepta.setTitol(titol.getText().toString());
        recepta.setCategoria(categoria.getSelectedItem().toString());
        recepta.setDescripcio(descripcio.getText().toString());
        recepta.setImatge(idDrawable);
        if(esValid()) {
            dbManager.actualitzarRecepta(recepta);
            dbManager.esborrarIngredientsDeRecepta(recepta.getId());
            dbManager.afegirIngredientsARecepta(recepta.getId(), llistaIngredients);
            Intent returnIntent = new Intent();
            setResult(ReceptaActivity.RESULT_OK, returnIntent);
            finish();
        }
        else Toast.makeText(EditarRecepta.this, "Tots els camps són obligatoris!", Toast.LENGTH_SHORT).show();
    }

    public boolean esValid()
    {
        if(titol.getText().toString().isEmpty() || categoria.getSelectedItem().toString().isEmpty() || descripcio.getText().toString().isEmpty()) return false;
        return true;
    }

    public void seleccionarFoto(View view)
    {
        Intent intent = new Intent(this, FotosActivity.class);
        startActivityForResult(intent, 6);
    }

    public void seleccionarIngredients(View view)
    {
        Intent intent = new Intent(this, EditIngredientsActivity.class);
        startActivityForResult(intent, 15);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 6) {
            if(resultCode == MainActivity.RESULT_OK){
                String result = data.getStringExtra("result");
                idDrawable = Integer.parseInt(result);
                imatge.setImageResource(idDrawable);
            }
            if (resultCode == MainActivity.RESULT_CANCELED) {
            }
        }
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
        Spinner sItems = (Spinner) findViewById(R.id.categoria);
        sItems.setAdapter(adapter);
    }

}
