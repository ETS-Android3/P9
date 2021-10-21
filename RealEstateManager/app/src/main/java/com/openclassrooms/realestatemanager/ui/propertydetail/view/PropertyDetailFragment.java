package com.openclassrooms.realestatemanager.ui.propertydetail.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.photoList.OnRowPhotoListener;
import com.openclassrooms.realestatemanager.ui.photoList.PhotoListAdapter;
import com.openclassrooms.realestatemanager.ui.propertydetail.listener.OnEditPropertyListener;
import com.openclassrooms.realestatemanager.ui.propertydetail.listener.OnMapListener;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodelfactory.PropertyDetailViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewstate.PropertyDetailViewState;
import com.openclassrooms.realestatemanager.utils.UtilsDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PropertyDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PropertyDetailFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Location userLocation;
    private PropertyLocationData currentPropertyLocation;
    private List<PropertyLocationData> otherPropertiesLocation = new ArrayList<>();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private long propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;

    TextView textViewPrice;
    TextView textViewSurface;
    TextView textViewRooms;
    TextView textViewDescription;
    TextView textViewAddressTitle;
    TextView textViewAddress;
    TextView textViewPointOfInterest;
    TextView textViewState;
    TextView textViewEntryDate;
    TextView textViewSaleDate;
    TextView textViewAgentName;
    TextView textViewAgentEmail;
    TextView textViewAgentPhone;
    TextView textViewType;
    TextView textViewPhotoLegend;

    private PropertyDetailViewModel propertyDetailViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    PhotoListAdapter photoListAdapter;

    private void setPropertyDetailViewModel(PropertyDetailViewModel propertyDetailViewModel) {
        this.propertyDetailViewModel = propertyDetailViewModel;
    }

    public PropertyDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param propertyId Parameter 1.
     * @return A new instance of fragment PropertyDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PropertyDetailFragment newInstance(long propertyId) {
        PropertyDetailFragment fragment = new PropertyDetailFragment();
        Bundle args = new Bundle();
        args.putLong(PropertyConst.ARG_PROPERTY_ID_KEY, propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    this interface used to edit property
     */
    private OnEditPropertyListener callbackEditProperty;
    private OnMapListener callbackMap;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }

    private void createCallbackToParentActivity() {
        try {
            callbackEditProperty = (OnEditPropertyListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnPropertyClickedListener");
        }
        try {
            callbackMap = (OnMapListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnMapListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
            propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        }
        Log.d(Tag.TAG, "PropertyDetailFragment.onCreateView() propertyId=" + propertyId + "");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_detail, container, false);
        configureComponents(view);
        configureRecyclerView(view);
        configureBottomNavigationBar(view);
        configureDetailViewModel();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Tag.TAG, "onViewCreated() called with: view = [" + view + "], savedInstanceState = [" + savedInstanceState + "]");
    }

    private void configureComponents(View view){
        textViewPrice = view.findViewById(R.id.property_detail_prive_value);
        textViewSurface = view.findViewById(R.id.property_detail_surface_value);
        textViewRooms = view.findViewById(R.id.property_detail_rooms_value);
        textViewDescription = view.findViewById(R.id.property_detail_description_value);
        textViewAddressTitle = view.findViewById(R.id.property_detail_address_title_value);
        textViewAddress = view.findViewById(R.id.property_detail_address_value);
        textViewPointOfInterest = view.findViewById(R.id.property_detail_point_of_interest_value);
        textViewState = view.findViewById(R.id.property_detail_state_value);
        textViewEntryDate = view.findViewById(R.id.property_detail_entry_date_value);
        textViewSaleDate = view.findViewById(R.id.property_detail_sale_date_value);
        textViewAgentName = view.findViewById(R.id.property_detail_agent_name_value);
        textViewAgentEmail = view.findViewById(R.id.property_detail_agent_email_value);
        textViewAgentPhone = view.findViewById(R.id.property_detail_agent_phone_value);
        textViewType = view.findViewById(R.id.property_detail_type_value);
        textViewPhotoLegend = view.findViewById(R.id.property_detail_photo_legend_value);
    }

    private void configureRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.property_detail_recycler_view);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        photoListAdapter = new PhotoListAdapter(getContext(), new OnRowPhotoListener() {
            @Override
            public void onClickRowPhoto(long photoId) {
                // open photo with call back
            }
        });

        recyclerView.setAdapter(photoListAdapter);
    }


    private void configureBottomNavigationBar(View view) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.fragment_property_detail_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navigate(item);
            }
        });
    }

    private boolean navigate(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_propertyEditFragment:
                callbackEditProperty.onEditPropertyClicked(this.propertyId);
                return true;
        }
        return false;
    }

    private void configureDetailViewModel(){
        propertyDetailViewModel = new ViewModelProvider(
                requireActivity(), PropertyDetailViewModelFactory.getInstance())
                .get(PropertyDetailViewModel.class);

        propertyDetailViewModel.getViewState().observe(getViewLifecycleOwner(), new Observer<PropertyDetailViewState>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChanged(PropertyDetailViewState propertyDetailViewState) {
                setUserLocation(propertyDetailViewState.getUserLocation());
                setCurrentPropertyLocation(propertyDetailViewState.getCurrentPropertyLocation());
                setOtherPropertiesLocation(propertyDetailViewState.getPropertyLocationData());
                setPrice(propertyDetailViewState.getPropertyDetailData().getPrice());
                setSurface(propertyDetailViewState.getPropertyDetailData().getSurface());
                setRooms(propertyDetailViewState.getPropertyDetailData().getRooms());
                setDescription(propertyDetailViewState.getPropertyDetailData().getDescription());
                setAddressTitle(propertyDetailViewState.getPropertyDetailData().getAddressTitle());
                setAddress(propertyDetailViewState.getPropertyDetailData().getAddress());
                setPointOfInterest(propertyDetailViewState.getPropertyDetailData().getPointsOfInterest());
                setState(propertyDetailViewState.getPropertyState());
                setEntryDate(propertyDetailViewState.getEntryDate());
                setSaleDate(propertyDetailViewState.getSaleDate());
                setAgentName(propertyDetailViewState.getPropertyDetailData().getAgentName());
                setAgentEmail(propertyDetailViewState.getPropertyDetailData().getAgentEmail());
                setAgentPhone(propertyDetailViewState.getPropertyDetailData().getAgentPhone());
                setTypeName(propertyDetailViewState.getPropertyDetailData().getTypeName());
                setPhotoLegend("");
                // list photos
                photoListAdapter.updateData(propertyDetailViewState.getPhotos());
            }
        });

        propertyDetailViewModel.getFirstOrValidId(propertyId).observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                propertyId = aLong;
                propertyDetailViewModel.load(propertyId);
            }
        });
    }

    private void setPrice(int price){
        textViewPrice.setText(Utils.convertPriceToString(price));
    }

    private void setSurface(int surface){
        textViewSurface.setText(Utils.convertSurfaceToString(surface));
    }

    private void setRooms(int rooms){
        textViewRooms.setText(String.format("%d", rooms));
    }

    private void setDescription(String description){
        textViewDescription.setText(description);
    }

    private void setAddressTitle(String addressTitle){
        textViewAddressTitle.setText(addressTitle);
    }

    private void setAddress(String address){
        textViewAddress.setText(address);
    }

    private void setPointOfInterest(String pointOfInterest){
        textViewPointOfInterest.setText(pointOfInterest);
    }

    private void setState(String available){
        textViewState.setText(available);
    }

    private void setEntryDate(String entryDate) {
        textViewEntryDate.setText(entryDate);
    }

    private void setSaleDate(String saleDate){
        textViewSaleDate.setText(saleDate);
    }

    private void setAgentName(String agentName) {
        textViewAgentName.setText(agentName);
    }

    private void setAgentEmail(String agentEmail) {
        textViewAgentEmail.setText(agentEmail);
    }

    private void setAgentPhone(String agentPhone) {
        textViewAgentPhone.setText(agentPhone);
    }

    private void setTypeName(String typeName) {
        textViewType.setText(typeName);
    }

    private void setPhotoLegend(String photoLegend) {
        textViewPhotoLegend.setText(photoLegend);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String strPropertyId = (String) marker.getTag();
        Log.d(Tag.TAG, "onMarkerClick() propertyId = [" + propertyId + "]");
        try {
            long propertyId = Long.parseLong(strPropertyId);
            callbackMap.OnMapClicked(propertyId);
        } catch (NumberFormatException nfe) {
            Log.e(Tag.TAG, "onMarkerClick: " + nfe.getMessage());
        }
        return false;
    }

    private String formatTitleMarker(String addressTitle, int price){
        return String.format("%s %s", addressTitle, Utils.convertPriceToString(price));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setOtherPropertiesLocation(List<PropertyLocationData> propertyLocationDataList){
        Log.d(Tag.TAG, "setOtherPropertiesLocation() (propertyLocationDataList==null)=" + (propertyLocationDataList == null));
        if (propertyLocationDataList != null) {
            this.otherPropertiesLocation.clear();
            this.otherPropertiesLocation.addAll(propertyLocationDataList);
            drawOtherPropertiesLocation();
        }
    }

    private void drawOtherPropertiesLocation() {
        Log.d(Tag.TAG, "drawOtherPropertiesLocation() (mMap==null)=" + (mMap==null) + " (otherPropertiesLocation==null)=" + (otherPropertiesLocation==null));
        if ((mMap != null) && (this.otherPropertiesLocation != null)) {
            Bitmap bitmap = UtilsDrawable.drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_primary_color, getContext().getTheme()));
            for (PropertyLocationData propertyLocationData : otherPropertiesLocation) {
                LatLng latlng = new LatLng(propertyLocationData.getLatitude(), propertyLocationData.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(formatTitleMarker(propertyLocationData.getAddressTitle(), propertyLocationData.getPrice()))
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                String tag = String.format("%s", propertyLocationData.getId());
                marker.setTag(tag);
                mMap.setOnMarkerClickListener(this::onMarkerClick);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setCurrentPropertyLocation(PropertyLocationData currentPropertyLocation) {
        this.currentPropertyLocation = currentPropertyLocation;
        drawCurrentPropertylocation();
    }

    private void drawCurrentPropertylocation(){
        Log.d(Tag.TAG, "drawCurrentPropertylocation() (mMap==null)=" + (mMap==null) + " (currentPropertyLocation==null)=" + (currentPropertyLocation==null));
        if ((mMap != null) && (currentPropertyLocation != null)) {
            Bitmap bitmap = UtilsDrawable.drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_dark_red, getContext().getTheme()));
            LatLng latlng = new LatLng(currentPropertyLocation.getLatitude(), currentPropertyLocation.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(formatTitleMarker(currentPropertyLocation.getAddressTitle(), currentPropertyLocation.getPrice()))
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        }
    }

    private void setUserLocation(Location userLocation){
        this.userLocation = userLocation;
        drawUserLocation();
    }

    private void drawUserLocation(){
        Log.d(Tag.TAG, "drawUserLocation() (mMap==null)=" + (mMap==null) + " (userLocation==null)=" + (userLocation==null));
        if ((mMap!=null) && (this.userLocation != null)) {
            LatLng latlng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latlng).title("Your position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Tag.TAG, "MapFragment.onStart() called");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_property_detail_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(Tag.TAG, "onMapReady()");
        mMap = googleMap;
        if (this.userLocation != null) {
            Log.d(Tag.TAG, "onMapReady() -> setLocation()");
            drawUserLocation();
            drawCurrentPropertylocation();
            drawOtherPropertiesLocation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "MapFragment.onResume()");
        if ((mMap != null) && (this.userLocation != null) && (propertyDetailViewModel != null)) {

            propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
            if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
                propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
            }
            Log.d(Tag.TAG, "PropertyDetailFragment.onViewCreated() propertyId=" + propertyId + "");
            Log.d(Tag.TAG, "MapFragment.onResume() -> propertyDetailViewModel.load()");
            propertyDetailViewModel.load(propertyId);
        }
    }
}