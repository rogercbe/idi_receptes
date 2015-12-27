package com.example.rogercendros.receptes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FotosAdapter extends BaseAdapter {
    private Context context;

    public FotosAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Foto.ITEMS.length;
    }

    @Override
    public Foto getItem(int position) {
        return Foto.ITEMS[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, viewGroup, false);
        }

        ImageView foto = (ImageView) view.findViewById(R.id.foto);

        final Foto item = getItem(position);
        foto.setImageResource(item.getIdDrawable());

        return view;
    }

}
