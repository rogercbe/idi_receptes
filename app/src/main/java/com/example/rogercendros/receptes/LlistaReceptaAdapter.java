package com.example.rogercendros.receptes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class LlistaReceptaAdapter extends ArrayAdapter<Recepta> {

    public LlistaReceptaAdapter(Context context, List<Recepta> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItemView = convertView;

        if (null == convertView) {
            listItemView = inflater.inflate(
                    R.layout.list_item,
                    parent,
                    false);
        }

        // Crear instàncies dels elements
        TextView recepta = (TextView)listItemView.findViewById(R.id.text1);
        TextView categoria = (TextView)listItemView.findViewById(R.id.text2);
        ImageView imatge = (ImageView)listItemView.findViewById(R.id.imatge);

        Recepta item = getItem(position);

        // Assignació als elements
        recepta.setText(item.getTitol());
        categoria.setText(item.getCategoria());
        imatge.setImageResource(item.getImatge());

        return listItemView;

    }
}
