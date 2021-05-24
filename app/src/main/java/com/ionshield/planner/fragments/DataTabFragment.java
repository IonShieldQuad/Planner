package com.ionshield.planner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ionshield.planner.R;
import com.ionshield.planner.fragments.list.ListItemFragment;
import com.ionshield.planner.fragments.list.ListTypeFragment;
import com.ionshield.planner.fragments.list.ListTypeItemRefFragment;
import com.ionshield.planner.fragments.list.Mode;

public class DataTabFragment extends Fragment {
    private String[] titles;
    private DataTabViewModel mModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.data_tab_fragment, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mModel = new ViewModelProvider(requireActivity()).get(DataTabViewModel.class);

        DataPagerAdapter tabPager = new DataPagerAdapter(requireActivity());
        if (getView() != null) {
            titles = getResources().getStringArray(R.array.data_titles);

            ViewPager2 viewPager = getView().findViewById(R.id.tab_pager);
            viewPager.setAdapter(tabPager);
            // Display a tab for each Fragment displayed in ViewPager.
            TabLayout tabLayout = getView().findViewById(R.id.tab_layout);
            //tabLayout.setupWithViewPager(viewPager);
            new TabLayoutMediator(tabLayout, viewPager, (tab, pos) -> tab.setText(titles == null ? "" : titles[pos])).attach();

            mModel.getPage().observe(getViewLifecycleOwner(), page -> {
                if (viewPager.getCurrentItem() != page) {
                    //TODO: FIX THIS THING (BROKEN)
                    viewPager.setCurrentItem(page, false);
                }
            });

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    mModel.setPage(position);
                }

            });



        }

    }



    static class DataPagerAdapter extends FragmentStateAdapter {
        public DataPagerAdapter(FragmentActivity fa) {
            super(fa);
        }




        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0:
                    return ListItemFragment.newInstance(1, Mode.EDIT, false);
                case 1:
                    return ListTypeFragment.newInstance(1, Mode.EDIT, false);
                case 2:
                    return ListTypeItemRefFragment.newInstance(1, Mode.EDIT, false);
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
