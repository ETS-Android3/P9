package com.openclassrooms.realestatemanager.ui;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Display;
import android.view.Menu;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertydetail.view.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.ui.propertylist.view.PropertyListFragment;
import com.openclassrooms.realestatemanager.utils.LandscapeHelper;

public class MainActivity extends AppCompatActivity implements PropertyListFragment.OnPropertySelectedListener,
        PropertyListFragment.OnAddPropertyListener, PropertyDetailFragment.OnEditPropertyListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_propertyListFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        logScreen();
    }

    private void logScreen(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d(Tag.TAG, String.format("logScreen(). width = %d, height = %d", width, height));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * burger menu can open drawer
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onPropertySelectedClicked(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.onPropertySelectedClicked() called with: propertyId = [" + propertyId + "]");
        if (LandscapeHelper.isLandscape()) {
            PropertyDetailFragment propertyDetailFragment = PropertyDetailFragment.newInstance(propertyId);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_view, propertyDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            Bundle bundle = new Bundle();
            bundle.putLong("property_id_arg", propertyId);
            navController.navigate(R.id.action_nav_propertyListFragment_to_nav_propertyDetailFragment, bundle);
        }
    }


    @Override
    public void onAddPropertyClicked() {
        Log.d(Tag.TAG, "MainActivity.onAddPropertyCLicked() called");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.property_edit_title_create));
        navController.navigate(R.id.action_nav_propertyListFragment_to_nav_propertyEditFragment, bundle);
    }

    @Override
    public void onEditPropertyClicked(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.onEditPropertyClicked() called with: propertyId = [" + propertyId + "]");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        Bundle bundle = new Bundle();
        bundle.putLong("property_id_arg", propertyId);
        bundle.putString("title", getString(R.string.property_edit_title_modify));
        navController.navigate(R.id.action_nav_propertyDetailFragment_to_nav_propertyEditFragment, bundle);
    }
}