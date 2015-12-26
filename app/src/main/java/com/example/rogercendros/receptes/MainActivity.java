package com.example.rogercendros.receptes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ListView list;
    private ArrayList receptes;
    private ArrayAdapter adaptador;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this, null);

        // Carregar la llista
        list = (ListView)findViewById(R.id.llista);
        //adaptador = new LlistaReceptaAdapter(this, DataSource.receptes);
        adaptador = new LlistaReceptaAdapter(this, dbManager.llegirReceptes());
        list.setAdapter(adaptador);

        // Event on click de la llista
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String recepta = String.valueOf(parent.getItemAtPosition(position));
                        Recepta recepta = (Recepta) adaptador.getItem(position);
                        //Toast.makeText(MainActivity.this, recepta.getId(), Toast.LENGTH_SHORT).show();
                        obrirDetall(recepta.getId());
                    }
                }
        );
    }

    public void onButtonClick(View view)
    {
        Intent intent = new Intent(this, NovaRecepta.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    // Reescriure la llista cada cop que tornem
    @Override
    public void onResume(){
        super.onResume();
        dbManager = new DBManager(this, null);

        // Carregar la llista
        list = (ListView)findViewById(R.id.llista);
        //adaptador = new LlistaReceptaAdapter(this, DataSource.receptes);
        adaptador = new LlistaReceptaAdapter(this, dbManager.llegirReceptes());
        list.setAdapter(adaptador);
    }

    // Mostrar Toast si canviem l'estat de la llista
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == MainActivity.RESULT_OK){
                Toast.makeText(MainActivity.this, "Afegida una nova recepta!", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == MainActivity.RESULT_CANCELED) {
            }
        }
    }

    public void obrirDetall(int id)
    {
        Intent i = new Intent(this, ReceptaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        i.putExtras(bundle);
        startActivity(i);
    }
}
