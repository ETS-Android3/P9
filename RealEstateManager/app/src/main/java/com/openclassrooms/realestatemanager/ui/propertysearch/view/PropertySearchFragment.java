package com.openclassrooms.realestatemanager.ui.propertysearch.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;
import com.openclassrooms.realestatemanager.ui.propertysearch.listener.PropertySearchListener;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel.PropertySearchViewModel;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;
import com.openclassrooms.realestatemanager.ui.view_model_factory.AppViewModelFactory;

public class PropertySearchFragment extends Fragment {

    // callback to apply search
    private PropertySearchListener propertySearchListener;

    private PropertySearchViewModel propertySearchViewModel;

    // Components
    private TextInputLayout textInputLayoutAddressTitle;
    private TextInputLayout textInputLayoutAddress;
    private Button buttonOk;
    private Button buttonReset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_search, container, false);
        configureComponents(view);
        configureDetailViewModel();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }

    private void createCallbackToParentActivity() {
        try {
            propertySearchListener = (PropertySearchListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + "must implement PropertyEditListener");
        }
    }

    private void configureComponents(View view) {
        textInputLayoutAddressTitle = view.findViewById(R.id.fragment_property_search_text_input_layout_address_title);
        textInputLayoutAddress = view.findViewById(R.id.fragment_property_search_text_input_layout_address);

        buttonOk = view.findViewById(R.id.fragment_property_search_button_apply);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        buttonReset = view.findViewById(R.id.fragment_property_search_button_reset_all);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });

    }

    private void configureDetailViewModel() {
        propertySearchViewModel = new ViewModelProvider(
                requireActivity(), AppViewModelFactory.getInstance())
                .get(PropertySearchViewModel.class);

        propertySearchViewModel.getViewState().observe(getViewLifecycleOwner(), new Observer<PropertySearchViewState>() {
            @Override
            public void onChanged(PropertySearchViewState propertySearchViewState) {

            }
        });
    }

    private void setTitleAddress(String titleAddress){
        textInputLayoutAddressTitle.getEditText().setText(titleAddress);
    }

    private String getTitleAddress(){
        return textInputLayoutAddressTitle.getEditText().getText().toString().trim();
    }

    private void setAddress(String address){
        textInputLayoutAddress.getEditText().setText(address);
    }

    private String getAddress(){
        return textInputLayoutAddress.getEditText().getText().toString().trim();
    }

    private void resetForm() {
        setTitleAddress("");
        setAddress("");
        propertySearchViewModel.resetSearch();
    }

    private void validateForm() {
        String titleAddress = getTitleAddress();
        String address = getAddress();

        propertySearchViewModel.setSearchValues(titleAddress, address);
        //if (propertySearchListener != null)
        //    propertySearchListener.onApplySearch();
    }
}