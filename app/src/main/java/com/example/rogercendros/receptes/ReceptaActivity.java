package com.example.rogercendros.receptes;

import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ReceptaActivity extends ActionBarActivity {

    private DBManager dbManager;
    private ImageView imatge;
    private TextView titol;
    private TextView categoria;
    private TextView descripcio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepta);
        dbManager = new DBManager(this, null);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");

        Recepta recepta = dbManager.llegirReceptaPerId(id);

        titol = (TextView)findViewById(R.id.titol);
        categoria = (TextView)findViewById(R.id.categoria);
        descripcio = (TextView)findViewById(R.id.descripcio);
        imatge = (ImageView)findViewById(R.id.imatge);

        titol.setText(recepta.getTitol());
        categoria.setText(recepta.getCategoria());
        descripcio.setText(recepta.getDescripcio());
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
