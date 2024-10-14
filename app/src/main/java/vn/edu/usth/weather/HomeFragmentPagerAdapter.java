package vn.edu.usth.weather;

import android.content.res.Resources;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String[] titles;

    public HomeFragmentPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);

        // Retrieve the actual string values from the resources
        titles = new String[] {
                resources.getString(R.string.title_hanoi),  // "Hanoi" from strings.xml
                resources.getString(R.string.title_hcm),    // "HCM" from strings.xml
                resources.getString(R.string.title_paris)   // "Paris" from strings.xml
        };
    }

    @Override
    public int getCount() {
        return PAGE_COUNT; // number of pages for a ViewPager
    }

    @Override
    public Fragment getItem(int page) {
        switch (page) {
            case 0:
                return Hanoi.newInstance(titles[0], "Details about Hanoi");
            case 1:
                return Hcm.newInstance(titles[1], "Details about HCM");
            case 2:
                return WeatherAndForecastFragment.newInstance(titles[2], "Details about Paris");
        }
        return new EmptyFragment(); // failsafe
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Call the super method to ensure the fragment is instantiated correctly
        return super.instantiateItem(container, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position]; // returns the translated tab title
    }
}
