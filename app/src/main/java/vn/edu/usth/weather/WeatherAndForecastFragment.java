package vn.edu.usth.weather;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherAndForecastFragment extends Fragment {

    private TextView weatherTextView;
    private TextView weatherConditionTextView;

    public static WeatherAndForecastFragment newInstance(String param1, String param2) {
        WeatherAndForecastFragment fragment = new WeatherAndForecastFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentweatherandforecast, container, false);
        weatherTextView = view.findViewById(R.id.temperature2);
        weatherConditionTextView = view.findViewById(R.id.weather_condition2);

        ImageView logo = view.findViewById(R.id.logo);

        fetchWeatherForecast();
        fetchUSTHLogo(logo);

        return view;
    }

    private void fetchWeatherForecast() {
        String apiKey = "90f7e255f3cd319f38bb29b0c45aa49f";
        String cityId = "2988507";
        String url = "https://api.openweathermap.org/data/2.5/forecast?id=" + cityId + "&appid=" + apiKey + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray list = jsonResponse.getJSONArray("list");

                    JSONObject firstForecast = list.getJSONObject(0);
                    JSONObject main = firstForecast.getJSONObject("main");
                    double temperatureCelsius = main.getDouble("temp");

                    String weatherDescription = firstForecast.getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");

                    weatherTextView.setText(String.format("%.2f°C", temperatureCelsius));
                    weatherConditionTextView.setText(weatherDescription);

                } catch (JSONException e) {
                    e.printStackTrace();
                    weatherTextView.setText("Error parsing weather data");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherTextView.setText("Error fetching weather data");
            }
        });

        queue.add(request);
    }
    private void fetchUSTHLogo(ImageView logo) {
        String imageUrl = "https://usth.edu.vn/wp-content/uploads/2021/11/logo.png";
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        BitmapDrawable background = new BitmapDrawable(getResources(), response);
                        logo.setBackground(background);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(imageRequest);
    }
}
