package com.example.rogercendros.receptes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class FotosActivity extends ActionBarActivity {

    private GridView gv;
    private ArrayList<File> list;
    private FotosAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos);

        gv = (GridView) findViewById(R.id.grid);
        adaptador = new FotosAdapter(this);
        gv.setAdapter(adaptador);

        gv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Foto foto = (Foto) adaptador.getItem(position);
                        tornarImatge(foto.getIdDrawable());
                    }
                }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fotos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.galeria) {

            Intent pickImageIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageIntent.setType("image/*");
            pickImageIntent.putExtra("crop", "true");
            pickImageIntent.putExtra("outputX", 180);
            pickImageIntent.putExtra("outputY", 180);
            pickImageIntent.putExtra("aspectX", 1);
            pickImageIntent.putExtra("aspectY", 1);
            pickImageIntent.putExtra("scale", true);
            pickImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, "@drawable");
            pickImageIntent.putExtra("outputFormat",

            Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(pickImageIntent, 90);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 90) {
            if(resultCode == ReceptaActivity.RESULT_OK){
                data.getData();
            }
        }
    }

    public void tornarImatge(int id)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", String.valueOf(id));
        setResult(NovaRecepta.RESULT_OK,returnIntent);
        finish();
    }
}
