package com.ionshield.planner.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ionshield.planner.PlannerApplication;
import com.ionshield.planner.R;
import com.ionshield.planner.fragments.DataTabFragment;
import com.ionshield.planner.fragments.MapFragment;
import com.ionshield.planner.fragments.PlanMenuFragment;

public class MainActivity extends AppCompatActivity {
    private int itemId;
    //private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(item -> {

            itemId = item.getItemId();

            if (item.getItemId() == R.id.item_plan_menu) {
                PlanMenuFragment fragment = new PlanMenuFragment();
                showTopLevelFragment(fragment);
                return true;
            }

            if (item.getItemId() == R.id.item_map) {
                MapFragment fragment = new MapFragment();
                showTopLevelFragment(fragment);
                return true;
            }

            if (item.getItemId() == R.id.item_data) {
                //ListItemFragment fragment = new ListItemFragment(Mode.EDIT);
                showTopLevelFragment(new DataTabFragment());
                //fragment.setMode(Mode.SELECT_EDIT);
                //this.fragment = fragment;
                return true;
            }

            return false;
        });

        showTopLevelFragment(new PlanMenuFragment());

        /*Item item = new Item();
        item.itemId = 0;
        item.name = "test";
        PlannerRepository.getInstance().getItemDao().insertAll(item);*/
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("bottom_nav_item", itemId);
        //getSupportFragmentManager().putFragment(outState, "current_fragment", fragment);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /*fragment = getSupportFragmentManager().getFragment(savedInstanceState, "current_fragment");
        if (fragment != null) {
            showTopLevelFragment(fragment);
        }*/
        itemId = savedInstanceState.getInt("bottom_nav_item", 0);
        if (itemId != 0) {
            BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(itemId);
            }
        }
    }

    private void showTopLevelFragment(Fragment fragment) {
        // Use the fragment manager to dynamically change the fragment displayed in the FrameLayout.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }*/
        if (item.getItemId() == R.id.action_dark_mode) {
            PlannerApplication.getApplication().toggleDarkMode();
        }
        return true;
    }



    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) {
                planId = data.getIntExtra("value", -1);
                updateText();
            }
        }
    }*/


}