package com.rodev.flatyapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.rodev.flatyapp.R;
import com.rodev.flatyapp.adapter.ViewPagerAdapter;
import com.rodev.flatyapp.databinding.FragmentSavedBinding;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;
    private final TabConfigurator tabConfigurator = new TabConfigurator();

    public SavedFragment() {
        tabConfigurator.addFragment(R.string.fav_regions, new FavoriteRegionsFragment());
        tabConfigurator.addFragment(R.string.fav_flats, new FavoriteFlatsFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        ViewPagerAdapter pagerAdapter = tabConfigurator.createViewPagerAdapter(getActivity());
        binding.viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, tabConfigurator).attach();

        tabConfigurator.addTabsTo(binding.tabLayout);
    }

    private static class TabConfigurator implements TabLayoutMediator.TabConfigurationStrategy {

        private final ArrayList<FragmentTab> tabs = new ArrayList<>();

        @Override
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            tab.setText(tabs.get(position).titleResId);
        }

        public void addFragment(int tabTitleResId, Fragment fragment) {
            tabs.add(new FragmentTab(tabTitleResId, fragment));
        }

        public void addTabsTo(TabLayout tabLayout) {
            tabs.forEach(t -> tabLayout.newTab());
        }

        public ViewPagerAdapter createViewPagerAdapter(FragmentActivity activity) {
            return new ViewPagerAdapter(
                    activity,
                    tabs.stream()
                            .map(t -> t.fragment)
                            .collect(Collectors.toList())
            );
        }

    }

    private static class FragmentTab {
        public final int titleResId;
        public final Fragment fragment;

        private FragmentTab(int titleResId, Fragment fragment) {
            this.titleResId = titleResId;
            this.fragment = fragment;
        }
    }
}