package com.openclassrooms.realestatemanager.ui.propertyedit.view;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.main.view.OnBackPressedInterface;
import com.openclassrooms.realestatemanager.ui.photoList.OnRowPhotoListener;
import com.openclassrooms.realestatemanager.ui.photoList.PhotoListAdapter;
import com.openclassrooms.realestatemanager.ui.photoedit.OnPhotoEditListener;
import com.openclassrooms.realestatemanager.ui.photoedit.PhotoEditDialogFragment;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.ConfirmationDeletePhotoListener;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel.PropertyEditViewModel;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel.FieldKey;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodelfactory.PropertyEditViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownViewstate;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.FieldState;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.PropertyEditViewState;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.StaticMapViewState;
import com.openclassrooms.realestatemanager.ui.selectimage.ImageSelectorObserver;
import com.openclassrooms.realestatemanager.utils.FileProviderHelper;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class PropertyEditFragment extends Fragment implements ConfirmationDeletePhotoListener,
                                                              OnBackPressedInterface  {

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
    private ImageView imageViewGoogleStaticMap;
    private Button buttonOk;

    PhotoListAdapter photoListAdapter;
    // call back
    private PropertyEditListener callbackEditProperty;

    private Photo photoToDelete;

    public PropertyEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(Tag.TAG, "PropertyEditFragment.onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        // to select photo from gallery
        imageSelectorObserver = new ImageSelectorObserver(requireActivity().getActivityResultRegistry());
        getLifecycle().addObserver(imageSelectorObserver);
    }

    @Override
    public boolean onBackPressed() {
        Log.d(Tag.TAG, "PropertyEditFragment.onBackPressed() called. to clearCache.");
        propertyEditViewModel.clearCache();
        return true;
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
        Log.d(Tag.TAG, "PropertyEditFragment.onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_edit, container, false);

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
        imageViewGoogleStaticMap = view.findViewById(R.id.fragment_property_edit_image_view_map);

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

        Button buttonTakePicture = view.findViewById(R.id.property_edit_button_take_picture);
        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        buttonOk = view.findViewById(R.id.fragment_property_edit_button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        Button buttonCancel = view.findViewById(R.id.fragment_property_edit_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelForm();
            }
        });
    }

    private void configureImageSelectorObserver(){
        Log.d(Tag.TAG, "PropertyEditFragment.configureImageSelectorObserver() called");
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

    private void configureRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.property_edit_recycler_view);
        registerForContextMenu(recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        photoListAdapter = new PhotoListAdapter(getContext(), new OnRowPhotoListener() {
            @Override
            public void onClickRowPhoto(Photo photo) {
                Log.d(Tag.TAG, "onClickRowPhoto() called with: photo = [" + photo + "]");
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

            @Override
            public void onLongClickRowPhoto(View view, Photo photo) {
                Log.d(Tag.TAG, "PropertyEditFragment.onLongClickRowPhoto() called with: view = [" + view + "], photo = [" + photo + "]");
                // cache photo to delete
                photoToDelete = photo;
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
        configureImageViewGoogleStaticMap();
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
                    Log.d(Tag.TAG, "PropertyEditFragment.onFocusChange() called text = [" + text + "]");
                    propertyEditViewModel.getAddressMutableLiveData().setValue(text);
                }
            }
        });
    }

    private void configureImageViewGoogleStaticMap(){
        propertyEditViewModel.getGoogleStaticMapViewState().observe(getViewLifecycleOwner(), new Observer<StaticMapViewState>() {
            @Override
            public void onChanged(StaticMapViewState staticMapViewState) {
                setPropertyLatLng(staticMapViewState.getLatLang());
                setImageViewGoogleStaticMap(staticMapViewState.getUrl());
            }
        });
    }

    private void setImageViewGoogleStaticMap(String url){
        if ((url == null) || (url.trim().isEmpty())) {
            // Clear picture
            imageViewGoogleStaticMap.setVisibility(View.GONE);
            imageViewGoogleStaticMap.setMaxHeight(100);
            Glide.with(imageViewGoogleStaticMap.getContext())
                    .load("")
                    .placeholder(R.drawable.ic_location)
                    .into(imageViewGoogleStaticMap);
            imageViewGoogleStaticMap.setMaxWidth(100);
        } else {
            //load picture
            imageViewGoogleStaticMap.setVisibility(View.VISIBLE);
            Glide.with(imageViewGoogleStaticMap.getContext())
                    .load(url)
                    .centerCrop()
                    .into(imageViewGoogleStaticMap);
        }
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
                            propertyEditViewModel.rememberValue(FieldKey.AGENT_ID, Long.toString(agentId));
                            propertyEditViewModel.rememberValue(FieldKey.AGENT_NAME, item.getName());
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
                            propertyEditViewModel.rememberValue(FieldKey.PROPERTY_TYPE_ID, Long.toString(propertyTypeId));
                            propertyEditViewModel.rememberValue(FieldKey.PROPERTY_TYPE_NAME, item.getName());
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
        propertyEditViewModel.clearCache();
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
                setImageViewGoogleStaticMap(propertyEditViewState.getGoogleStaticMapUrl());
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
                buttonOk.setEnabled(aBoolean);
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
            buttonOk.setEnabled(false);
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

    private void cancelForm() {
        propertyEditViewModel.clearCache();
        callbackEditProperty.onCancelEditProperty(this.propertyId);
    }

    private void setPropertyLatLng(LatLng latLng){
        this.propertyLatLng = latLng;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Tag.TAG, "PropertyEditFragment.onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "PropertyEditFragment.onResume()");
        propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PROPERTY_ID_KEY))){
            propertyId = getArguments().getLong(PropertyConst.ARG_PROPERTY_ID_KEY, PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        }
        Log.d(Tag.TAG, "PropertyEditFragment.onResume() propertyId=" + propertyId);
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

    private TextWatcher createTextWatcher(FieldKey key){
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

    private TextWatcher addressTitleCacheListener = createTextWatcher(FieldKey.ADDRESS_TITLE);
    private void setAddressTitle(String addressTitle){
        setValueToComponent(addressTitle, textInputLayoutAddressTitle, addressTitleCacheListener);
    }

    private String getAddress(){
        return textInputLayoutAddress.getEditText().getText().toString().trim();
    }

    private TextWatcher addressCacheListener = createTextWatcher(FieldKey.ADDRESS);
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

    private TextWatcher priceCacheListener = createTextWatcher(FieldKey.PRICE);
    private void setPrice(String price){
        setValueToComponent(price, textInputLayoutPrice, priceCacheListener);
    }

    private String getSurface(){
        return textInputLayoutSurface.getEditText().getText().toString().trim();
    }

    private TextWatcher surfaceCacheListener = createTextWatcher(FieldKey.SURFACE);
    private void setSurface(String surface){
        setValueToComponent(surface, textInputLayoutSurface, surfaceCacheListener);
    }

    private String getRooms(){
        return textInputLayoutRooms.getEditText().getText().toString().trim();
    }

    private TextWatcher roomsCacheListener = createTextWatcher(FieldKey.ROOMS);
    private void setRooms(String rooms){
        setValueToComponent(rooms, textInputLayoutRooms, roomsCacheListener);
    }

    private String getDescription(){
        return textInputLayoutDescription.getEditText().getText().toString().trim();
    }

    private TextWatcher descriptionCacheListener = createTextWatcher(FieldKey.DESCRIPTION);
    private void setDescription(String description){
        setValueToComponent(description, textInputLayoutDescription, descriptionCacheListener);
    }

    private String getPointOfInterest(){
        return textInputLayoutPointOfInterest.getEditText().getText().toString().trim();
    }

    private TextWatcher pointOfInterestCacheListener = createTextWatcher(FieldKey.POINT_OF_INTEREST);
    private void setPointOfInterest(String pointOfInterest){
        setValueToComponent(pointOfInterest, textInputLayoutPointOfInterest, pointOfInterestCacheListener);
    }

    private String getEntryDate() {
        return textInputLayoutEntryDate.getEditText().getText().toString().trim();
    }

    private TextWatcher entryDateCacheListener = createTextWatcher(FieldKey.ENTRY_DATE);
    private void setEntryDate(String entryDate){
        Log.d(Tag.TAG, "PropertyEditFragment.setEntryDate() called with: entryDate = [" + entryDate + "]");
        setValueToComponent(entryDate, textInputLayoutEntryDate, entryDateCacheListener);
    }

    private String getSaleDate() {
        return textInputLayoutSaleDate.getEditText().getText().toString().trim();
    }

    private TextWatcher saleDateCacheListener = createTextWatcher(FieldKey.SALE_DATE);
    private void setSaleDate(String saleDate){
        setValueToComponent(saleDate, textInputLayoutSaleDate, saleDateCacheListener);
    }

    private void setPhotos(List<Photo> photos){
        Log.d(Tag.TAG, "PropertyEditFragment.setPhotos() called with: photos = [" + photos + "]");
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

    private Uri latestUri = null;

    ActivityResultLauncher<Uri> mGetContent = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    // do what you need with the uri here ...
                    Log.d("TAG", "onActivityResult() called with: uri = [" + latestUri + "]");
                    addPhoto(latestUri, "form camera");
                }
            });



    private void takePicture(){
        latestUri = FileProviderHelper.createFileUri();
        mGetContent.launch(latestUri);
    }

    private void addPhoto(Uri uri, String caption){
        Log.d(Tag.TAG, "PropertyEditFragment.addPhoto() called with: uri = [" + uri + "], caption = [" + caption + "]");
        propertyEditViewModel.addPhoto(uri, caption, propertyId);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(Tag.TAG, "PropertyEditFragment.onCreateContextMenu() called with: menu = [" + menu + "], v = [" + v + "], menuInfo = [" + menuInfo + "]");
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.fragment_property_edit_context_menu_photo, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Log.d(Tag.TAG, "PropertyEditFragment.onContextItemSelected() called with: item = [" + item + "]");
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //Log.d(Tag.TAG, "onContextItemSelected() called with: info = [" + info + "]");
        switch (item.getItemId()){
            case R.id.fragment_property_edit_context_menu_photo_delete:
                confirmDeletePhoto();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void confirmDeletePhoto(){
        Log.d(Tag.TAG, "PropertyEditFragment.confirmDeletePhoto() photoToDelete=" + photoToDelete);
        if (photoToDelete != null) {
            ConfirmationDeletePhotoDialogFragment confirmationDeletePhotoDialogFragment = new ConfirmationDeletePhotoDialogFragment();
            confirmationDeletePhotoDialogFragment.setListener(this);
            confirmationDeletePhotoDialogFragment.show(getChildFragmentManager(), ConfirmationDeletePhotoDialogFragment.TAG);
        }
    }

    @Override
    public void onConfirmDeletePhoto() {
        Log.d(Tag.TAG, "PropertyEditFragment.onConfirmDeletePhoto() called");
        if (photoToDelete != null){
            propertyEditViewModel.deletePhoto(photoToDelete);
            photoToDelete = null;
        }
    }

    @Override
    public void onCancelDeletePhoto() {
        Log.d(Tag.TAG, "PropertyEditFragment.onCancelDeletePhoto() called");
        photoToDelete = null;
    }
}
