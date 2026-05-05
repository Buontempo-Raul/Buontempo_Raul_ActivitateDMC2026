package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BeerImageAdapter extends BaseAdapter {
    private Context context;
    private List<BeerImageInfo> beerList;
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public BeerImageAdapter(Context context, List<BeerImageInfo> beerList) {
        this.context = context;
        this.beerList = beerList;
    }

    @Override
    public int getCount() {
        return beerList.size();
    }

    @Override
    public Object getItem(int position) {
        return beerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // În BeerImageAdapter.java, am adăugat un check pentru a nu re-descărca dacă imaginea există deja
// și am folosit un ViewHolder simplu (opțional, dar recomandat).

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_beer_image, parent, false);
        }

        BeerImageInfo beer = beerList.get(position);
        ImageView ivBeer = convertView.findViewById(R.id.ivBeer);
        TextView tvBeerDescription = convertView.findViewById(R.id.tvBeerDescription);

        tvBeerDescription.setText(beer.getDescription());

        // Setăm un tag pentru a preveni afișarea imaginii greșite pe un item reciclat
        ivBeer.setTag(beer.getImageUrl());
        ivBeer.setImageResource(android.R.drawable.ic_menu_report_image);

        executorService.execute(() -> {
            Bitmap bitmap = downloadImage(beer.getImageUrl());
            if (bitmap != null) {
                mainHandler.post(() -> {
                    // Verificăm dacă ImageView-ul mai vrea această imagine (pentru itemi reciclați)
                    if (ivBeer.getTag().equals(beer.getImageUrl())) {
                        ivBeer.setImageBitmap(bitmap);
                    }
                });
            }
        });

        return convertView;
    }

    private Bitmap downloadImage(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
