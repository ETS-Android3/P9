package com.openclassrooms.realestatemanager.ui.propertydetail.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailBinding;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.photoList.PhotoListAdapter;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodelfactory.PropertyDetailViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewstate.PropertyDetailViewState;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PropertyDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PropertyDetailFragment extends Fragment {

    private FragmentPropertyDetailBinding binding;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private long propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;

    private ImageView imageViewGoogleStaticMap;

    private PropertyDetailViewModel propertyDetailViewModel;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_property_detail, container, false);
        binding = FragmentPropertyDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
            propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        }
        Log.d(Tag.TAG, "PropertyDetailFragment.onCreateView() propertyId=" + propertyId + "");
        configureComponents(view);
        configureRecyclerView(view);
        configureDetailViewModel();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(Tag.TAG, "PropertyDetailFragment.onViewCreated()");
    }

    private void configureComponents(View view){
        imageViewGoogleStaticMap = view.findViewById(R.id.fragment_property_detail_image_view_map);
    }

    private void configureRecyclerView(View view) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.propertyDetailRecyclerView.setLayoutManager(layoutManager);

        PhotoListAdapter photoListAdapter = new PhotoListAdapter(getContext(), null);
        binding.propertyDetailRecyclerView.setAdapter(photoListAdapter);
    }

    private void setPhotos(List<Photo> photos){
        ((PhotoListAdapter) binding.propertyDetailRecyclerView.getAdapter()).updateData(photos);
    }

    private void configureDetailViewModel(){
        propertyDetailViewModel = new ViewModelProvider(
                requireActivity(), PropertyDetailViewModelFactory.getInstance())
                .get(PropertyDetailViewModel.class);

        propertyDetailViewModel.getViewState().observe(getViewLifecycleOwner(), new Observer<PropertyDetailViewState>() {
            @Override
            public void onChanged(PropertyDetailViewState propertyDetailViewState) {
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
                setImageViewGoogleStaticMap(propertyDetailViewState.getStaticMapUrl());
                // list photos
                setPhotos(propertyDetailViewState.getPhotos());
            }
        });

        propertyDetailViewModel.getFirstOrValidId(propertyId).observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                Log.d(Tag.TAG, "PropertyDetailFragment.configureDetailViewModel()->getFirstOrValidId->onChanged called with: aLong = [" + aLong + "]");
                propertyId = aLong;
                propertyDetailViewModel.load(propertyId);
            }
        });
    }

    private void setPrice(int price){
        binding.propertyDetailPriveValue.setText(Utils.convertPriceToString(price));
    }

    private void setSurface(int surface){
        binding.propertyDetailSurfaceValue.setText(Utils.convertSurfaceToString(surface));
    }

    private void setRooms(int rooms){
        binding.propertyDetailRoomsValue.setText(String.format("%d", rooms));
    }

    private void setDescription(String description){
        binding.propertyDetailDescriptionValue.setText(description);
    }

    private void setAddressTitle(String addressTitle){
        binding.propertyDetailAddressTitleValue.setText(addressTitle);
    }

    private void setAddress(String address){
        binding.propertyDetailAddressValue.setText(address);
    }

    private void setPointOfInterest(String pointOfInterest){
        binding.propertyDetailPointOfInterestValue.setText(pointOfInterest);
    }

    private void setState(String available){
        binding.propertyDetailStateValue.setText(available);
    }

    private void setEntryDate(String entryDate) {
        binding.propertyDetailEntryDateValue.setText(entryDate);
    }

    private void setSaleDate(String saleDate){
        binding.propertyDetailSaleDateValue.setText(saleDate);
    }

    private void setAgentName(String agentName) {
        binding.propertyDetailAgentNameValue.setText(agentName);
    }

    private void setAgentEmail(String agentEmail) {
        binding.propertyDetailAgentEmailValue.setText(agentEmail);
    }

    private void setAgentPhone(String agentPhone) {
        binding.propertyDetailAgentPhoneValue.setText(agentPhone);
    }

    private void setTypeName(String typeName) {
        binding.propertyDetailTypeValue.setText(typeName);
    }

    private void setImageViewGoogleStaticMap(String url){
        if ((url == null) || (url.trim().isEmpty())) {
            // Clear picture
            Glide.with(imageViewGoogleStaticMap.getContext())
                    .load("")
                    .placeholder(R.drawable.ic_signal_wifi_connected_no_internet)
                    .into(imageViewGoogleStaticMap);
        } else {
            //load picture
            Glide.with(imageViewGoogleStaticMap.getContext())
                    .load(url)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imageViewGoogleStaticMap);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Tag.TAG, "PropertyDetailFragment.onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "PropertyDetailFragment.onResume()");
        propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
            propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        }
        Log.d(Tag.TAG, "PropertyDetailFragment.onResume() -> propertyDetailViewModel.load(" + propertyId + ")");
        propertyDetailViewModel.load(propertyId);
    }
}