package vn.edu.usth.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Hcm extends Fragment {

    private TextView temperatureTextView;
    private TextView weatherConditionTextView;
    private ImageView logo;

    public static Hcm newInstance(String param1, String param2) {
        Hcm fragment = new Hcm();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hcm, container, false);

        temperatureTextView = view.findViewById(R.id.temperature1);
        weatherConditionTextView = view.findViewById(R.id.weather_condition1);
        logo = view.findViewById(R.id.logo);


        new FetchWeatherTask().execute();

        new DownloadImageTask().execute("https://usth.edu.vn/wp-content/uploads/2021/11/logo.png");

        return view;
    }

    private class FetchWeatherTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String apiKey = "90f7e255f3cd319f38bb29b0c45aa49f";
            String cityId = "1580578";
            String urlString = "https://api.openweathermap.org/data/2.5/forecast?id=" + cityId + "&appid=" + apiKey + "&units=metric";
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder builder = new StringBuilder();
                while (scanner.hasNext()) {
                    builder.append(scanner.nextLine());
                }
                scanner.close();
                inputStream.close();
                connection.disconnect();
                return builder.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray list = jsonResponse.getJSONArray("list");

                    JSONObject firstForecast = list.getJSONObject(0);
                    JSONObject main = firstForecast.getJSONObject("main");
                    double temperatureCelsius = main.getDouble("temp");

                    String weatherDescription = firstForecast.getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");

                    temperatureTextView.setText(String.format("%.2f°C", temperatureCelsius));
                    weatherConditionTextView.setText(weatherDescription);

                } catch (JSONException e) {
                    e.printStackTrace();
                    temperatureTextView.setText("Error parsing weather data");
                }
            } else {
                temperatureTextView.setText("Error fetching weather data");
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            try {
                InputStream inputStream = new URL(url).openStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                logo.setImageBitmap(result);
            } else {
                logo.setBackgroundResource(android.R.color.darker_gray);
            }
        }
    }
}
