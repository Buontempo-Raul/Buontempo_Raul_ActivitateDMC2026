package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BeerListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        ListView lvBeerImages = findViewById(R.id.lvBeerImages);
        List<BeerImageInfo> beerImages = new ArrayList<>();

        beerImages.add(new BeerImageInfo(
                "https://la-grecu.ro/wp-content/uploads/2023/02/stellaartois.jpg",
                "Stella Artois - O bere blondă premium de origine belgiană.",
                "https://ro.wikipedia.org/wiki/Stella_Artois"
        ));
        beerImages.add(new BeerImageInfo(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR9soNbiYr339L9q-auppnHWJF-xpFpCnOKA&s",
                "Heineken - Una dintre cele mai populare beri lager din lume.",
                "https://heineken.com"
        ));
        beerImages.add(new BeerImageInfo(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvSOPMAk9EP3txQ39bpAAwAlXQzem0rQvnJw&s",
                "Corona Extra - Bere blondă mexicană, servită de obicei cu lămâie.",
                "https://en.wikipedia.org/wiki/Corona_(beer)"
        ));
        beerImages.add(new BeerImageInfo(
                "https://auchan.vtexassets.com/arquivos/ids/252962/bere-blonda-staropramen-05l-sgr.jpg?v=638404773926370000",
                "Staropramen - Bere cehă de calitate superioară din Praga.",
                "https://www.staropramen.com"
        ));
        beerImages.add(new BeerImageInfo(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8QG8WwuJ1XX-ltEVYvblbLOypBrs0eoIppQ&s",
                "Guinness - Bere neagră de tip stout, originară din Irlanda.",
                "https://www.guinness.com"
        ));

        BeerImageAdapter adapter = new BeerImageAdapter(this, beerImages);
        lvBeerImages.setAdapter(adapter);

        lvBeerImages.setOnItemClickListener((parent, view, position, id) -> {
            BeerImageInfo selectedBeer = beerImages.get(position);
            Intent intent = new Intent(BeerListActivity.this, WebViewActivity.class);
            intent.putExtra("WEB_URL", selectedBeer.getWebUrl());
            startActivity(intent);
        });
    }
}
