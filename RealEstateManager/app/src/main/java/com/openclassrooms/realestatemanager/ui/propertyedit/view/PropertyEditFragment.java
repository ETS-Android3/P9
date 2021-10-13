package com.openclassrooms.realestatemanager.ui.propertyedit.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel.PropertyEditViewModel;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodelfactory.PropertyEditViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownViewstate;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.FieldState;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PropertyEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PropertyEditFragment extends Fragment implements OnMapReadyCallback {

    private long UNINITIALIZED_AGENT_ID = -1;
    private long UNINITIALIZED_PROPERTY_TYPE_ID = -1;

    private long propertyId;
    private long agentId = UNINITIALIZED_AGENT_ID;
    private long propertyTypeId = UNINITIALIZED_PROPERTY_TYPE_ID;

    private TextView textViewTitle;

    private TextInputLayout textInputLayoutAddressTitle;

    private TextInputLayout textInputLayoutAddress;
    private TextInputEditText textInputEditTextAddress;

    private TextInputLayout textInputLayoutPrice;

    private TextInputLayout textInputLayoutSurface;

    private TextInputLayout textInputLayoutRooms;

    private TextInputLayout textInputLayoutDescription;

    private TextInputLayout textInputLayoutPointOfInterest;

    private TextInputLayout textInputLayoutEntryDate;
    private ImageView imageViewEntryDate;

    private TextInputLayout textInputLayoutSaleDate;
    private ImageView imageViewSaleDate;

    private SwitchMaterial switchMaterialAvailable;
    private SwitchMaterial switchMaterialCategory;

    private TextInputLayout textInputLayoutAgent;
    private TextInputLayout textInputLayoutPropertyType;

    private Spinner spinnerType;
    private Button buttonPhoto;

    private BottomNavigationView bottomNavigationView;
    private MenuItem menuItemOk;

    private PropertyEditViewModel propertyEditViewModel;

    private PropertyEditListener callbackEditProperty;

    private GoogleMap mMap;
    private LatLng propertyLatLng;

    public PropertyEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param propertyId
     * @return A new instance of fragment PropertyEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PropertyEditFragment newInstance(long propertyId) {
        PropertyEditFragment fragment = new PropertyEditFragment();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }

    private void createCallbackToParentActivity() {
        try {
            callbackEditProperty = (PropertyEditListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + "must implement PropertyEditListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_edit, container, false);
        configureBottomNavigationBar(view);
        configureComponents(view);
        return view;
    }

    private void configureComponents(View view) {
        textViewTitle = view.findViewById(R.id.fragment_property_edit_text_view_title);

        textInputLayoutAddressTitle = view.findViewById(R.id.fragment_property_edit_text_input_layout_address_title);
        textInputLayoutAddress = view.findViewById(R.id.fragment_property_edit_text_input_layout_address);
        textInputEditTextAddress = view.findViewById(R.id.fragment_property_edit_text_input_edit_text_address);
        textInputLayoutPrice = view.findViewById(R.id.fragment_property_edit_text_input_layout_price);
        textInputLayoutSurface = view.findViewById(R.id.fragment_property_edit_text_input_layout_surface);
        textInputLayoutRooms = view.findViewById(R.id.fragment_property_edit_text_input_layout_rooms);
        textInputLayoutDescription = view.findViewById(R.id.fragment_property_edit_text_input_layout_description);
        textInputLayoutPointOfInterest = view.findViewById(R.id.fragment_property_edit_text_input_layout_point_of_interest);
        textInputLayoutEntryDate = view.findViewById(R.id.fragment_property_edit_text_input_layout_entry_date);

        imageViewEntryDate = view.findViewById(R.id.fragment_property_imageView_entry_date);
        imageViewEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(textInputLayoutEntryDate);
            }
        });

        textInputLayoutSaleDate = view.findViewById(R.id.fragment_property_edit_text_input_layout_sale_date);
        imageViewSaleDate = view.findViewById(R.id.fragment_property_imageView_sale_date);
        imageViewSaleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(textInputLayoutSaleDate);
            }
        });

        textInputLayoutAgent = view.findViewById(R.id.fragment_property_edit_text_input_layout_agent);
        textInputLayoutPropertyType = view.findViewById(R.id.fragment_property_edit_text_input_layout_property_type);

        switchMaterialAvailable = view.findViewById(R.id.property_edit_switch_available);
        switchMaterialAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                menuItemOk.setEnabled(isChecked);
            }
        });

        switchMaterialCategory = view.findViewById(R.id.property_edit_switch_category);
        buttonPhoto = view.findViewById(R.id.property_edit_button_add_photo);

        bottomNavigationView = view.findViewById(R.id.fragment_property_edit_bottom_navigation_view);
        menuItemOk = bottomNavigationView.getMenu().findItem(R.id.fragment_property_edit_ok);
    }

    private void selectDate(TextInputLayout textInputLayoutDate){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                textInputLayoutDate.getEditText().setText(Utils.convertDateToLocalFormat(date));
            }
        }, year, month, day);
        picker.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
            this.propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        } else {
            this.propertyId = -1;
        }
        Log.d(Tag.TAG, "PropertyEditFragment.onViewCreated() propertyId= [" + propertyId + "]");

        configureViewModel();
    }

    private void configureViewModel() {
        propertyEditViewModel = new ViewModelProvider(
                requireActivity(),
                PropertyEditViewModelFactory.getInstance()).get(PropertyEditViewModel.class);

        configureGpsListener();
        configureDropdown();
        configureControlValues();
        configureControlAgentId();
        configureControlPropertyTypeId();
        configureControlAllValues();

        propertyEditViewModel.loadDropDownLists();
    }

    private void configureGpsListener(){
        /**
         * to load gps location and update map when address change
         */
        propertyEditViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                Log.d(Tag.TAG, "onChanged() called. with: latLng = [" + latLng + "]");
                setPropertyLatLng(latLng);
            }
        });

        textInputEditTextAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = getAddress();
                    propertyEditViewModel.loadLocationByAddress(text);
                }
            }
        });
    }

    private void configureDropdown(){
        /**
         * To load agent list and propertytype list for drop down
         */
        propertyEditViewModel.getDropDownViewstateMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<DropdownViewstate>() {
            @Override
            public void onChanged(DropdownViewstate dropDownViewstate) {
                // load dropdown agents list
                if (dropDownViewstate.getAgentItems() != null){
                    ArrayAdapter adapterAgents = new ArrayAdapter(getContext(), R.layout.list_item, dropDownViewstate.getAgentItems());
                    AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutAgent.getEditText();
                    autoCompleteTextView.setAdapter(adapterAgents);
                    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DropdownItem item = (DropdownItem) adapterAgents.getItem(position);
                            agentId = item.getId();
                            // to check input
                            checkAllValues();
                        }
                    });
                }
                // load dropdown property types list
                if (dropDownViewstate.getPropertyTypeItems() != null){
                    ArrayAdapter adapterPropertyType = new ArrayAdapter(getContext(), R.layout.list_item, dropDownViewstate.getPropertyTypeItems());
                    AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutPropertyType.getEditText();
                    autoCompleteTextView.setAdapter(adapterPropertyType);
                    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DropdownItem item = (DropdownItem) adapterPropertyType.getItem(position);
                            propertyTypeId = item.getId();
                            // to check input
                            checkAllValues();
                        }
                    });
                }
            }
        });
    }

    private void configureControlValues(){
        configureControlValue(textInputLayoutAddressTitle,
                propertyEditViewModel.getOnCheckAddressTitleValueLiveData(),
                propertyEditViewModel::checkAddressTitleValue);
        configureControlValue(textInputLayoutAddress,
                propertyEditViewModel.getOnCheckAddressValueLiveData(),
                propertyEditViewModel::checkAddressValue);
        configureControlValue(textInputLayoutPrice,
                propertyEditViewModel.getOnCheckPriceValueLiveData(),
                propertyEditViewModel::checkPriceValue);
        configureControlValue(textInputLayoutSurface,
                propertyEditViewModel.getOnCheckSurfaceValueLiveData(),
                propertyEditViewModel::checkSurfaceValue);
        configureControlValue(textInputLayoutRooms,
                propertyEditViewModel.getOnCheckRoomsValueLiveData(),
                propertyEditViewModel::checkRoomsValue);
        configureControlValue(textInputLayoutDescription,
                propertyEditViewModel.getOnCheckDescriptionValueLiveData(),
                propertyEditViewModel::checkDescriptionValue);
        configureControlValue(textInputLayoutPointOfInterest,
                propertyEditViewModel.getOnCheckPointOfInterestValueLiveData(),
                propertyEditViewModel::checkPointOfInterestValue);
        configureControlValue(textInputLayoutEntryDate,
                propertyEditViewModel.getOnCheckEntryDateValueLiveData(),
                propertyEditViewModel::checkEntryDateValue);
        configureControlValue(textInputLayoutSaleDate,
                propertyEditViewModel.getOnCheckSaleDateValueLiveData(),
                propertyEditViewModel::checkSaleDateValue);
    }

    private void configureControlAgentId(){
        propertyEditViewModel.getOnCheckAgentIdValueLiveData().observe(getViewLifecycleOwner(), new Observer<FieldState>() {
            @Override
            public void onChanged(FieldState fieldState) {
                setErrorEnabledLayout(textInputLayoutAgent, fieldState);
            }
        });
    }

    private void configureControlPropertyTypeId(){
        propertyEditViewModel.getOnCheckPropertyTypeIdValueLiveData().observe(getViewLifecycleOwner(), new Observer<FieldState>() {
            @Override
            public void onChanged(FieldState fieldState) {
                setErrorEnabledLayout(textInputLayoutPropertyType, fieldState);
            }
        });
    }

    private void configureControlAllValues(){
        propertyEditViewModel.getOnCheckAllValuesLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(Tag.TAG, "configureControlAllValues.onChanged() called with: aBoolean = [" + aBoolean + "]");
                menuItemOk.setEnabled(aBoolean);
            }
        });
    }
    /**
     * show or hide error on component
     * @param textInputLayout
     * @param fieldState
     */
    private void setErrorEnabledLayout(TextInputLayout textInputLayout, FieldState fieldState){
        if (fieldState.getResId() == PropertyEditViewModel.NO_STRING_ID) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getString(fieldState.getResId()));
            menuItemOk.setEnabled(false);
        }
    }

    /**
     * Configure the listener and live data to check the value coming from the TextInputLayout
     * @param textInputLayout
     * @param getLiveData
     * @param checkValueProcedure
     */
    private void configureControlValue(TextInputLayout textInputLayout,
                                  LiveData<FieldState> getLiveData,
                                  Function<String, Boolean> checkValueProcedure){

        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                //checkValueProcedure.apply(text);
                checkAllValues();
            }
        });

        getLiveData.observe(getViewLifecycleOwner(), new Observer<FieldState>() {
            @Override
            public void onChanged(FieldState fieldState) {
                setErrorEnabledLayout(textInputLayout, fieldState);
            }
        });
    }

    private void configureBottomNavigationBar(View view) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.fragment_property_edit_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navigate(item);
            }
        });
    }

    private boolean navigate(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_property_edit_cancel:
                callbackEditProperty.onCancelEditProperty(this.propertyId);
                return true;
            case R.id.fragment_property_edit_ok:
                validateForm();
                return true;
            case R.id.fragment_property_edit_sell:
                callbackEditProperty.onSellProperty(this.propertyId);
                return true;
        }
        return false;
    }

    private void setPropertyLatLng(LatLng latLng){
        this.propertyLatLng = latLng;
        drawPropertylocation();
    }

    private void drawPropertylocation(){
        Log.d(Tag.TAG, "PropertyEditFragment.drawPropertylocation() (mMap==null)=" + (mMap==null) + " (propertyLatLng==null)=" + (propertyLatLng==null));
        if ((mMap!=null) && (this.propertyLatLng != null)) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(propertyLatLng).title("Property position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLatLng, 12));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(Tag.TAG, "PropertyEditFragment.onMapReady()");
        mMap = googleMap;
        if (this.propertyLatLng != null) {
            Log.d(Tag.TAG, "onMapReady() -> setLocation()");
            drawPropertylocation();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Tag.TAG, "PropertyDetailFragment.MapFragment.onStart() called");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_property_edit_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "PropertyDetailFragment.MapFragment.onResume()");
        if ((mMap != null) && (this.propertyLatLng != null)) {
            propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
            if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
                propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
            }
            Log.d(Tag.TAG, "PropertyDetailFragment.onViewCreated() propertyId=" + propertyId + "");
            Log.d(Tag.TAG, "MapFragment.onResume() -> propertyDetailViewModel.load()");
            //propertyDetailViewModel.load(propertyId);
        }
    }

    private String getAddressTitle(){
        return textInputLayoutAddressTitle.getEditText().getText().toString().trim();
    }

    private void setAddressTitle(String addressTitle){
        textInputLayoutAddressTitle.getEditText().setText(addressTitle);
    }

    private String getAddress(){
        return textInputLayoutAddress.getEditText().getText().toString().trim();
    }

    private void setAdrress(String address){
        textInputLayoutAddress.getEditText().setText(address);
    }

    private long getAgentId(){
        return this.agentId;
    }

    private long getPropertyTypeId(){
        return this.propertyTypeId;
    }

    private String getPrice(){
        return textInputLayoutPrice.getEditText().getText().toString().trim();
    };

    private void setPrice(String price){
        textInputLayoutPrice.getEditText().setText(price);
    }

    private String getSurface(){
        return textInputLayoutSurface.getEditText().getText().toString().trim();
    }

    private void setSurface(String surface){
        textInputLayoutSurface.getEditText().setText(surface);
    }

    private String getRooms(){
        return textInputLayoutRooms.getEditText().getText().toString().trim();
    }

    private void setRooms(String rooms){
        textInputLayoutRooms.getEditText().setText(rooms);
    }

    private String getDescription(){
        return textInputLayoutDescription.getEditText().getText().toString().trim();
    }

    private void setDescription(String description){
        textInputLayoutDescription.getEditText().setText(description);
    }

    private String getPointOfInterest(){
        return textInputLayoutPointOfInterest.getEditText().getText().toString().trim();
    }

    private void setPointOfInterest(String pointOfInterest){
        textInputLayoutPointOfInterest.getEditText().setText(pointOfInterest);
    }

    private String getEntryDate() {
        return textInputLayoutEntryDate.getEditText().getText().toString().trim();
    }

    private void setEntryDate(String entryDate){
        textInputLayoutEntryDate.getEditText().setText(entryDate);
    }

    private String getSaleDate() {
        return textInputLayoutSaleDate.getEditText().getText().toString().trim();
    }

    private void setSaleDate(String saleDate){
        textInputLayoutEntryDate.getEditText().setText(saleDate);
    }

    private boolean getAvailable(){
        return switchMaterialAvailable.isChecked();
    }

    private void setAvailable(boolean available){
        switchMaterialAvailable.setChecked(available);
    }

    private boolean getCategory(){
        return switchMaterialCategory.isChecked();
    }

    private void setCategory(boolean forSale){
        switchMaterialCategory.setChecked(forSale);
    }

    private boolean validateForm(){
        if (this.propertyId == PropertyConst.PROPERTY_ID_NOT_INITIALIZED) {
            insertProperty();
        } else {
            updateProperty();
        }
        return false;
    }

    private void updateProperty(){

    }

    private void insertProperty(){
        propertyEditViewModel.addProperty(
                getPrice(),
                getSurface(),
                getDescription(),
                getAddressTitle(),
                getAddress(),
                getPointOfInterest(),
                getAvailable(),
                getEntryDate(),
                getSaleDate(),
                getPropertyTypeId(),
                getCategory(),
                getAgentId(),
                getRooms(),
                propertyLatLng,
                new PropertyEditViewModel.AddPropertyInterface() {
                    @Override
                    public void onPropertyAdded(long propertyId) {
                        callbackEditProperty.onValidateEditProperty(propertyId);
                    }
                });
    }

    private void checkAllValues(){
        propertyEditViewModel.checkAllValues(
                getPrice(),
                getSurface(),
                getDescription(),
                getAddressTitle(),
                getAddress(),
                getPointOfInterest(),
                getAvailable(),
                getEntryDate(),
                getSaleDate(),
                getPropertyTypeId(),
                getCategory(),
                getAgentId(),
                getRooms(),
                propertyLatLng);
    }
}
