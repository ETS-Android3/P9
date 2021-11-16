package com.openclassrooms.realestatemanager.ui.main;



import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.bundle.PropertyBundle;
import com.openclassrooms.realestatemanager.ui.main.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.ui.main.viewmodelfactory.MainViewModelFactory;
import com.openclassrooms.realestatemanager.ui.main.viewstate.MainViewState;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodelfactory.PropertyDetailViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertylist.listener.OnPropertySelectedListener;
import com.openclassrooms.realestatemanager.ui.propertymap.listener.OnMapListener;
import com.openclassrooms.realestatemanager.utils.LandscapeHelper;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements OnPropertySelectedListener,
                                                               PropertyEditListener,
                                                               OnMapListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    MenuItem menuItemHome;
    MenuItem menuItemDetail;
    MenuItem menuItemEdit;
    MenuItem menuItemAdd;
    MenuItem menuItemMap;
    MenuItem menuItemSearch;

    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag.TAG, "MainActivity.onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // display icons and menu item in tools bar
        setSupportActionBar(binding.appBarMain.toolbar);

        // Configure tool bar to display title
        Toolbar toolbar = binding.appBarMain.toolbar;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_propertyListFragment_portrait)
                .setFallbackOnNavigateUpListener(new AppBarConfiguration.OnNavigateUpListener() {
                    @Override
                    public boolean onNavigateUp() {
                        Log.d(Tag.TAG, "MainActivity.onNavigateUp() called");
                        return false;
                    }
                })
                .build();
        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);

        logScreen();
        configureViewModel();
    }

    private void configureViewModel() {
        mainViewModel = new ViewModelProvider(this, MainViewModelFactory.getInstance())
            .get(MainViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "MainActivity.onResume() called");
        Log.d(Tag.TAG, "MainActivity.onResume() isLandscape = " + LandscapeHelper.isLandscape());
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
        Log.d(Tag.TAG, "MainActivity.onStart() isLandscape = " + LandscapeHelper.isLandscape());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(Tag.TAG, "MainActivity.onBackPressed() called");
    }

    private void logScreen(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d(Tag.TAG, String.format("MainActivity.logScreen(). width = %d, height = %d", width, height));
        Log.d(Tag.TAG, String.format("MainActivity.logScreen(). density = %f", getResources().getDimension(R.dimen.density)));

        WindowManager manager = (WindowManager) this.getSystemService(Activity.WINDOW_SERVICE);
        if (manager != null && manager.getDefaultDisplay() != null) {
            int rotation = manager.getDefaultDisplay().getRotation();
            Log.d(Tag.TAG, "MainActivity.logScreen() rotation = " + rotation);
            int orientation = this.getResources().getConfiguration().orientation;
            Log.d(Tag.TAG, "MainActivity.logScreen() orientation = " + orientation);
        }
        Log.d(Tag.TAG, "MainActivity.logScreen() isLandscape = " + LandscapeHelper.isLandscape());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(Tag.TAG, "MainActivity.onCreateOptionsMenu() called with: menu = [" + menu + "]");
        Log.d(Tag.TAG, "MainActivity.onCreateOptionsMenu() isLandscape = " + LandscapeHelper.isLandscape());
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menuItemHome = menu.findItem(R.id.menu_item_toolbar_home);
        menuItemDetail = menu.findItem(R.id.menu_item_toolbar_detail);
        menuItemEdit = menu.findItem(R.id.menu_item_toolbar_edit);
        menuItemAdd = menu.findItem(R.id.menu_item_toolbar_add);
        menuItemMap = menu.findItem(R.id.menu_item_toolbar_map);
        menuItemSearch = menu.findItem(R.id.menu_item_toolbar_search);

        mainViewModel.getMainViewStateLiveData().observe(this, new Observer<MainViewState>() {
            @Override
            public void onChanged(MainViewState mainViewState) {
                setMenuItemHome(mainViewState.getHome());
                setMenuItemDetail(mainViewState.getDetail());
                setMenuItemEdit(mainViewState.getEdit());
                setMenuItemAdd(mainViewState.getAdd());
                setMenuItemMap(mainViewState.getMap());
                setMenuItemSearch(mainViewState.getSearch());
                if (mainViewState.isOrientationChanged()) {
                    Log.d(Tag.TAG, "MainActivity ViewSate onChanged() orientation changed " +
                            "isLandscape = [" + mainViewState.isLandscape() + "] " +
                            "sate = [" + mainViewState.getNavigationState() + "]");

                    navigateTo(mainViewState.getNavigationState());
                }
            }
        });

        mainViewModel.getIsLandscapeMutableLiveData().setValue(LandscapeHelper.isLandscape());
        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.HOME);

        return true;
    }

    private void setMenuItemState(MenuItem menuItem, MenuItemViewState menuItemViewState){
        menuItem.setEnabled(menuItemViewState.isEnabled());
        menuItem.setVisible(menuItemViewState.isVisible());
    }

    private void setMenuItemHome(MenuItemViewState menuItemViewState) {
        setMenuItemState(menuItemHome, menuItemViewState);
    }

    private void setMenuItemDetail(MenuItemViewState menuItemViewState) {
        setMenuItemState(menuItemDetail, menuItemViewState);
    }

    private void setMenuItemEdit(MenuItemViewState menuItemViewState) {
        setMenuItemState(menuItemEdit, menuItemViewState);
    }

    private void setMenuItemAdd(MenuItemViewState menuItemViewState) {
        setMenuItemState(menuItemAdd, menuItemViewState);
    }

    private void setMenuItemMap(MenuItemViewState menuItemViewState) {
        setMenuItemState(menuItemMap, menuItemViewState);
    }

    private void setMenuItemSearch(MenuItemViewState menuItemViewState) {
        setMenuItemState(menuItemSearch, menuItemViewState);
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
        mainViewModel.getIsLandscapeMutableLiveData().setValue(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    private void loadConfigurationPortrait(){
        // rotate from landscape to portrait. remove detail to only show list
        if (! LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.onConfigurationChanged() -> portrait");
            Fragment detailFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_landscape);

            Log.d(Tag.TAG, "MainActivity.onConfigurationChanged() -> loadConfigurationPortrait detailFragmant = " + detailFragment);

            if (detailFragment == null) {
                // screen rotation can come with null fragment
                // go to main view with detail
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_propertyListFragment_portrait);
            }
        }
    }

    private void loadConfigurationLandscape(){
        // rotate from portrait to landscape
        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.onConfigurationChanged() -> landscape");
            Fragment detailFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_landscape);
            Log.d(Tag.TAG, "MainActivity.onConfigurationChanged() -> loadConfigurationLandscape detailFragmant + " + detailFragment);
            if (detailFragment == null) {
                // screen rotation can come with null fragment
                // go to main view with detail
                NavController navControllerMain = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navControllerMain.navigate(R.id.nav_propertyListFragment_portrait);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_toolbar_home:
                navigateTo(NavigationState.HOME);
                return true;
            case R.id.menu_item_toolbar_add:
                navigateTo(NavigationState.ADD);
                return true;
            case R.id.menu_item_toolbar_edit:
                navigateTo(NavigationState.EDIT);
                return true;
            case R.id.menu_item_toolbar_map:
                navigateTo(NavigationState.MAP);
                return true;
            case R.id.menu_item_toolbar_search:
                navigateTo(NavigationState.SEARCH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigateTo(NavigationState destination){
        switch (destination){
            case HOME:
                navToHome();
                return;
            case ADD:
                navToAdd();
                return;
            case EDIT:
                navToEdit();
                return;
            case MAP:
                navToMap();
                return;
            case SEARCH:
                navToSearch();
                return;
        }
    }

    /**
     * burger menu can open drawer
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {

/*
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        // back button in tool bar call onBackPressed;
        onBackPressed();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
*/

        return false;
    }

    @Override
    public void onPropertySelectedClicked(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.onPropertySelectedClicked() called with: propertyId = [" + propertyId + "] isLandscape = [" + LandscapeHelper.isLandscape() + "]");

        if (LandscapeHelper.isLandscape()) {
            navToDetailWithLandscapeOrientation(propertyId);
        }
        else {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_portrait_list_to_detail,
                    PropertyBundle.createDetailBundle(propertyId));
        }
        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.DETAIL);
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
            navController.navigate(R.id.action_nav_portrait_maps_to_detail,
                    PropertyBundle.createEditBundle(propertyId));
        }
        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.DETAIL);
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

        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.DETAIL);
    }

    private void navToDetailWithLandscapeOrientation(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.navToDetailWithLandscapeOrientation() called with: propertyId = [" + propertyId + "]");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_landscape);
        navController.navigate(R.id.nav_propertyDetailFragment_landscape, PropertyBundle.createDetailBundle(propertyId));

        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.DETAIL);
    }

    private void navToEditWithLandscapeOrientation(long propertyId){
        Log.d(Tag.TAG, "MainActivity.navToEditWithLandscapeOrientation() called with: propertyId = [" + propertyId + "]");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_landscape);
        navController.navigate(R.id.nav_propertyEditFragment_landscape, PropertyBundle.createEditBundle(propertyId));

        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.EDIT);
    }

    private void navToHome(){
        Log.d(Tag.TAG, "navToHome() called");

        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.navToHome isLandscape");
            navToDetailWithLandscapeOrientation(PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        }
        else {
            Log.d(Tag.TAG, "MainActivity.navToHome() isLandscape = false");

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_propertyListFragment_portrait);
        }

        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.HOME);
    }

    private void navToAdd(){
        Log.d(Tag.TAG, "navToAdd() called");

        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.onAddPropertyCLicked() isLandscape");
            navToEditWithLandscapeOrientation(PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        }
        else {
            Log.d(Tag.TAG, "MainActivity.onAddPropertyCLicked() isLandscape = false");

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_propertyEditFragment_portrait,
                    PropertyBundle.createEditBundle(PropertyConst.PROPERTY_ID_NOT_INITIALIZED));
        }

        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.ADD);
    }

    private void navToEdit(){
        Log.d(Tag.TAG, "navToEdit() called");

        // retrieve property id from view model
        PropertyDetailViewModel propertyDetailViewModel = new ViewModelProvider(
                this, PropertyDetailViewModelFactory.getInstance())
                .get(PropertyDetailViewModel.class);
        long id = propertyDetailViewModel.getCurrentPropertyId();

        if (LandscapeHelper.isLandscape()) {
            Log.d(Tag.TAG, "MainActivity.onEditPropertyClicked() isLandscape");
            navToEditWithLandscapeOrientation((id));
        }
        else {
            Log.d(Tag.TAG, "MainActivity.onEditPropertyClicked() isLandscape = false");

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_propertyEditFragment_portrait,
                    PropertyBundle.createEditBundle(id));
        }

        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.EDIT);
    }

    private void navToMap(){
        Log.d(Tag.TAG, "MainActivity.navToMap");

        if (LandscapeHelper.isLandscape()) {
            //navToDetailWithLandscapeOrientation(propertyId);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_landscape);
            navController.navigate(R.id.nav_propertyMapsFragment_landscape);
        }
        else {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_propertyMapsFragment_portrait);
        }

        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.MAP);
    }

    private void navToSearch() {
        Log.d(Tag.TAG, "navToSearch() called");

        if (LandscapeHelper.isLandscape()) {
            //navToDetailWithLandscapeOrientation(propertyId);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_landscape);
            navController.navigate(R.id.nav_propertySearchFragment_landscape);
        }
        else {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_propertySearchFragment_portrait);
        }

        mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.SEARCH);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(Tag.TAG, "onPostCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }
}