package com.example.rogercendros.receptes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

class IngredientsAdapter extends ArrayAdapter<Ingredient> {

    public IngredientsAdapter(Context context, List<Ingredient> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItemView = convertView;

        if (null == convertView) {
            listItemView = inflater.inflate(
                    R.layout.ingredient_list,
                    parent,
                    false);
        }

        // Crear instàncies dels elements
        CheckBox txtIngredient = (CheckBox)listItemView.findViewById(R.id.checkbox);
        Ingredient ingredient = getItem(position);
        txtIngredient.setText(ingredient.getNom());

        return listItemView;
    }
}
