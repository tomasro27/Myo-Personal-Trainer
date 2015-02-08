package hackatbrown.com.myopersonaltrainer;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class SlidingTabsFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsFragment";
    public static int actionBarHeight;


    /**
     * A custom ViewPager title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A ViewPager which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;


    public SlidingTabsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sliding_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new TabsPagerAdapter(getActivity().getSupportFragmentManager()));



        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        //mSlidingTabLayout.setCustomTabView(R.layout.custom_tab_title, R.id.tabtext);

        mSlidingTabLayout.setDividerColors(Color.parseColor("#EEEEEE"));
        TypedValue tv = new TypedValue();
        if (getActivity().getApplicationContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getActivity().getResources().getDisplayMetrics());
        }
        mSlidingTabLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarHeight));
        mSlidingTabLayout.setSelectedIndicatorColors(Color.parseColor("#d32f2f"));
        mSlidingTabLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mSlidingTabLayout.setCustomTabView(R.layout.tab_custom_layout, 0);

        mSlidingTabLayout.setViewPager(mViewPager);


    }



}
