package com.example.rogercendros.receptes;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

        if (dbManager.llegirIngredients().size() == 0) dbManager.seedIngredients();

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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        if (id == R.id.buscar) {
            buscar();
            return true;
        }

        if (id == R.id.help) {
            help();
            return true;
        }

        if (id == R.id.about) {
            about();
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
        if (requestCode == 20) {
            if(resultCode == MainActivity.RESULT_OK){
                Toast.makeText(MainActivity.this, "S'ha eliminat la recepta!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void obrirDetall(int id)
    {
        Intent i = new Intent(this, ReceptaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        i.putExtras(bundle);
        startActivityForResult(i, 20);
    }

    public void about(View view)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void about()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("About");
        alertDialog.setMessage("'Receptari' és un llibre de receptes virtual per guardar les teves receptes preferides i poder accedir a elles fàcilment.\n\nAquesta aplicació és un projecte per l'assignatura d'IDI de la FIB. \n\nAutor: Roger Cendrós.\nAquesta és la versió 1.0.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "D'ACORD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void help()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Ajuda");
        alertDialog.setMessage("Aquesta aplicació permet gestionar un llibre de receptes virtual.\n\nPots afegir noves receptes prement el botó '+' de la pàgina principal. " +
                "Si necessites visualitzar-les en detall, editar-les o esborrar-les o fins i tot substituir ingredients d'una recepta, podràs fer ràpidament prement a una recepta de la llista.\n\nFinalment pots utilitzar el buscador del menú" +
                " per filtrar totes les receptes i buscar la que més t'interessi aplicant els filtres convenients!\n\n" +
                "Fàcil, oi?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "GRÀCIES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void buscar()
    {

    }
}
