package com.openclassrooms.realestatemanager.ui.propertysearch.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertysearch.listener.PropertySearchListener;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel.PropertySearchViewModel;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;
import com.openclassrooms.realestatemanager.ui.view_model_factory.AppViewModelFactory;

import java.util.List;

public class PropertySearchFragment extends Fragment {

    // fields
    private long agentId = PropertyConst.AGENT_ID_NOT_INITIALIZED;
    private long propertyTypeId = PropertyConst.PROPERTY_TYPE_ID_NOT_INITIALIZED;
    // callback to apply search
    private PropertySearchListener propertySearchListener;
    // view model
    private PropertySearchViewModel propertySearchViewModel;

    // Components
    private TextInputLayout textInputLayoutAddressTitle;
    private TextInputLayout textInputLayoutAddress;
    private TextInputLayout textInputLayoutAgents;
    private TextInputLayout textInputLayoutPropertyTypes;

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
        textInputLayoutAgents = view.findViewById(R.id.fragment_property_search_text_input_layout_agent);
        textInputLayoutPropertyTypes = view.findViewById(R.id.fragment_property_search_text_input_layout_property_type);

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
                setAgents(propertySearchViewState.getAgents(), propertySearchViewState.getAgentIndex());
                setPropertyTypes(propertySearchViewState.getPropertyTypes(), propertySearchViewState.getPropertyTypeIndex());
            }
        });
    }

    private void setAgents(List<DropdownItem> agents, int index) {
        Log.d(Tag.TAG, "PropertySearchFragment.setAgents() called with: agents = [" + agents + "], index = [" + index + "]");
        if (agents != null){
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.list_item, agents);
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutAgents.getEditText();

            // must do setText before setAdapter
            if ((index >= 0) && (index <= arrayAdapter.getCount())) {
                DropdownItem item = (DropdownItem) arrayAdapter.getItem(index);
                this.agentId = item.getId();
                autoCompleteTextView.setText(item.getName());
            }

            autoCompleteTextView.setAdapter(arrayAdapter);
            autoCompleteTextView.setListSelection (index);

            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(Tag.TAG, "PropertySearchFragment.onItemClick() called with:  position = [" + position + "], id = [" + id + "]");
                    DropdownItem item = (DropdownItem) arrayAdapter.getItem(position);
                    agentId = item.getId();
                    propertySearchViewModel.getAgentIndexMutableLiveData().setValue(position);
                }
            });
        }
    }

    private void setPropertyTypes(List<DropdownItem> propertyTypes, int index) {
        Log.d(Tag.TAG, "PropertySearchFragment.setPropertyTypes() called with: propertyTypes = [" + propertyTypes + "], index = [" + index + "]");
        if (propertyTypes != null){
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.list_item, propertyTypes);
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutPropertyTypes.getEditText();
            if ((index >= 0) && (index <= arrayAdapter.getCount())) {
                DropdownItem item = (DropdownItem) arrayAdapter.getItem(index);
                this.propertyTypeId = item.getId();
                autoCompleteTextView.setText(item.getName());
            }
            autoCompleteTextView.setAdapter(arrayAdapter);
            autoCompleteTextView.setListSelection (index);

            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DropdownItem item = (DropdownItem) arrayAdapter.getItem(position);
                    propertyTypeId = item.getId();
                    propertySearchViewModel.getPropertyTypeMutableLiveData().setValue(position);
                }
            });
        }
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

        propertySearchViewModel.setSearchValues(titleAddress, address, this.agentId, this.propertyTypeId);
        //if (propertySearchListener != null)
        //    propertySearchListener.onApplySearch();
    }
}