package com.example.rogercendros.receptes;

import android.app.Activity;
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
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class NovaRecepta extends Activity {

    private DBManager dbManager;
    private EditText titol;
    private Spinner categoria;
    private EditText descripcio;
    private ImageView imatge;
    private int idDrawable;
    private SpinnerAdapter adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_recepta);

        idDrawable = R.drawable.defecte;
        titol = (EditText)findViewById(R.id.titol);
        categoria = (Spinner)findViewById(R.id.categoria);
        descripcio = (EditText)findViewById(R.id.descripcio);
        imatge = (ImageView)findViewById(R.id.imatge);
        imatge.setImageResource(idDrawable);

        inicialitzarSpinner();

        dbManager = new DBManager(this, null);
    }

    public void afegirRecepta(View view)
    {
        Recepta recepta = new Recepta();
        recepta.setTitol(titol.getText().toString());
        recepta.setCategoria(categoria.getSelectedItem().toString());
        recepta.setDescripcio(descripcio.getText().toString());
        recepta.setImatge(idDrawable);
        if(esValid()) {
            dbManager.afegirRecepta(recepta);
            Intent returnIntent = new Intent();
            setResult(MainActivity.RESULT_OK, returnIntent);
            finish();
        }
        else Toast.makeText(NovaRecepta.this, "Tots els camps són obligatoris!", Toast.LENGTH_SHORT).show();
    }

    public boolean esValid()
    {
        if(titol.getText().toString().isEmpty() || categoria.getSelectedItem().toString().isEmpty() || descripcio.getText().toString().isEmpty()) return false;
        return true;
    }

    public void seleccionarFoto(View view)
    {
        Intent intent = new Intent(this, FotosActivity.class);
        startActivityForResult(intent, 5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nova_recepta, menu);
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
        Spinner sItems = (Spinner) findViewById(R.id.categoria);
        sItems.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5) {
            if(resultCode == MainActivity.RESULT_OK){
                String result = data.getStringExtra("result");
                idDrawable = Integer.parseInt(result);
                imatge.setImageResource(idDrawable);
            }
            if (resultCode == MainActivity.RESULT_CANCELED) {
            }
        }
    }
}
