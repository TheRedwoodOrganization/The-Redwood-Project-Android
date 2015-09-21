package be.redwood.the_redwood_project.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.fragments.DrawerFragment;

public class DrawerFragmentFactory {
    FragmentPagerAdapter adapterViewPager;

    static void createDrawerFragment(FragmentManager fragmentManager) {
        Fragment fragment = new DrawerFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer_fragment, fragment);
        fragmentTransaction.commit();
    }

}
