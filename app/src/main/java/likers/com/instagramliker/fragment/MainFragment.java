package likers.com.instagramliker.fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import likers.com.instagramliker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    View rootView;
    ViewPager pager;
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        pager= (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        CustomPagerAdapter viewPagerAdapter = new CustomPagerAdapter(getChildFragmentManager());
        pager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(pager);
        return rootView;

    }


    class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new FollowingFragment();

                case 1:
                    return new FollowersFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        public CharSequence getPageTitle(int pos)
        {
            switch (pos)
            {
                case 0:
                    return "Following";
                case 1:
                    return "Followers";
                default:
                    return null;
            }
        }
    }
}
