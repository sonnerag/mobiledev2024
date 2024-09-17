package vn.edu.usth.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherAndForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
 


public class WeatherAndForecastFragment extends Fragment {



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WeatherAndForecastFragment() {
        // Required empty public constructor
    }

    // Existing method for creating an instance with parameters
    public static WeatherAndForecastFragment newInstance(String param1, String param2) {
        WeatherAndForecastFragment fragment = new WeatherAndForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // New method for creating an instance without parameters
    public static WeatherAndForecastFragment newInstance() {
        return new WeatherAndForecastFragment(); // No parameters needed
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmentweatherandforecast, container, false);
    }
}
