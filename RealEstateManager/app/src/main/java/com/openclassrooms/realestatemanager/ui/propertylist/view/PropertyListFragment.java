package com.openclassrooms.realestatemanager.ui.propertylist.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertylist.listener.OnRowPropertyClickListener;
import com.openclassrooms.realestatemanager.ui.propertylist.viewmodel.PropertyListViewModel;
import com.openclassrooms.realestatemanager.ui.propertylist.viewmodelfactory.PropertyListViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertylist.viewstate.PropertyListViewState;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PropertyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PropertyListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    PropertyListAdapter propertyListAdapter;
    // PropertyListFragmentArgs
    // PropertyListFragmentDirections

    /**
     * this interface is for sending propertyId to MainActivity
     */
    private OnPropertyClickedListener mCallback;
    public interface OnPropertyClickedListener{
        public void onPropertyClicked(long propertyId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }

    private void createCallbackToParentActivity() {
        try {
            mCallback = (OnPropertyClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + "must implement OnPropertyClickedListener");
        }
    }

    private PropertyListViewModel propertyListViewModel;

    public PropertyListFragment() {
        // Required empty public constructor
    }

    public static PropertyListFragment newInstance() {
        PropertyListFragment fragment = new PropertyListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);
        configureRecyclerView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModel();
    }

    private void configureRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.fragment_property_list_recyclerview);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        propertyListAdapter = new PropertyListAdapter(getContext(), new OnRowPropertyClickListener() {
            @Override
            public void onClickRowProperty(long propertyId) {
                // send property id to activity
                Log.d(Tag.TAG, "PropertyListFragment.onClickRowProperty() called with: propertyId = [" + propertyId + "]");
                if (mCallback != null) {
                    mCallback.onPropertyClicked(propertyId);
                }
            }
        });

        recyclerView.setAdapter(propertyListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void configureViewModel() {
        propertyListViewModel = new ViewModelProvider(
                requireActivity(),PropertyListViewModelFactory.getInstance())
                .get(PropertyListViewModel.class);
        propertyListViewModel.getViewState().observe(getViewLifecycleOwner(), new Observer<PropertyListViewState>() {
            @Override
            public void onChanged(PropertyListViewState propertyListViewState) {
                ((PropertyListAdapter)recyclerView.getAdapter()).updateData(propertyListViewState.getRowPropertyViewStates());
            }
        });
        propertyListViewModel.load();
    }


}