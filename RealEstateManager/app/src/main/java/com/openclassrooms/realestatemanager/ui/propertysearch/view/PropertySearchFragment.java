package com.openclassrooms.realestatemanager.ui.propertysearch.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertysearch.listener.PropertySearchListener;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel.PropertySearchViewModel;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;
import com.openclassrooms.realestatemanager.ui.view_model_factory.AppViewModelFactory;
import com.openclassrooms.realestatemanager.utils.DateRangePickedHelper;
import com.openclassrooms.realestatemanager.utils.Utils;

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
    private TextInputLayout textInputLayoutFullText;
    private TextInputLayout textInputLayoutAgents;
    private TextInputLayout textInputLayoutPropertyTypes;

    private TextView textViewRangePrice;
    private RangeSlider rangeSliderPrice;

    private TextView textViewRangeSurface;
    private RangeSlider rangeSliderSurface;

    private TextView textViewRangeRooms;
    private RangeSlider rangeSliderRooms;

    private Button buttonOk;
    private Button buttonReset;

    TextView textViewEntryDateRange;
    TextView textViewSaleDateRange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag.TAG, "PropertySearch Fragment onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(Tag.TAG, "PropertySearch Fragment onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_search, container, false);
        configureComponents(view);
        configureViewModel();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(Tag.TAG, "PropertySearch Fragment onAttach() called with: context = [" + context + "]");
        this.createCallbackToParentActivity();
    }

    private void createCallbackToParentActivity() {
        try {
            propertySearchListener = (PropertySearchListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + "must implement PropertyEditListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Tag.TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Tag.TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Tag.TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Tag.TAG, "onStop() called");
    }

    private void configureComponents(View view) {
        Log.d(Tag.TAG, "PropertySearch Fragment configureComponents() called with: view = [" + view + "]");

        textInputLayoutFullText = view.findViewById(R.id.fragment_property_search_text_input_layout_fulltext);
        textInputLayoutFullText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                Log.d(Tag.TAG, "PropertySearch Fragment afterTextChanged() called with: s = [" + s + "]");
                propertySearchViewModel.afterFullTextChanged(text);
            }
        });

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

        textViewEntryDateRange = view.findViewById(R.id.fragment_property_search_entry_date_range);
        textViewSaleDateRange = view.findViewById(R.id.fragment_property_search_sale_date_range);

        configurePriceComponents(view);
        configureSurfaceComponents(view);
        configureRoomsComponents(view);
        configureDateComponents(view);
    }

    private void configureDateComponents(View view) {
        Button buttonSelectEntryDate = view.findViewById(R.id.fragment_property_search_button_select_entry_date);
        buttonSelectEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEntryDate();
            }
        });

        Button buttonResetEntryDate = view.findViewById(R.id.fragment_property_search_button_reset_entry_date);
        buttonResetEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDate(textViewEntryDateRange);
            }
        });

        Button buttonSelectSaleDate = view.findViewById(R.id.fragment_property_search_button_select_sale_date);
        buttonSelectSaleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSaleDate();
            }
        });

        Button buttonResetSaleDate = view.findViewById(R.id.fragment_property_search_button_reset_sale_date);
        buttonResetSaleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDate(textViewSaleDateRange);
            }
        });
    }

    private void selectEntryDate(){
        DateRangePickedHelper.Show(getChildFragmentManager(), new DateRangePickedHelper.DateRangePickerHelperInterface() {
            @Override
            public void onValidate(Object selection) {
                propertySearchViewModel.setValueEntryDate(selection);
            }
        });
    }

    private void selectSaleDate(){
        DateRangePickedHelper.Show(getChildFragmentManager(), new DateRangePickedHelper.DateRangePickerHelperInterface() {
            @Override
            public void onValidate(Object selection) {
                propertySearchViewModel.setValueSaleDate(selection);
            }
        });
    }

    private void resetDate(TextView textView){
        textView.setText(getResources().getString(R.string.no_date_range_selected));
    }

    private void configurePriceComponents(View view) {
        textViewRangePrice = view.findViewById(R.id.fragment_property_search_price_range);
        rangeSliderPrice = view.findViewById(R.id.fragment_property_search_range_slider_price);
        rangeSliderPrice.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                Log.d(Tag.TAG, "PropertySearch fragment price onValueChange() called with: slider.getValues = [" + slider.getValues().get(0) + " and " + slider.getValues().get(1) );
                propertySearchViewModel.setPriceRange(slider.getValues());
            }
        });

        LabelFormatter priceFormater = new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                int price = (int)value;
                price = price * 1000;
                return Utils.convertPriceToString(price);
            }
        };
        rangeSliderPrice.setLabelFormatter(priceFormater);
    }

    private void configureSurfaceComponents(View view) {
        textViewRangeSurface = view.findViewById(R.id.fragment_property_search_surface_range);
        rangeSliderSurface = view.findViewById(R.id.fragment_property_search_range_slider_surface);
        rangeSliderSurface.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                Log.d(Tag.TAG, "PropertySearch fragment onValueChange() called with: slider.getValues = [" + slider.getValues().get(0) + " and " + slider.getValues().get(1) );
                propertySearchViewModel.setSurfaceRange(slider.getValues());
            }
        });

        LabelFormatter surfaceFormater = new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                int surface = (int)value;
                return Utils.convertSurfaceToString(surface);
            }
        };
        rangeSliderSurface.setLabelFormatter(surfaceFormater);
    }

    private void configureRoomsComponents(View view) {
        textViewRangeRooms = view.findViewById(R.id.fragment_property_search_rooms_range);
        rangeSliderRooms = view.findViewById(R.id.fragment_property_search_range_slider_rooms);
        rangeSliderRooms.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                Log.d(Tag.TAG, "PropertySearch fragment onValueChange() called with: slider.getValues = [" + slider.getValues().get(0) + " and " + slider.getValues().get(1) );
                propertySearchViewModel.setRoomsRange(slider.getValues());
            }
        });
    }

    private void configureViewModel() {
        Log.d(Tag.TAG, "PropertySearch Fragment configureViewModel() called");
        propertySearchViewModel = new ViewModelProvider(
                requireActivity(), AppViewModelFactory.getInstance())
                .get(PropertySearchViewModel.class);

        propertySearchViewModel.getViewState().observe(getViewLifecycleOwner(), new Observer<PropertySearchViewState>() {
            @Override
            public void onChanged(PropertySearchViewState propertySearchViewState) {
                Log.d(Tag.TAG, "PropertySearch Fragment observe -> onChanged() called with: propertySearchViewState = [" + propertySearchViewState + "]");
                Log.d(Tag.TAG, "PropertySearch Fragment observe -> onChanged() fullText = [" + propertySearchViewState.getFullText() + "]");
                setFullText(propertySearchViewState.getFullText());
                setAgents(propertySearchViewState.getAgents(), propertySearchViewState.getAgentIndex());
                setPropertyTypes(propertySearchViewState.getPropertyTypes(), propertySearchViewState.getPropertyTypeIndex());

                setRangeSliderValuesPrice(propertySearchViewState.getMinMaxPrice(), propertySearchViewState.getValuesPrice());
                setCaptionPrice(propertySearchViewState.getCaptionPrice());
                setRangeSliderValuesSurface(propertySearchViewState.getMinMaxSurface(), propertySearchViewState.getValuesSurface());
                setCaptionSurface(propertySearchViewState.getCaptionSurface());
                setRangeSliderValuesRooms(propertySearchViewState.getMinMaxRooms(), propertySearchViewState.getValuesRooms());
                setCaptionRooms(propertySearchViewState.getCaptionRooms());

                setCaptionEntryDate(propertySearchViewState.getCaptionEntryDate());
                setCaptionSaleDate(propertySearchViewState.getCaptionSaleDate());
            }
        });
    }

    private void setCaptionPrice(String text) {
        textViewRangePrice.setText(text);
    }

    private void setCaptionSurface(String text) {
        textViewRangeSurface.setText(text);
    }

    private void setCaptionRooms(String text) {
        textViewRangeRooms.setText(text);
    }

    private void setCaptionEntryDate(String text) { textViewEntryDateRange.setText(text); }

    private void setCaptionSaleDate(String text) {
        textViewSaleDateRange.setText(text);
    }

    private void setRangeSliderFromToValues(RangeSlider rangeSlider, List<Float> range){
        rangeSlider.setValueFrom(range.get(0));
        rangeSlider.setValueTo(range.get(1));
    }

    private void setRangeSliderValues(RangeSlider rangeSlider, List<Float> floats){
        if ((rangeSlider.getValues().get(0).floatValue() != floats.get(0).floatValue()) ||
                (rangeSlider.getValues().get(1).floatValue() != floats.get(1).floatValue()))
            rangeSlider.setValues(floats);
    }

    private void setRangeSliderValuesPrice(List<Float> range, List<Float> values) {
        setRangeSliderFromToValues(rangeSliderPrice, range);
        setRangeSliderValues(rangeSliderPrice, values);
    }

    private void setRangeSliderValuesSurface(List<Float> range, List<Float> values) {
        setRangeSliderFromToValues(rangeSliderSurface, range);
        setRangeSliderValues(rangeSliderSurface, values);
    }

    private void setRangeSliderValuesRooms(List<Float> range, List<Float> values) {
        setRangeSliderFromToValues(rangeSliderRooms, range);
        setRangeSliderValues(rangeSliderRooms, values);
    }

    private void setFullText(String text){
        Log.d(Tag.TAG, "PropertySearch Fragment setFullText() called with: text = [" + text + "]");
        String input = textInputLayoutFullText.getEditText().getText().toString();
        if ((text != null) && ( ! input.equals(text))) {
            textInputLayoutFullText.getEditText().setText(text);
            textInputLayoutFullText.getEditText().setSelection(text.length());
        }
    }

    private void setAgents(List<DropdownItem> agents, int index) {
        Log.d(Tag.TAG, "PropertySearch Fragment setAgents() called with: agents = [" + agents + "], index = [" + index + "]");
        if (agents != null){
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.list_item, agents);
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutAgents.getEditText();

            // must do setText before setAdapter
            if ((index >= 0) && (index <= arrayAdapter.getCount())) {
                DropdownItem item = (DropdownItem) arrayAdapter.getItem(index);
                this.agentId = item.getId();
                // set adapter to null to avoid autoComplete dropdown appearing when text is set
                autoCompleteTextView.setAdapter(null);
                if (!autoCompleteTextView.getText().toString().equals(item.getName()))
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
        Log.d(Tag.TAG, "PropertySearch Fragment setPropertyTypes() called with: propertyTypes = [" + propertyTypes + "], index = [" + index + "]");
        if (propertyTypes != null){
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.list_item, propertyTypes);
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayoutPropertyTypes.getEditText();
            if ((index >= 0) && (index <= arrayAdapter.getCount())) {
                DropdownItem item = (DropdownItem) arrayAdapter.getItem(index);
                this.propertyTypeId = item.getId();
                // set adapter to null to avoid autoComplete dropdown appearing when text is set
                autoCompleteTextView.setAdapter(null);
                if (!autoCompleteTextView.getText().toString().equals(item.getName()))
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

    private void resetForm() {
        propertySearchViewModel.resetSearch();
    }

    private void validateForm() {
        propertySearchViewModel.setSearchValues(this.agentId, this.propertyTypeId);
        if (propertySearchListener != null)
            propertySearchListener.onApplySearch();
    }
}