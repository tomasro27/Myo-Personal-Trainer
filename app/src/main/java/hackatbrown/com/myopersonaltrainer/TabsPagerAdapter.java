package hackatbrown.com.myopersonaltrainer;

/**
 * Created by tomasrodriguez on 1/27/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
 * The individual pages are simple and just display two lines of text. The important section of
 * this class is the  #getPageTitle(int) method which controls what is displayed in the
 * SlidingTabLayout.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final int[] ICONS = new int[] {
            R.drawable.myo_tab,
            R.drawable.dumbbell_tab,
            R.drawable.music_tab
    };

    /**
     * default constructor for the FragmentPagerAdapter
     * @param fm
     */
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return ICONS.length;
    }

    public int getDrawableId(int position) {
        return ICONS[position];
    }


    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return new MyoFragment();
            case 1: return new WorkoutsFragment();
            case 2: return new ProfileFragment();
        }

        return null;
    }



}
