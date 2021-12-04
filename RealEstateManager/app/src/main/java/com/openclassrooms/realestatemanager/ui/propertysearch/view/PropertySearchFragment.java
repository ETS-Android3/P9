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

import com.google.android.material.slider.BasicLabelFormatter;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel.FieldKey;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertysearch.listener.PropertySearchListener;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel.PropertySearchViewModel;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;
import com.openclassrooms.realestatemanager.ui.view_model_factory.AppViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.NumberFormat;
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

    private Button buttonOk;
    private Button buttonReset;

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

        textViewRangePrice = view.findViewById(R.id.fragment_property_search_price_range);
        rangeSliderPrice = view.findViewById(R.id.fragment_property_search_range_slider_price);
        rangeSliderPrice.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                //Log.d(Tag.TAG, "PropertySearch fragment onStopTrackingTouch() called with: slider = [" + slider + "]");
            }
        });

        rangeSliderPrice.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                Log.d(Tag.TAG, "PropertySearch fragment onValueChange() called with: slider.getValues = [" + slider.getValues().get(0) + " and " + slider.getValues().get(1) );
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

    private void configureViewModel() {
        Log.d(Tag.TAG, "PropertySearch Fragment configureViewModel() called");
        propertySearchViewModel = new ViewModelProvider(
                requireActivity(), AppViewModelFactory.getInstance())
                .get(PropertySearchViewModel.class);

        propertySearchViewModel.getViewState().observe(getViewLifecycleOwner(), new Observer<PropertySearchViewState>() {
            @Override
            public void onChanged(PropertySearchViewState propertySearchViewState) {
                Log.d(Tag.TAG, "PropertySearch VM observe -> onChanged() called with: propertySearchViewState = [" + propertySearchViewState + "]");
                Log.d(Tag.TAG, "PropertySearch VM observe -> onChanged() fullText = [" + propertySearchViewState.getFullText() + "]");
                setAgents(propertySearchViewState.getAgents(), propertySearchViewState.getAgentIndex());
                setPropertyTypes(propertySearchViewState.getPropertyTypes(), propertySearchViewState.getPropertyTypeIndex());

                setFullText(propertySearchViewState.getFullText());
            }
        });

        propertySearchViewModel.getPriceRangeLiveData().observe(getViewLifecycleOwner(), new Observer<List<Float>>() {
            @Override
            public void onChanged(List<Float> floats) {
                if ((rangeSliderPrice.getValues().get(0).floatValue() != floats.get(0).floatValue()) ||
                  (rangeSliderPrice.getValues().get(1).floatValue() != floats.get(1).floatValue()))
                    rangeSliderPrice.setValues(floats);
            }
        });

        propertySearchViewModel.getPriceRangeCaptionLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textViewRangePrice.setText(s);
            }
        });
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
        setFullText("");
        propertySearchViewModel.resetSearch();
    }

    private void validateForm() {
        propertySearchViewModel.setSearchValues(this.agentId, this.propertyTypeId);
        //if (propertySearchListener != null)
        //    propertySearchListener.onApplySearch();
    }
}