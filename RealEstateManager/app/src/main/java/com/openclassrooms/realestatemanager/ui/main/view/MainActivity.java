package com.openclassrooms.realestatemanager.ui.main.view;



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

import androidx.fragment.app.FragmentContainerView;
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
import com.openclassrooms.realestatemanager.ui.main.viewstate.MenuItemViewState;
import com.openclassrooms.realestatemanager.ui.main.NavigationState;
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

        // Configure tool bar to display title
        Toolbar toolbar = binding.toolbar;
        // display icons and menu items in tools bar
        setSupportActionBar(toolbar);
        // Configure tool bar to display title
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_propertyListFragment_portrait)
                .build();
        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);

        logScreen();
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

        configureViewModel();

        return true;
    }

    private void configureViewModel(){
        mainViewModel = new ViewModelProvider(this, MainViewModelFactory.getInstance())
                .get(MainViewModel.class);
        mainViewModel.getMainViewStateLiveData().observe(this, new Observer<MainViewState>() {
            @Override
            public void onChanged(MainViewState mainViewState) {
                setMainViewState(mainViewState);
            }
        });

        // When device rotate activity was recreated.
        // inform the ViewModel orientation changed.
        mainViewModel.getIsLandscapeMutableLiveData().setValue(LandscapeHelper.isLandscape());
    }

    private void setMainViewState(MainViewState mainViewState) {
        Log.d(Tag.TAG, "MainActivity.setMainViewState() called with: mainViewState = [" + mainViewState + "]");
        navigateTo(mainViewState.getNavigationState());
        setMenuItemHome(mainViewState.getHome());
        setMenuItemDetail(mainViewState.getDetail());
        setMenuItemEdit(mainViewState.getEdit());
        setMenuItemAdd(mainViewState.getAdd());
        setMenuItemMap(mainViewState.getMap());
        setMenuItemSearch(mainViewState.getSearch());
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_toolbar_home:
                mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.HOME);
                return true;
            case R.id.menu_item_toolbar_detail:
                mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.DETAIL);
                return true;
            case R.id.menu_item_toolbar_edit:
                mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.EDIT);
                return true;
            case R.id.menu_item_toolbar_add:
                mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.ADD);
                return true;
            case R.id.menu_item_toolbar_map:
                mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.MAP);
                return true;
            case R.id.menu_item_toolbar_search:
                mainViewModel.getNavigationStateMutableLiveData().setValue(NavigationState.SEARCH);
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
        navToDetail(propertyId);
    }

    @Override
    public void OnMapClicked(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.OnMapClicked() called with: propertyId = [" + propertyId + "]");
        navToDetail(propertyId);
    }

    /**
     * Cancel edit property
     * @param propertyId
     */
    @Override
    public void onCancelEditProperty(long propertyId) {
        Log.d(Tag.TAG, "MainActivity.onCancelEditProperty() called with: propertyId = [" + propertyId + "]");
        navToDetail(propertyId);
    }

    /**
     * Validate edit property
     * @param propertyId
     */
    @Override
    public void onValidateEditProperty(long propertyId) {
        // close fragment call back
        Log.d(Tag.TAG, "MainActivity.onValidateEditProperty() called with: propertyId = [" + propertyId + "]");
        navToDetail(propertyId);
    }

    private NavController getNavController() {
        return Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    }

    // to remove backStack
    // use navController.popBackStack(R.id.fragment_apps, true);
    // or setPopUpTo(int, boolean) with the id of the NavController's graph and set inclusive to true.
    public void navigateTo(NavigationState destination){
        Log.d(Tag.TAG, "navigateTo() called with: destination = [" + destination + "]");
        switch (destination){
            case HOME:
                navToHome();
                return;
            case LIST:
                navToList();
                return;
            case DETAIL:
                navToDetail();
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

    private void navToHome(){
        Log.d(Tag.TAG, "navToHome() called");
        if (LandscapeHelper.isLandscape()) {
            navToDetail();
        } else {
            navToList();
        }
    }

    private void navToList(){
        Log.d(Tag.TAG, "navToList() called");
        getNavController().navigate(R.id.nav_propertyListFragment_portrait);
    }

    private void navToDetail(){
        Log.d(Tag.TAG, "navToDetail() called");
        navToDetail(PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
    }

    private void navToDetail(long propertyId){
        getNavController().navigate(R.id.nav_propertyDetailFragment_portrait,
                PropertyBundle.createEditBundle(propertyId));
    }

    private void navToAdd(){
        Log.d(Tag.TAG, "navToAdd() called");
        getNavController().navigate(R.id.nav_propertyEditFragment_portrait,
                PropertyBundle.createEditBundle(PropertyConst.PROPERTY_ID_NOT_INITIALIZED));
    }

    private void navToEdit(){
        Log.d(Tag.TAG, "navToEdit() called");

        // retrieve property id from view model
        PropertyDetailViewModel propertyDetailViewModel = new ViewModelProvider(
                this, PropertyDetailViewModelFactory.getInstance())
                .get(PropertyDetailViewModel.class);
        long id = propertyDetailViewModel.getCurrentPropertyId();
        getNavController().navigate(R.id.nav_propertyEditFragment_portrait,
                PropertyBundle.createEditBundle(id));
    }

    private void navToMap(){
        Log.d(Tag.TAG, "MainActivity.navToMap");
        getNavController().navigate(R.id.nav_propertyMapsFragment_portrait);
    }

    private void navToSearch() {
        Log.d(Tag.TAG, "navToSearch() called");
        getNavController().navigate(R.id.nav_propertySearchFragment_portrait);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(Tag.TAG, "onPostCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }
}