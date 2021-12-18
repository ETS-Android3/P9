package com.openclassrooms.realestatemanager.ui.propertyedit.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
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
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.FieldState;
import com.openclassrooms.realestatemanager.ui.view_model_factory.AppViewModelFactory;
import com.openclassrooms.realestatemanager.utils.FileProviderHelper;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class PropertyEditFragment extends Fragment implements ConfirmationDeletePhotoListener,
                                                              OnBackPressedInterface  {

    // Fields
    private long propertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
    private long agentId = PropertyConst.AGENT_ID_NOT_INITIALIZED;
    private long propertyTypeId = PropertyConst.PROPERTY_TYPE_ID_NOT_INITIALIZED;
    private LatLng propertyLatLng;
    private PropertyEditViewModel propertyEditViewModel;
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
    }

    @Override
    public void onBackPressed() {
        Log.d(Tag.TAG, "PropertyEditFragment.onBackPressed() called. to clearCache.");
        propertyEditViewModel.clearCache();
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
        imageButtonEntryDate.setOnClickListener(v -> selectDate(textInputLayoutEntryDate));

        textInputLayoutSaleDate = view.findViewById(R.id.fragment_property_edit_text_input_layout_sale_date);
        ImageButton imageButtonRemoveSaleDate = view.findViewById(R.id.fragment_property_imageButton_remove_sale_date);
        imageButtonRemoveSaleDate.setOnClickListener(v -> setSaleDate(""));
        ImageButton imageButtonSaleDate = view.findViewById(R.id.fragment_property_imageButton_sale_date);
        imageButtonSaleDate.setOnClickListener(v -> selectDate(textInputLayoutSaleDate));

        textInputLayoutAgent = view.findViewById(R.id.fragment_property_edit_text_input_layout_agent);
        textInputLayoutPropertyType = view.findViewById(R.id.fragment_property_edit_text_input_layout_property_type);

        Button buttonPhoto = view.findViewById(R.id.property_edit_button_add_photo);
        buttonPhoto.setOnClickListener(v -> selectPhoto());

        Button buttonTakePicture = view.findViewById(R.id.property_edit_button_take_picture);
        buttonTakePicture.setOnClickListener(v -> takePicture());

        buttonOk = view.findViewById(R.id.fragment_property_edit_button_ok);
        buttonOk.setOnClickListener(v -> validateForm());

        Button buttonCancel = view.findViewById(R.id.fragment_property_edit_button_cancel);
        buttonCancel.setOnClickListener(v -> cancelForm());
    }

    private void configureRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.property_edit_recycler_view);
        registerForContextMenu(recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        photoListAdapter = new PhotoListAdapter(new OnRowPhotoListener() {
            @Override
            public void onClickRowPhoto(Photo photo) {
                Log.d(Tag.TAG, "onClickRowPhoto() called with: photo = [" + photo + "]");
                // open photo with call back
                PhotoEditDialogFragment.newInstance(
                        getString(R.string.edit_photo),
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

    private void setDateToTextInputLayout(Date date, TextInputLayout textInputLayout){
        if (textInputLayout != null) {
            Objects.requireNonNull(textInputLayout.getEditText()).setText(Utils.convertDateToLocalFormat(date));
        }
    }

    private void selectDate(TextInputLayout textInputLayoutDate){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            Date date = new GregorianCalendar(year1, month1, dayOfMonth).getTime();
            setDateToTextInputLayout(date, textInputLayoutDate);
        }, year, month, day);
        picker.show();
    }

    private void configureViewModel() {
        propertyEditViewModel = new ViewModelProvider(
                requireActivity(),
                AppViewModelFactory.getInstance()).get(PropertyEditViewModel.class);

        configureGpsListener();
        configureImageViewGoogleStaticMap();
        configureControlValues();
        configureControlAgentId();
        configureControlPropertyTypeId();
        configureControlAllValues();
        configureViewState();
     }

    /**
     * to load gps location and update map when address change
     */
    private void configureGpsListener(){
        textInputEditTextAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String text = getAddress();
                Log.d(Tag.TAG, "PropertyEditFragment.onFocusChange() called text = [" + text + "]");
                propertyEditViewModel.getAddressMutableLiveData().setValue(text);
            }
        });
    }

    private void configureImageViewGoogleStaticMap(){
        propertyEditViewModel.getGoogleStaticMapViewState().observe(getViewLifecycleOwner(), staticMapViewState -> {
            setPropertyLatLng(staticMapViewState.getLatLang());
            setImageViewGoogleStaticMap(staticMapViewState.getUrl());
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
     */
    private int findDropdownPositionById(long id, List<DropdownItem> items){
        if ((items != null) && (items.size() > 0) && (id >= 0)) {
            int position = 0;
            for (DropdownItem item : items) {
                if (item.getId() == id) {
                    return position;
                }
                position++;
            }
        }
        return -1;
    }

    /**
     * Load agents list and property types list in drop down components
     * @param currentAgentId : agent id
     * @param currentPropertyTypeId : property type id
     */
    private void configureDropdown(long currentAgentId, long currentPropertyTypeId){
        // There are two lists in Dropdown View state, one for the agents list and one for the property types list
        propertyEditViewModel.getDropDownViewstateMediatorLiveData().observe(getViewLifecycleOwner(), viewState -> {
            // load dropdown agents list
            if (viewState.getAgentItems() != null){
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutAgent.getEditText();
                if (autoCompleteTextView != null){
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.list_item, viewState.getAgentItems());

                    // update list position with current agent
                    int position = findDropdownPositionById(currentAgentId, viewState.getAgentItems());
                    if ((position >= 0) && (position <= arrayAdapter.getCount())) {
                        this.agentId = currentAgentId;
                        autoCompleteTextView.setListSelection (position);
                    }
                    autoCompleteTextView.setAdapter(arrayAdapter);

                    autoCompleteTextView.setOnItemClickListener((parent, view, position1, id) -> {
                        // retrieve ArrayAdapter with parent.getAdapter()
                        DropdownItem item = (DropdownItem) parent.getAdapter().getItem(position1);
                        agentId = item.getId();
                        propertyEditViewModel.rememberValue(FieldKey.AGENT_ID, Long.toString(agentId));
                        propertyEditViewModel.rememberValue(FieldKey.AGENT_NAME, item.getName());
                        // to check input
                        checkAllValues();
                    });
                }
            }
            // load dropdown property types list
            if (viewState.getPropertyTypeItems() != null){
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutPropertyType.getEditText();
                if (autoCompleteTextView != null){
                    autoCompleteTextView.setAdapter(new ArrayAdapter(getContext(), R.layout.list_item, viewState.getPropertyTypeItems()));
                    // update list position with current property type
                    int position = findDropdownPositionById(currentPropertyTypeId, viewState.getPropertyTypeItems());
                    if (position >= 0) {
                        autoCompleteTextView.setListSelection (position);
                        propertyTypeId = currentPropertyTypeId;
                    }

                    autoCompleteTextView.setOnItemClickListener((parent, view, position12, id) -> {
                        DropdownItem item = (DropdownItem) parent.getAdapter().getItem(position12);
                        propertyTypeId = item.getId();
                        propertyEditViewModel.rememberValue(FieldKey.PROPERTY_TYPE_ID, Long.toString(propertyTypeId));
                        propertyEditViewModel.rememberValue(FieldKey.PROPERTY_TYPE_NAME, item.getName());
                        // to check input
                        checkAllValues();
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
        propertyEditViewModel.getViewStateLiveData(this.propertyId).observe(getViewLifecycleOwner(), propertyEditViewState -> {
            setAddressTitle(propertyEditViewState.getAddressTitle());
            setAddress(propertyEditViewState.getAddress());
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
        propertyEditViewModel.getOnCheckAgentIdValueLiveData().observe(getViewLifecycleOwner(), fieldState -> setErrorEnabledLayout(textInputLayoutAgent, fieldState));
    }

    /**
     * setErrorEnabled true or false when property type id was checked
     */
    private void configureControlPropertyTypeId(){
        propertyEditViewModel.getOnCheckPropertyTypeIdValueLiveData().observe(getViewLifecycleOwner(), fieldState -> setErrorEnabledLayout(textInputLayoutPropertyType, fieldState));
    }

    /**
     * button ok : setEnabled true or false, depends on all values
     */
    private void configureControlAllValues(){
        propertyEditViewModel.getOnCheckAllValuesLiveData().observe(getViewLifecycleOwner(), aBoolean -> buttonOk.setEnabled(aBoolean));
    }

    /**
     * show or hide error on component
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
     */
    private void configureControlValue(TextInputLayout textInputLayout,
                                  LiveData<FieldState> getLiveData){

        Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
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

        getLiveData.observe(getViewLifecycleOwner(), fieldState -> setErrorEnabledLayout(textInputLayout, fieldState));

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
        return Objects.requireNonNull(textInputLayoutAddressTitle.getEditText()).getText().toString().trim();
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
        Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener(textWatcher);
    }

    private void removeCacheListener(TextInputLayout textInputLayout, TextWatcher textWatcher){
        Objects.requireNonNull(textInputLayout.getEditText()).removeTextChangedListener(textWatcher);
    }

    private void setValueToComponent(String value, TextInputLayout textInputLayout, TextWatcher cacheListener){
        // Don't want to remember initial value in cache
        removeCacheListener(textInputLayout, cacheListener);
        Objects.requireNonNull(textInputLayout.getEditText()).setText(value);
        // We can activate the cache after saving the value in the component
        addCacheListener(textInputLayout, cacheListener);
    }

    private final TextWatcher addressTitleCacheListener = createTextWatcher(FieldKey.ADDRESS_TITLE);
    private void setAddressTitle(String addressTitle){
        setValueToComponent(addressTitle, textInputLayoutAddressTitle, addressTitleCacheListener);
    }

    private String getAddress(){
        return Objects.requireNonNull(textInputLayoutAddress.getEditText()).getText().toString().trim();
    }

    private final TextWatcher addressCacheListener = createTextWatcher(FieldKey.ADDRESS);
    private void setAddress(String address){
        setValueToComponent(address, textInputLayoutAddress, addressCacheListener);
    }

    private long getAgentId(){
        return this.agentId;
    }

    private void setAgentId(long id, String name){
        this.agentId = id;
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutAgent.getEditText();
        Objects.requireNonNull(autoCompleteTextView).setText(name);
    }

    private long getPropertyTypeId(){
        return this.propertyTypeId;
    }

    private void setPropertyTypeId(long id, String name){
        this.propertyTypeId = id;
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutPropertyType.getEditText();
        Objects.requireNonNull(autoCompleteTextView).setText(name);
    }

    private String getPrice(){
        return Objects.requireNonNull(textInputLayoutPrice.getEditText()).getText().toString().trim();
    }

    private final TextWatcher priceCacheListener = createTextWatcher(FieldKey.PRICE);
    private void setPrice(String price){
        setValueToComponent(price, textInputLayoutPrice, priceCacheListener);
    }

    private String getSurface(){
        return Objects.requireNonNull(textInputLayoutSurface.getEditText()).getText().toString().trim();
    }

    private final TextWatcher surfaceCacheListener = createTextWatcher(FieldKey.SURFACE);
    private void setSurface(String surface){
        setValueToComponent(surface, textInputLayoutSurface, surfaceCacheListener);
    }

    private String getRooms(){
        return Objects.requireNonNull(textInputLayoutRooms.getEditText()).getText().toString().trim();
    }

    private final TextWatcher roomsCacheListener = createTextWatcher(FieldKey.ROOMS);
    private void setRooms(String rooms){
        setValueToComponent(rooms, textInputLayoutRooms, roomsCacheListener);
    }

    private String getDescription(){
        return Objects.requireNonNull(textInputLayoutDescription.getEditText()).getText().toString().trim();
    }

    private final TextWatcher descriptionCacheListener = createTextWatcher(FieldKey.DESCRIPTION);
    private void setDescription(String description){
        setValueToComponent(description, textInputLayoutDescription, descriptionCacheListener);
    }

    private String getPointOfInterest(){
        return Objects.requireNonNull(textInputLayoutPointOfInterest.getEditText()).getText().toString().trim();
    }

    private final TextWatcher pointOfInterestCacheListener = createTextWatcher(FieldKey.POINT_OF_INTEREST);
    private void setPointOfInterest(String pointOfInterest){
        setValueToComponent(pointOfInterest, textInputLayoutPointOfInterest, pointOfInterestCacheListener);
    }

    private String getEntryDate() {
        return Objects.requireNonNull(textInputLayoutEntryDate.getEditText()).getText().toString().trim();
    }

    private final TextWatcher entryDateCacheListener = createTextWatcher(FieldKey.ENTRY_DATE);
    private void setEntryDate(String entryDate){
        Log.d(Tag.TAG, "PropertyEditFragment.setEntryDate() called with: entryDate = [" + entryDate + "]");
        setValueToComponent(entryDate, textInputLayoutEntryDate, entryDateCacheListener);
    }

    private String getSaleDate() {
        return Objects.requireNonNull(textInputLayoutSaleDate.getEditText()).getText().toString().trim();
    }

    private final TextWatcher saleDateCacheListener = createTextWatcher(FieldKey.SALE_DATE);
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
                propertyId -> {
                    // when data are ok go back to main activity to close form and navigate
                    callbackEditProperty.onValidateEditProperty(propertyId);
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
        selectFromGallery.launch(new String[] {"image/*"});
    }

    final ActivityResultLauncher<String[]> selectFromGallery = registerForActivityResult(
            new ActivityResultContracts.OpenMultipleDocuments(),
            this::onActivityResultFromGallery);

    private void onActivityResultFromGallery(List<Uri> result) {
        for (Uri uri : result) {
            //getContext().getContentResolver().takePersistableUriPermission(uri, permissionFlags);
            Log.d(Tag.TAG, "PropertyEditFragment.onActivityResult() called with: result = [" + result + "]");
            addPhoto(uri, requireActivity().getString(R.string.image_from_gallery));
        }
    }

    private void takePicture(){
        latestUri = FileProviderHelper.createFileUri();
        takePictureLauncher.launch(latestUri);
    }

    private Uri latestUri = null;

    final ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            this::onActivityResultFromPictureLauncher);

    private void onActivityResultFromPictureLauncher(Boolean result) {
        Log.d("TAG", "PropertyEditFragment.onActivityResult() called with: uri = [" + latestUri + "]");
        addPhoto(latestUri, requireActivity().getString(R.string.picture_from_camera));
    }

    private void addPhoto(Uri uri, String caption){
        Log.d(Tag.TAG, "PropertyEditFragment.addPhoto() called with: uri = [" + uri + "], caption = [" + caption + "]");
        propertyEditViewModel.addPhoto(uri, caption, propertyId);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        Log.d(Tag.TAG, "PropertyEditFragment.onCreateContextMenu() called with: menu = [" + menu + "], v = [" + v + "], menuInfo = [" + menuInfo + "]");
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.fragment_property_edit_context_menu_photo, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Log.d(Tag.TAG, "PropertyEditFragment.onContextItemSelected() called with: item = [" + item + "]");
        if (item.getItemId() == R.id.fragment_property_edit_context_menu_photo_delete) {
            confirmDeletePhoto();
            return true;
        }
        else {
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
