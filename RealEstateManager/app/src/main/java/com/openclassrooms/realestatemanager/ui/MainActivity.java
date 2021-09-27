package com.openclassrooms.realestatemanager.ui;



import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertydetail.view.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.ui.propertylist.view.PropertyListFragment;
import com.openclassrooms.realestatemanager.utils.LandscapeHelper;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements PropertyListFragment.OnPropertySelectedListener,
        PropertyListFragment.OnAddPropertyListener, PropertyDetailFragment.OnEditPropertyListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private static final int LOAD_FIRST_PROPERTY = -1;

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
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            landscape();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private void landscape(){
        if (LandscapeHelper.isLandscape()) {
            //Fragment mainFragment = getFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            PropertyDetailFragment propertyDetailFragment = PropertyDetailFragment.newInstance(LOAD_FIRST_PROPERTY);

            Fragment detailFragment = getFragmentManager().findFragmentById(R.id.fragment_container_view);
            if (detailFragment == null) {
                // screen rotation can come with null fragment
                Log.d(Tag.TAG, "*** landscape add 1");
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                Bundle bundle = new Bundle();
                bundle.putLong("property_id_arg", LOAD_FIRST_PROPERTY);
                navController.navigate(R.id.nav_propertyListFragment, bundle);

                Log.d(Tag.TAG, "*** landscape add 2");
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container_view, propertyDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void onPropertySelectedClicked(long propertyId) {
        Log.d(Tag.TAG, "*** MainActivity.onPropertySelectedClicked() called with: propertyId = [" + propertyId + "]");
        if (LandscapeHelper.isLandscape()) {
            PropertyDetailFragment propertyDetailFragment = PropertyDetailFragment.newInstance(propertyId);

            Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container_view);
            if (fragment == null) {
                // screen rotation can come with null fragment
                Log.d(Tag.TAG, "*** onPropertySelectedClicked() add 1");
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//                Bundle bundle = new Bundle();
//                bundle.putLong("property_id_arg", propertyId);
                navController.navigate(R.id.nav_propertyListFragment);

                Log.d(Tag.TAG, "*** onPropertySelectedClicked() add 2");
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container_view, propertyDetailFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Log.d(Tag.TAG, "*** onPropertySelectedClicked() replace ");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, propertyDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
            //Fragment mainFragment = getFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            //Fragment detailFragment = mainFragment.getChildFragmentManager().findFragmentById(R.id.fragment_container_view);

/*            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
            //NavController navController = navHostFragment.getNavController();
            NavController navController = Navigation.findNavController(this, R.id.fragment_container_view );

            Bundle bundle = new Bundle();
            bundle.putLong("property_id_arg", propertyId);
            navController.navigate(R.id.nav_propertyDetailFragment, bundle);*/

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