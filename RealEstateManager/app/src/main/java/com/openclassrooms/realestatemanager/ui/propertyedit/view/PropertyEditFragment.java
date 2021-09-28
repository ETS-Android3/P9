package com.openclassrooms.realestatemanager.ui.propertyedit.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertydetail.view.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.PropertyEditListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PropertyEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PropertyEditFragment extends Fragment {

    long propertyId;

    private PropertyEditListener callbackEditProperty;

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
        return view;
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
                callbackEditProperty.onCancel(this.propertyId);
                return true;
            case R.id.fragment_property_edit_ok:
                callbackEditProperty.onValidate(this.propertyId);
                return true;
            case R.id.fragment_property_edit_sell:
                callbackEditProperty.onSell(this.propertyId);
                return true;
        }
        return false;
    }
}