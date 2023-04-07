package hcmute.edu.vn.phamdinhquochoa.flatyapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments;

    public ViewPagerAdapter(@NonNull FragmentActivity fragment, List<Fragment> fragments) {
        super(fragment);
        this.fragments = fragments;
    }

    public static ViewPagerAdapter of(FragmentActivity activity, Fragment... fragments) {
        return new ViewPagerAdapter(activity, new ArrayList<>(Arrays.asList(fragments)));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
