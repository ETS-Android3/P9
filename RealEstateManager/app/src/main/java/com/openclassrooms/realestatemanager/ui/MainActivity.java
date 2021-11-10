package com.openclassrooms.realestatemanager.ui;



import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.bundle.PropertyBundle;
import com.openclassrooms.realestatemanager.ui.propertydetail.listener.OnEditPropertyListener;
import com.openclassrooms.realestatemanager.ui.propertydetail.listener.OnMapListener;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;
import com.openclassrooms.realestatemanager.ui.propertyedit.view.PropertyEditFragment;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertydetail.view.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.ui.propertylist.listener.OnAddPropertyListener;
import com.openclassrooms.realestatemanager.ui.propertylist.listener.OnPropertySelectedListener;
import com.openclassrooms.realestatemanager.ui.propertylist.view.PropertyListFragment;
import com.openclassrooms.realestatemanager.utils.LandscapeHelper;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements OnPropertySelectedListener,
                                                               OnAddPropertyListener,
                                                               OnEditPropertyListener,
                                                               PropertyEditListener,
                                                               OnMapListener {

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

        WindowManager manager = (WindowManager) this.getSystemService(Activity.WINDOW_SERVICE);
        if (manager != null && manager.getDefaultDisplay() != null) {
            int rotation = manager.getDefaultDisplay().getRotation();
            Log.d(Tag.TAG, "logScreen() rotation = " + rotation);
            int orientation = this.getResources().getConfiguration().orientation;
            Log.d(Tag.TAG, "logScreen() orientation = " + orientation);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_toolbar_map:
                navToMap();
                return true;
            case R.id.menu_item_toolbar_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        Log.d(Tag.TAG, "MainActivity.onConfigurationChanged()");

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            loadConfigurationLandscape();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            loadConfigurationPortrait();
        }
    }

    private void loadConfigurationPortrait(){
        if (! LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.onConfigurationChanged() -> portrait");
            Fragment detailFragment = getFragmentManager().findFragmentById(R.id.fragment_container_view);
            if (detailFragment == null) {
                // screen rotation can come with null fragment
                // go to main view with detail
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_propertyListFragment);
            }
        }
    }

    private void loadConfigurationLandscape(){
        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.onConfigurationChanged() -> landscape");
            Fragment detailFragment = getFragmentManager().findFragmentById(R.id.fragment_container_view);
            if (detailFragment == null) {
                // screen rotation can come with null fragment
                // go to main view with detail
                NavController navControllerMain = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navControllerMain.navigate(R.id.nav_propertyListFragment);
            }
        }
    }

    @Override
    public void onPropertySelectedClicked(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.onPropertySelectedClicked() called with: propertyId = [" + propertyId + "]");
        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "onPropertySelectedClicked() isLandscape");
            navToDetailWithLandscapeOrientation(propertyId);
        }
        else {
            Log.d(Tag.TAG, "onPropertySelectedClicked() isLandscape = false");
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_propertyListFragment_to_nav_propertyDetailFragment,
                    PropertyBundle.createDetailBundle(propertyId));
        }
    }


    @Override
    public void onAddPropertyClicked() {
        Log.d(Tag.TAG, "MainActivity.onAddPropertyCLicked() called");

        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.onAddPropertyCLicked() isLandscape");
            navToEditWithLandscapeOrientation(PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        }
        else {
            Log.d(Tag.TAG, "MainActivity.onAddPropertyCLicked() isLandscape = false");

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_propertyListFragment_to_nav_propertyEditFragment,
                    PropertyBundle.createEditBundle(PropertyConst.PROPERTY_ID_NOT_INITIALIZED));
        }
    }

    @Override
    public void onEditPropertyClicked(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.onEditPropertyClicked() called with: propertyId = [" + propertyId + "]");

        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.onEditPropertyClicked() isLandscape");
            navToEditWithLandscapeOrientation((propertyId));
        }
        else {
            Log.d(Tag.TAG, "MainActivity.onEditPropertyClicked() isLandscape = false");

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_propertyDetailFragment_to_nav_propertyEditFragment,
                    PropertyBundle.createEditBundle(propertyId));
        }
    }

    @Override
    public void OnMapClicked(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.OnMapClicked() called with: propertyId = [" + propertyId + "]");
        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.OnMapClicked() isLandscape = true");
            navToDetailWithLandscapeOrientation(propertyId);
        }
        else {
            Log.d(Tag.TAG, "MainActivity.OnMapClicked() isLandscape = false");

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_propertyDetailFragment_self,
                    PropertyBundle.createEditBundle(propertyId));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "MainActivity.onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Tag.TAG, "MainActivity.onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Tag.TAG, "MainActivity.onStop() called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Tag.TAG, "MainActivity.onStart() called");
    }

    /**
     * Cancel edit property
     * @param propertyId
     */
    @Override
    public void onCancelEditProperty(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.onCancelEditProperty() called with: propertyId = [" + propertyId + "]");
        navFromEditToDetail(propertyId);
    }

    /**
     * Validate edit property
     * @param propertyId
     */
    @Override
    public void onValidateEditProperty(long propertyId) {
        // close fragment call back
        Log.d(Tag.TAG, "MainActivity.onValidateEditProperty() called with: propertyId = [" + propertyId + "]");
        navFromEditToDetail(propertyId);
    }

    /**
     * go back to detail
     * @param propertyId
     */
    private void navFromEditToDetail(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.navFromEditToDetail() called with: propertyId = [" + propertyId + "]");
        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.navFromEditToDetail() isLandscape");
            navToDetailWithLandscapeOrientation(propertyId);
        }
        else {
            Log.d(Tag.TAG, "MainActivity.navFromEditToDetail() isLandscape = false");
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.popBackStack();
        }
    }

    private void navToDetailWithLandscapeOrientation(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.navToDetailWithLandscapeOrientation() called with: propertyId = [" + propertyId + "]");

        NavController navController = Navigation.findNavController(this, R.id.fragment_container_view);
        navController.navigate(R.id.propertyDetailFragment, PropertyBundle.createDetailBundle(propertyId));
    }

    private void navToEditWithLandscapeOrientation(long propertyId){
        Log.d(Tag.TAG, "MainActivity.navToEditWithLandscapeOrientation() called with: propertyId = [" + propertyId + "]");

        NavController navController = Navigation.findNavController(this, R.id.fragment_container_view);
        navController.navigate(R.id.propertyEditFragment, PropertyBundle.createEditBundle(propertyId));
    }

    private void navToMap(){
        if (LandscapeHelper.isLandscape()) {
            //navToDetailWithLandscapeOrientation(propertyId);
            NavController navController = Navigation.findNavController(this, R.id.fragment_container_view);
            navController.navigate(R.id.propertyMapsFragment_land);
        }
        else {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.propertyMapsFragment);
        }
    }

}