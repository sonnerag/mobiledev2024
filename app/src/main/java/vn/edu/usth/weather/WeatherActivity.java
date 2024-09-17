package vn.edu.usth.weather;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;







public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";
    private ViewPager pager;
    private TabLayout tabLayout;
    private HomeFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = findViewById(R.id.viewpage);
        tabLayout = findViewById(R.id.tab);

        setupViewPager(); // Set up the ViewPager with the adapter

        Log.i(TAG, "onCreate: Activity created");
    }

    private void setupViewPager() {
        adapter = new HomeFragmentPagerAdapter(getSupportFragmentManager(), getResources());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.trans) {
            switchLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchLanguage() {
        Locale currentLocale = getResources().getConfiguration().locale;
        String newLang = currentLocale.getLanguage().equals("fr") ? "en" : "fr";

        Locale locale = new Locale(newLang);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        getSharedPreferences("Settings", MODE_PRIVATE)
                .edit()
                .putString("Language", newLang)
                .apply();

        recreate(); // Recreate the activity to apply changes
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            Locale savedLocale = new Locale(getSharedPreferences("Settings", MODE_PRIVATE)
                    .getString("Language", "en"));
            overrideConfiguration.setLocale(savedLocale);
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Activity started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: Activity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: Activity paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: Activity stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Activity destroyed");
    }
}
