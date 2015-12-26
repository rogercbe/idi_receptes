package com.example.rogercendros.receptes;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NovaRecepta extends Activity {

    private DBManager dbManager;
    private EditText titol;
    private EditText categoria;
    private EditText descripcio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_recepta);

        titol = (EditText)findViewById(R.id.titol);
        categoria = (EditText)findViewById(R.id.categoria);
        descripcio = (EditText)findViewById(R.id.descripcio);

        dbManager = new DBManager(this, null);
    }

    public void afegirRecepta(View view)
    {
        Recepta recepta = new Recepta();
        recepta.setTitol(titol.getText().toString());
        recepta.setCategoria(categoria.getText().toString());
        recepta.setDescripcio(descripcio.getText().toString());
        if(esValid()) {
            dbManager.afegirRecepta(recepta);
            netejarCamps();
        }
        else Toast.makeText(NovaRecepta.this, "Tots els camps són obligatoris!", Toast.LENGTH_SHORT).show();
    }

    public void tancar(View view)
    {
        finish();
    }

    public void netejarCamps()
    {
        titol.setText("");
        categoria.setText("");
        descripcio.setText("");
    }

    public boolean esValid()
    {
        if(titol.getText().toString().isEmpty() || categoria.getText().toString().isEmpty() || descripcio.getText().toString().isEmpty()) return false;
        return true;
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
}
