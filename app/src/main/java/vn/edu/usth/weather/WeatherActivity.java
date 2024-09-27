package vn.edu.usth.weather;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;


public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";
    private ViewPager pager;
    private TabLayout tabLayout;
    private HomeFragmentPagerAdapter adapter;
    private MediaPlayer mediaPlayer;
    private String mp3file = "streammusique.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            copyMp3ToSDCard();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = findViewById(R.id.viewpage);
        tabLayout = findViewById(R.id.tab);
        setupViewPager();

        Log.i(TAG, "onCreate: Activity created");
    }

    private void copyMp3ToSDCard() {
        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard, mp3file);


        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(this, "External storage is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.streammusique);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
            Log.i(TAG, "MP3 file copied to: " + file.getAbsolutePath());
            Toast.makeText(this, "MP3 file copied to SD card", Toast.LENGTH_SHORT).show();

            if (file.exists()) {
                playMp3(file.getAbsolutePath());
            } else {
                Log.e(TAG, "File does not exist at path: " + file.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to copy MP3 file", Toast.LENGTH_SHORT).show();
        }
    }

    private void playMp3(String filePath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.i(TAG, "MP3 is playing from SD card");

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error playing MP3 from SD card", e);
        }

        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            Log.i(TAG, "MediaPlayer: Music playback completed and resources released");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        Log.i(TAG, "onDestroy: Activity destroyed");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                copyMp3ToSDCard();  // Call this again if permission is granted
            } else {
                Toast.makeText(this, "Permission denied to write to external storage", Toast.LENGTH_SHORT).show();
            }
        }
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

        // Handle the 'refresh' menu item
        if (item.getItemId() == R.id.refresh) {
            Toast.makeText(this, "Refreshed!", Toast.LENGTH_SHORT).show();
            return true;
        }

        // Handle the 'settings' menu item
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivity(intent);
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

}