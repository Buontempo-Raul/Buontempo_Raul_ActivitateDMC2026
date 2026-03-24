package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class BeerAdapter extends ArrayAdapter<Bere> {

    public BeerAdapter(@NonNull Context context, @NonNull List<Bere> objects) {
        super(context, R.layout.item_beer, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_beer, parent, false);
        }

        Bere bere = getItem(position);

        if (bere != null) {
            TextView tvNume = convertView.findViewById(R.id.tvItemNume);
            TextView tvDetaliu = convertView.findViewById(R.id.tvItemDetaliu);
            TextView tvRating = convertView.findViewById(R.id.tvItemRating);

            tvNume.setText(bere.getNume());
            String detaliu = "Tip: " + bere.getTip() + " | " + bere.getCantitate() + "ml";
            tvDetaliu.setText(detaliu);
            tvRating.setText("Rating: " + bere.getRating());
        }

        return convertView;
    }
}
