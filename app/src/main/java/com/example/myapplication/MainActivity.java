package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private static final String ACCUWEATHER_API_KEY = "API_KEY";

    private TextView tvCityKey;
    private Spinner spForecastType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etCityName = findViewById(R.id.etCityName);
        Button btnGetCityKey = findViewById(R.id.btnGetCityKey);
        tvCityKey = findViewById(R.id.tvCityKey);
        spForecastType = findViewById(R.id.spForecastType);

        String[] options = {"1 zi", "5 zile"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spForecastType.setAdapter(adapter);

        btnGetCityKey.setOnClickListener(v -> {
            String cityName = etCityName.getText().toString().trim();
            if (!cityName.isEmpty()) {
                String selected = spForecastType.getSelectedItem().toString();
                String forecastType = "1day";
                if (selected.equals("5 zile")) {
                    forecastType = "5day";
                } else if (selected.equals("10 zile")) {
                    forecastType = "10day";
                }
                new FetchCityKeyTask().execute(cityName, ACCUWEATHER_API_KEY, forecastType);
            } else {
                Toast.makeText(MainActivity.this, "Vă rugăm să introduceți un oraș", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class FetchCityKeyTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String cityName = params[0];
            String apiKey = params[1];
            String forecastType = params[2];
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
                String encodedCity = URLEncoder.encode(cityName, "UTF-8");
                URL url = new URL("https://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + apiKey + "&q=" + encodedCity);
                
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                int responseCode = urlConnection.getResponseCode();
                InputStream inputStream;
                
                if (responseCode >= 200 && responseCode < 300) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }

                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                } else {
                    return null;
                }
            } catch (Exception e) {
                Log.e("AccuWeather", "Network error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return new String[]{response.toString(), forecastType};
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result == null || result[0].isEmpty()) {
                tvCityKey.setText("Eroare la obținerea codului orașului.");
                return;
            }

            String jsonResponse = result[0];
            String forecastType = result[1];

            try {
                Object json = new JSONTokener(jsonResponse).nextValue();

                if (json instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) json;
                    if (jsonArray.length() > 0) {
                        JSONObject cityObject = jsonArray.getJSONObject(0);
                        String key = cityObject.getString("Key");
                        String name = cityObject.getString("LocalizedName");
                        tvCityKey.setText("Oraș: " + name + "\nCod oraș: " + key + "\nSe preia prognoza...");
                        
                        new FetchWeatherTask().execute(key, ACCUWEATHER_API_KEY, forecastType);
                    } else {
                        tvCityKey.setText("Orașul nu a fost găsit.");
                    }
                } else if (json instanceof JSONObject) {
                    JSONObject errorObj = (JSONObject) json;
                    String message = errorObj.optString("Message", "Eroare API necunoscută");
                    tvCityKey.setText("Eroare API:\n" + message);
                }
            } catch (Exception e) {
                Log.e("AccuWeather", "Parsing error", e);
                tvCityKey.setText("Eroare procesare JSON oraș: " + e.getMessage());
            }
        }
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String locationKey = params[0];
            String apiKey = params[1];
            String forecastType = params[2];
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("https://dataservice.accuweather.com/forecasts/v1/daily/" + forecastType + "/" + locationKey + "?apikey=" + apiKey + "&metric=true");
                
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                int responseCode = urlConnection.getResponseCode();
                InputStream inputStream;
                
                if (responseCode >= 200 && responseCode < 300) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }

                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                }
            } catch (Exception e) {
                Log.e("AccuWeather", "Forecast network error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null || result.isEmpty()) {
                tvCityKey.append("\nEroare la obținerea prognozei.");
                return;
            }

            try {
                JSONObject root = new JSONObject(result);
                JSONArray dailyForecasts = root.getJSONArray("DailyForecasts");
                
                StringBuilder weatherInfo = new StringBuilder();
                for (int i = 0; i < dailyForecasts.length(); i++) {
                    JSONObject forecast = dailyForecasts.getJSONObject(i);
                    String date = forecast.optString("Date", "N/A").substring(0, 10);
                    JSONObject temperature = forecast.getJSONObject("Temperature");
                    
                    JSONObject min = temperature.getJSONObject("Minimum");
                    double minVal = min.getDouble("Value");
                    String minUnit = min.getString("Unit");

                    JSONObject max = temperature.getJSONObject("Maximum");
                    double maxVal = max.getDouble("Value");
                    String maxUnit = max.getString("Unit");

                    weatherInfo.append("\n\nData: ").append(date)
                               .append("\nTemp Min: ").append(minVal).append(" ").append(minUnit)
                               .append("\nTemp Max: ").append(maxVal).append(" ").append(maxUnit);
                }
                
                String currentText = tvCityKey.getText().toString();
                int loadingPos = currentText.indexOf("\nSe preia prognoza...");
                if (loadingPos != -1) {
                    currentText = currentText.substring(0, loadingPos);
                }
                tvCityKey.setText(currentText + weatherInfo.toString());

            } catch (Exception e) {
                Log.e("AccuWeather", "Forecast parsing error", e);
                tvCityKey.append("\nEroare la procesarea prognozei.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_beer_db) {
            startActivity(new Intent(this, BeerDatabaseActivity.class));
            return true;
        } else if (id == R.id.menu_second_activity) {
            startActivity(new Intent(this, SecondActivity.class));
            return true;
        } else if (id == R.id.menu_fourth_activity) {
            startActivity(new Intent(this, FourthActivity.class));
            return true;
        } else if (id == R.id.menu_beer_images) {
            startActivity(new Intent(this, BeerListActivity.class));
            return true;
        } else if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
