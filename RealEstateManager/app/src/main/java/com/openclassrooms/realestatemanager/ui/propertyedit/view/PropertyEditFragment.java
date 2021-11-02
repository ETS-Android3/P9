package com.openclassrooms.realestatemanager.ui.propertyedit.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.photoList.OnRowPhotoListener;
import com.openclassrooms.realestatemanager.ui.photoList.PhotoListAdapter;
import com.openclassrooms.realestatemanager.ui.photoedit.OnPhotoEditListener;
import com.openclassrooms.realestatemanager.ui.photoedit.PhotoEditDialogFragment;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel.PropertyEditViewModel;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel.RememberFieldKey;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodelfactory.PropertyEditViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownViewstate;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.FieldState;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.PropertyEditViewState;
import com.openclassrooms.realestatemanager.ui.selectimage.ImageSelectorObserver;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.utils.UtilsDrawable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class PropertyEditFragment extends Fragment implements OnMapReadyCallback {

    // Fields
    private long propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
    private long agentId = PropertyConst.AGENT_ID_NOT_INITIALIZED;
    private long propertyTypeId = PropertyConst.PROPERTY_TYPE_ID_NOT_INITIALIZED;
    private LatLng propertyLatLng;
    private PropertyEditViewModel propertyEditViewModel;
    private ImageSelectorObserver imageSelectorObserver;
    // Components
    private TextInputLayout textInputLayoutAddressTitle;
    private TextInputLayout textInputLayoutAddress;
    private TextInputEditText textInputEditTextAddress;
    private TextInputLayout textInputLayoutPrice;
    private TextInputLayout textInputLayoutSurface;
    private TextInputLayout textInputLayoutRooms;
    private TextInputLayout textInputLayoutDescription;
    private TextInputLayout textInputLayoutPointOfInterest;
    private TextInputLayout textInputLayoutEntryDate;
    private TextInputLayout textInputLayoutSaleDate;
    private TextInputLayout textInputLayoutAgent;
    private TextInputLayout textInputLayoutPropertyType;
    private MenuItem menuItemOk;
    private GoogleMap mMap;
    PhotoListAdapter photoListAdapter;
    // call back
    private PropertyEditListener callbackEditProperty;

    public PropertyEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        imageSelectorObserver = new ImageSelectorObserver(requireActivity().getActivityResultRegistry());
        getLifecycle().addObserver(imageSelectorObserver);
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
        configureImageSelectorObserver();
        configureRecyclerView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
            this.propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        } else {
            this.propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
        }
        Log.d(Tag.TAG, "PropertyEditFragment.onViewCreated() propertyId= [" + propertyId + "]");

        configureViewModel();
    }

    private void configureComponents(View view) {
        textInputLayoutAddressTitle = view.findViewById(R.id.fragment_property_edit_text_input_layout_address_title);
        textInputLayoutAddress = view.findViewById(R.id.fragment_property_edit_text_input_layout_address);
        textInputEditTextAddress = view.findViewById(R.id.fragment_property_edit_text_input_edit_text_address);
        textInputLayoutPrice = view.findViewById(R.id.fragment_property_edit_text_input_layout_price);
        textInputLayoutSurface = view.findViewById(R.id.fragment_property_edit_text_input_layout_surface);
        textInputLayoutRooms = view.findViewById(R.id.fragment_property_edit_text_input_layout_rooms);
        textInputLayoutDescription = view.findViewById(R.id.fragment_property_edit_text_input_layout_description);
        textInputLayoutPointOfInterest = view.findViewById(R.id.fragment_property_edit_text_input_layout_point_of_interest);
        textInputLayoutEntryDate = view.findViewById(R.id.fragment_property_edit_text_input_layout_entry_date);

        ImageButton imageButtonEntryDate = view.findViewById(R.id.fragment_property_imageButton_entry_date);
        imageButtonEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(textInputLayoutEntryDate);
            }
        });

        textInputLayoutSaleDate = view.findViewById(R.id.fragment_property_edit_text_input_layout_sale_date);
        ImageButton imageButtonRemoveSaleDate = view.findViewById(R.id.fragment_property_imageButton_remove_sale_date);
        imageButtonRemoveSaleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSaleDate("");
            }
        });
        ImageButton imageButtonSaleDate = view.findViewById(R.id.fragment_property_imageButton_sale_date);
        imageButtonSaleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(textInputLayoutSaleDate);
            }
        });

        textInputLayoutAgent = view.findViewById(R.id.fragment_property_edit_text_input_layout_agent);
        textInputLayoutPropertyType = view.findViewById(R.id.fragment_property_edit_text_input_layout_property_type);

        Button buttonPhoto = view.findViewById(R.id.property_edit_button_add_photo);
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.fragment_property_edit_bottom_navigation_view);
        menuItemOk = bottomNavigationView.getMenu().findItem(R.id.fragment_property_edit_ok);
    }

    private void configureRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.property_edit_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        photoListAdapter = new PhotoListAdapter(getContext(), new OnRowPhotoListener() {
            @Override
            public void onClickRowPhoto(Photo photo) {
                // open photo with call back
                PhotoEditDialogFragment.newInstance(
                        getContext().getString(R.string.edit_photo),
                        photo.getId(),
                        photo.getOrder(),
                        photo.getUrl(),
                        photo.getLegend(),
                        photo.getPropertyId())
                        .setPhotoEditListener(new OnPhotoEditListener() {
                            @Override
                            public void onPhotoEditOk(long id, int order, String url, String caption, long propertyId) {
                                Log.d(Tag.TAG, "PropertyEditFragment.onPhotoEditOk() called with: id = [" + id + "], order = [" + order + "], url = [" + url + "], caption = [" + caption + "], propertyId = [" + propertyId + "]");
                                Photo photo = new Photo(id, order, url, caption, propertyId);
                                propertyEditViewModel.updatePhoto(photo);
                            }

                            @Override
                            public void onPhotoEditCancel() {

                            }
                        })
                        .show(getChildFragmentManager(), null);
            }
        });

        recyclerView.setAdapter(photoListAdapter);
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

    private void configureViewModel() {
        propertyEditViewModel = new ViewModelProvider(
                requireActivity(),
                PropertyEditViewModelFactory.getInstance()).get(PropertyEditViewModel.class);

        configureGpsListener();
        configureControlValues();
        configureControlAgentId();
        configureControlPropertyTypeId();
        configureControlAllValues();
        configureViewState();
     }

    private void configureGpsListener(){
        /**
         * to load gps location and update map when address change
         */
        textInputEditTextAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = getAddress();
                    propertyEditViewModel.getLocationLiveDataByAddress(text).observe(getViewLifecycleOwner(), new Observer<LatLng>() {
                        @Override
                        public void onChanged(LatLng latLng) {
                            Log.d(Tag.TAG, "onChanged() called. with: latLng = [" + latLng + "]");
                            setPropertyLatLng(latLng);
                        }
                    });
                }
            }
        });
    }

    /**
     * Find position element in dropdown list by id, for agent ou property type
     * @param id
     * @param items
     * @return
     */
    private int findDropdownPositionById(long id, List<DropdownItem> items){
        if ((items != null) && (items.size() > 0) && (id >= 0)) {
            int position = 0;
            Iterator<DropdownItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                DropdownItem item = iterator.next();
                if (item.getId() == id) {
                    return position;
                }
                position++;
            }
        }
        return -1;
    }

    /**
     * Load agents list and propertytypes list in drop down components
     * @param currentAgentId
     * @param currentPropertyTypeId
     */
    private void configureDropdown(long currentAgentId, long currentPropertyTypeId){
        // There are two lists in DropdownViewstate, one for the agents list and one for the property types list
        propertyEditViewModel.getDropDownViewstateMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<DropdownViewstate>() {
            @Override
            public void onChanged(DropdownViewstate dropDownViewstate) {
                // load dropdown agents list
                if (dropDownViewstate.getAgentItems() != null){
                    ArrayAdapter adapterAgents = new ArrayAdapter(getContext(), R.layout.list_item, dropDownViewstate.getAgentItems());
                    AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutAgent.getEditText();
                    autoCompleteTextView.setAdapter(adapterAgents);
                    // update list position with current agent
                    int position = findDropdownPositionById(currentAgentId, dropDownViewstate.getAgentItems());
                    if (position >= 0) {
                        autoCompleteTextView.setListSelection (position);
                        agentId = currentAgentId;
                    }

                    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DropdownItem item = (DropdownItem) adapterAgents.getItem(position);
                            agentId = item.getId();
                            propertyEditViewModel.rememberValue(RememberFieldKey.AGENT_ID, Long.toString(agentId));
                            propertyEditViewModel.rememberValue(RememberFieldKey.AGENT_NAME, item.getName());
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
                    // update list position with current property type
                    int position = findDropdownPositionById(currentPropertyTypeId, dropDownViewstate.getPropertyTypeItems());
                    if (position >= 0) {
                        autoCompleteTextView.setListSelection (position);
                        propertyTypeId = currentPropertyTypeId;
                    }

                    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DropdownItem item = (DropdownItem) adapterPropertyType.getItem(position);
                            propertyTypeId = item.getId();
                            propertyEditViewModel.rememberValue(RememberFieldKey.PROPERTY_TYPE_ID, Long.toString(propertyId));
                            propertyEditViewModel.rememberValue(RememberFieldKey.PROPERTY_TYPE_NAME, item.getName());
                            // to check input
                            checkAllValues();
                        }
                    });
                }
            }
        });
    }

    /**
     * load property values to components
     */
    private void configureViewState(){
        Log.d(Tag.TAG, "PropertyEditFragment.configureViewState() called");
        propertyEditViewModel.clearFieldsCache();
        propertyEditViewModel.getViewStateLiveData(this.propertyId).observe(getViewLifecycleOwner(), new Observer<PropertyEditViewState>() {
            @Override
            public void onChanged(PropertyEditViewState propertyEditViewState) {
                setAddressTitle(propertyEditViewState.getAddressTitle());
                setAdrress(propertyEditViewState.getAddress());
                setDescription(propertyEditViewState.getDescription());
                setPointOfInterest(propertyEditViewState.getPointOfInterest());
                setPrice(propertyEditViewState.getPrice());
                setSurface(propertyEditViewState.getSurface());
                setRooms(propertyEditViewState.getRooms());
                setEntryDate(propertyEditViewState.getEntryDate());
                setSaleDate(propertyEditViewState.getSaleDate());
                setAgentId(propertyEditViewState.getAgentId(), propertyEditViewState.getAgentName());
                setPropertyTypeId(propertyEditViewState.getPropertyTypeId(), propertyEditViewState.getPropertyTypeName());
                setPropertyLatLng(new LatLng(propertyEditViewState.getLatitude(), propertyEditViewState.getLongitude()));
                configureDropdown(propertyEditViewState.getAgentId(), propertyEditViewState.getPropertyTypeId());
                setPhotos(propertyEditViewState.getPhotos());
                checkAllValues();
            }
        });
    }

    /**
     * for each components setErrorEnabled true or false when data has been checked
     */
    private void configureControlValues(){
        configureControlValue(textInputLayoutAddressTitle,
                propertyEditViewModel.getOnCheckAddressTitleValueLiveData());
        configureControlValue(textInputLayoutAddress,
                propertyEditViewModel.getOnCheckAddressValueLiveData());
        configureControlValue(textInputLayoutPrice,
                propertyEditViewModel.getOnCheckPriceValueLiveData());
        configureControlValue(textInputLayoutSurface,
                propertyEditViewModel.getOnCheckSurfaceValueLiveData());
        configureControlValue(textInputLayoutRooms,
                propertyEditViewModel.getOnCheckRoomsValueLiveData());
        configureControlValue(textInputLayoutDescription,
                propertyEditViewModel.getOnCheckDescriptionValueLiveData());
        configureControlValue(textInputLayoutPointOfInterest,
                propertyEditViewModel.getOnCheckPointOfInterestValueLiveData());
        configureControlValue(textInputLayoutEntryDate,
                propertyEditViewModel.getOnCheckEntryDateValueLiveData());
        configureControlValue(textInputLayoutSaleDate,
                propertyEditViewModel.getOnCheckSaleDateValueLiveData());
    }

    /**
     * setErrorEnabled true or false when agent id was checked
     */
    private void configureControlAgentId(){
        propertyEditViewModel.getOnCheckAgentIdValueLiveData().observe(getViewLifecycleOwner(), new Observer<FieldState>() {
            @Override
            public void onChanged(FieldState fieldState) {
                setErrorEnabledLayout(textInputLayoutAgent, fieldState);
            }
        });
    }

    /**
     * setErrorEnabled true or false when property type id was checked
     */
    private void configureControlPropertyTypeId(){
        propertyEditViewModel.getOnCheckPropertyTypeIdValueLiveData().observe(getViewLifecycleOwner(), new Observer<FieldState>() {
            @Override
            public void onChanged(FieldState fieldState) {
                setErrorEnabledLayout(textInputLayoutPropertyType, fieldState);
            }
        });
    }

    /**
     * button ok : setEnabled true or false, depends on all values
     */
    private void configureControlAllValues(){
        propertyEditViewModel.getOnCheckAllValuesLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
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
        if (fieldState.getResId() == PropertyConst.NO_STRING_ID) {
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
     */
    private void configureControlValue(TextInputLayout textInputLayout,
                                  LiveData<FieldState> getLiveData){

        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navigate(item);
            }
        });
    }

    private boolean navigate(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_property_edit_cancel:
                cancelForm();
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

    private void cancelForm() {
        propertyEditViewModel.clearFieldsCache();
        callbackEditProperty.onCancelEditProperty(this.propertyId);
    }

    private void setPropertyLatLng(LatLng latLng){
        this.propertyLatLng = latLng;
        drawPropertylocation();
    }

    private void drawPropertylocation(){
        if ((mMap != null) && (propertyLatLng != null)) {
            Bitmap bitmap = UtilsDrawable.drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_dark_red, getContext().getTheme()));
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(propertyLatLng)
                    .title("Property position")
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLatLng, 12));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(Tag.TAG, "PropertyEditFragment.onMapReady()");
        mMap = googleMap;
        if (this.propertyLatLng != null) {
            Log.d(Tag.TAG, "PropertyEditFragment.onMapReady() -> setLocation()");
            drawPropertylocation();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Tag.TAG, "PropertyEditFragment.MapFragment.onStart() called");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_property_edit_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "PropertyEditFragment.MapFragment.onResume()");
        if ((mMap != null) && (this.propertyLatLng != null)) {
            propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
            if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
                propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Tag.TAG, "PropertyEditFragment.onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Tag.TAG, "PropertyEditFragment.onStop() called");
    }

    private String getAddressTitle(){
        return textInputLayoutAddressTitle.getEditText().getText().toString().trim();
    }

    private TextWatcher createTextWatcher(RememberFieldKey key){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                // for two way binding
                propertyEditViewModel.rememberValue(key, text);
            }
        };
    }

    private void addCacheListener(TextInputLayout textInputLayout, TextWatcher textWatcher){
        textInputLayout.getEditText().addTextChangedListener(textWatcher);
    }

    private void removeCacheListener(TextInputLayout textInputLayout, TextWatcher textWatcher){
        textInputLayout.getEditText().removeTextChangedListener(textWatcher);
    }

    private void setValueToComponent(String value, TextInputLayout textInputLayout, TextWatcher cacheListener){
        // Don't want to remember initial value in cache
        removeCacheListener(textInputLayout, cacheListener);
        textInputLayout.getEditText().setText(value);
        // We can activate the cache after saving the value in the component
        addCacheListener(textInputLayout, cacheListener);
    }

    private TextWatcher addressTitleCacheListener = createTextWatcher(RememberFieldKey.ADDRESS_TITLE);
    private void setAddressTitle(String addressTitle){
        Log.d(Tag.TAG, "PropertyEditFragment.setAddressTitle() called with: addressTitle = [" + addressTitle + "]");
        setValueToComponent(addressTitle, textInputLayoutAddressTitle, addressTitleCacheListener);
    }

    private String getAddress(){
        return textInputLayoutAddress.getEditText().getText().toString().trim();
    }

    private TextWatcher addressCacheListener = createTextWatcher(RememberFieldKey.ADDRESS);
    private void setAdrress(String address){
        setValueToComponent(address, textInputLayoutAddress, addressCacheListener);
    }

    private long getAgentId(){
        return this.agentId;
    }

    private void setAgentId(long id, String name){
        this.agentId = id;
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutAgent.getEditText();
        autoCompleteTextView.setText(name);
    }

    private long getPropertyTypeId(){
        return this.propertyTypeId;
    }

    private void setPropertyTypeId(long id, String name){
        this.propertyTypeId = id;
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutPropertyType.getEditText();
        autoCompleteTextView.setText(name);
    }

    private String getPrice(){
        return textInputLayoutPrice.getEditText().getText().toString().trim();
    };

    private TextWatcher priceCacheListener = createTextWatcher(RememberFieldKey.PRICE);
    private void setPrice(String price){
        setValueToComponent(price, textInputLayoutPrice, priceCacheListener);
    }

    private String getSurface(){
        return textInputLayoutSurface.getEditText().getText().toString().trim();
    }

    private TextWatcher surfaceCacheListener = createTextWatcher(RememberFieldKey.SURFACE);
    private void setSurface(String surface){
        setValueToComponent(surface, textInputLayoutSurface, surfaceCacheListener);
    }

    private String getRooms(){
        return textInputLayoutRooms.getEditText().getText().toString().trim();
    }

    private TextWatcher roomsCacheListener = createTextWatcher(RememberFieldKey.ROOMS);
    private void setRooms(String rooms){
        setValueToComponent(rooms, textInputLayoutRooms, roomsCacheListener);
    }

    private String getDescription(){
        return textInputLayoutDescription.getEditText().getText().toString().trim();
    }

    private TextWatcher descriptionCacheListener = createTextWatcher(RememberFieldKey.DESCRIPTION);
    private void setDescription(String description){
        setValueToComponent(description, textInputLayoutDescription, descriptionCacheListener);
    }

    private String getPointOfInterest(){
        return textInputLayoutPointOfInterest.getEditText().getText().toString().trim();
    }

    private TextWatcher pointOfInterestCacheListener = createTextWatcher(RememberFieldKey.POINT_OF_INTEREST);
    private void setPointOfInterest(String pointOfInterest){
        setValueToComponent(pointOfInterest, textInputLayoutPointOfInterest, pointOfInterestCacheListener);
    }

    private String getEntryDate() {
        return textInputLayoutEntryDate.getEditText().getText().toString().trim();
    }

    private TextWatcher entryDateCacheListener = createTextWatcher(RememberFieldKey.ENTRY_DATE);
    private void setEntryDate(String entryDate){
        setValueToComponent(entryDate, textInputLayoutEntryDate, entryDateCacheListener);
    }

    private String getSaleDate() {
        return textInputLayoutSaleDate.getEditText().getText().toString().trim();
    }

    private TextWatcher saleDateCacheListener = createTextWatcher(RememberFieldKey.SALE_DATE);
    private void setSaleDate(String saleDate){
        setValueToComponent(saleDate, textInputLayoutSaleDate, saleDateCacheListener);
    }

    private void setPhotos(List<Photo> photos){
        photoListAdapter.updateData(photos);
    }

    private void validateForm(){
        insertOrUpdateProperty();
    }

    /**
     * call View Model to send date to database
     * if data control is ok call onPropertyAdded
     */
    private void insertOrUpdateProperty(){
        propertyEditViewModel.insertOrUpdateProperty(
                this.propertyId,
                getPrice(),
                getSurface(),
                getDescription(),
                getAddressTitle(),
                getAddress(),
                getPointOfInterest(),
                getEntryDate(),
                getSaleDate(),
                getPropertyTypeId(),
                getAgentId(),
                getRooms(),
                propertyLatLng,
                new PropertyEditViewModel.AddPropertyInterface() {
                    @Override
                    public void onPropertyAdded(long propertyId) {
                        // when data are ok go back to main activity to close form and navigate
                        callbackEditProperty.onValidateEditProperty(propertyId);
                    }
                });
    }

    /**
     * call View Model to control data.
     * a return is made to activate or not the errors on the components
     * See observer in configureControlValue
     */
    private void checkAllValues(){
        propertyEditViewModel.checkAllValues(
                getPrice(),
                getSurface(),
                getDescription(),
                getAddressTitle(),
                getAddress(),
                getPointOfInterest(),
                getEntryDate(),
                getSaleDate(),
                getPropertyTypeId(),
                getAgentId(),
                getRooms(),
                propertyLatLng);
    }

    private void selectPhoto(){
        imageSelectorObserver.openMultipleImages();
    }

    private void configureImageSelectorObserver(){
        imageSelectorObserver.getMultipleUrisLiveData().observe(getViewLifecycleOwner(), new Observer<List<Uri>>() {
            @Override
            public void onChanged(List<Uri> uris) {
                Log.d(Tag.TAG, "PropertyEditFragment.imageSelectorObserver.observe->onChanged() called with: uris = [" + uris + "]");
                int i = 1;
                for (Uri uri : uris) {
                    //getContext().getContentResolver().takePersistableUriPermission(uri, permissionFlags);
                    addPhoto(uri, "");
                    i++;
                }
            }
        });
    }

    private void addPhoto(Uri uri, String caption){
        Log.d(Tag.TAG, "PropertyEditFragment.addPhoto() called with: uri = [" + uri + "], caption = [" + caption + "]");
        propertyEditViewModel.addPhoto(uri, caption, propertyId);
    }

}
