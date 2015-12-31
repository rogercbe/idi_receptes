package com.example.rogercendros.receptes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ResultatFiltre extends ActionBarActivity {

    private ListView list;
    private ArrayAdapter adaptador;
    private DBManager dbManager;
    private List receptes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_filtre);

        dbManager = new DBManager(this, null);

        receptes = Filtre.resultat;

        list = (ListView)findViewById(R.id.llista);
        //adaptador = new LlistaReceptaAdapter(this, DataSource.receptes);
        adaptador = new LlistaReceptaAdapter(this, receptes);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultat_filtre, menu);
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

    public void obrirDetall(int id)
    {
        Intent i = new Intent(this, ReceptaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        i.putExtras(bundle);
        startActivity(i);
    }
}
