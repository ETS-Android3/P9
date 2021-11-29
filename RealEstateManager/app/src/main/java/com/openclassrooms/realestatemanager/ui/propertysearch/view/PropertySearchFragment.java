package com.openclassrooms.realestatemanager.ui.propertysearch.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodelfactory.PropertyDetailViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewstate.PropertyDetailViewState;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel.PropertySearchViewModel;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewmodelfactory.PropertySearchViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;

public class PropertySearchFragment extends Fragment {

    private PropertySearchViewModel propertySearchViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_search, container, false);
        configureDetailViewModel();
        return view;
    }

    private void configureDetailViewModel() {
        propertySearchViewModel = new ViewModelProvider(
                requireActivity(), PropertySearchViewModelFactory.getInstance())
                .get(PropertySearchViewModel.class);

        propertySearchViewModel.getViewState().observe(getViewLifecycleOwner(), new Observer<PropertySearchViewState>() {
            @Override
            public void onChanged(PropertySearchViewState propertySearchViewState) {

            }
        });
    }
}