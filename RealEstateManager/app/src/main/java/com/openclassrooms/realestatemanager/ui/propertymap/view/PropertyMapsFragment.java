package com.openclassrooms.realestatemanager.ui.propertymap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertymap.listener.OnMapListener;
import com.openclassrooms.realestatemanager.ui.propertymap.viewmodel.PropertyMapViewModel;
import com.openclassrooms.realestatemanager.ui.propertymap.viewstate.PropertyMapItem;
import com.openclassrooms.realestatemanager.ui.view_model_factory.AppViewModelFactory;
import com.openclassrooms.realestatemanager.utils.UtilsDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PropertyMapsFragment extends Fragment {

    private PropertyMapViewModel viewModel;
    private Location userLocation;
    private List<PropertyMapItem> propertyMapItems = new ArrayList<>();
    private GoogleMap mMap;
    private OnMapListener callbackMap;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;

            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);

            if (userLocation != null) {
                drawUserLocation();
                drawPropertyLocations();
            }
        }
    };

    private final GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(@NonNull Marker marker) {
            String strPropertyId = (String) marker.getTag();
            try {
                if ((strPropertyId != null) && (!strPropertyId.isEmpty())) {
                    long id = Long.parseLong(strPropertyId);
                    callbackMap.OnMapClicked(id);
                }
            } catch (NumberFormatException nfe) {
                Log.e(Tag.TAG, "PropertyMapsFragment.onMarkerClick: " + nfe.getMessage());
            }
            return false;
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        createCallbackToParentActivity();
    }

    private void createCallbackToParentActivity() {
        try {
            callbackMap = (OnMapListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnMapListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(Tag.TAG, "PropertyMapsFragment.onCreateView()");
        return inflater.inflate(R.layout.fragment_property_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Tag.TAG, "PropertyMapsFragment.onViewCreated()");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        configureViewModel();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Tag.TAG, "PropertyMapsFragment.onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "PropertyMapsFragment.onResume()");

        if (viewModel != null) {
            long id = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
            if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
                id = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
            }
            Log.d(Tag.TAG, "PropertyMapsFragment.onResume() -> propertyDetailViewModel.load(" + id + ")");
        }
    }

    private void configureViewModel(){
        viewModel = new ViewModelProvider(
                requireActivity(), AppViewModelFactory.getInstance())
                .get(PropertyMapViewModel.class);

        viewModel.getViewState().observe(getViewLifecycleOwner(), propertyMapViewState -> {
            setUserLocation(propertyMapViewState.getUserLocation());
            setPropertyMapItems(propertyMapViewState.getPropertyMapItems());
        });
    }

    private void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
        drawUserLocation();
    }

    private void drawUserLocation(){
        Log.d(Tag.TAG, "PropertyMapsFragment.drawUserLocation() (mMap==null)=" + (mMap==null) + " (userLocation==null)=" + (userLocation==null));
        if ((mMap !=null) && (userLocation != null)) {
            LatLng latlng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latlng).title(getResources().getString(R.string.your_position)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
        }
    }

    public void setPropertyMapItems(List<PropertyMapItem> propertyMapItems) {
        this.propertyMapItems = propertyMapItems;
        drawPropertyLocations();
    }

    private void drawPropertyLocations() {
        if ((mMap != null) && (this.propertyMapItems != null)) {
            Bitmap bitmap = UtilsDrawable.drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_dark_red, requireContext().getTheme()));
            for (PropertyMapItem item : propertyMapItems) {
                LatLng latlng = new LatLng(item.getLatitude(), item.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(item.getTitle())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                String tag = String.format("%s", item.getId());
                Objects.requireNonNull(marker).setTag(tag);
                mMap.setOnMarkerClickListener(markerClickListener);
            }
        }
    }
}