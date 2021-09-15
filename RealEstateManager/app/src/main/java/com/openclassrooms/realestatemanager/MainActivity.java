package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodelfactory.PropertyDetailViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewstate.PropertyDetailViewState;
import com.openclassrooms.realestatemanager.ui.propertylist.viewmodel.PropertyListViewModel;
import com.openclassrooms.realestatemanager.ui.propertylist.viewmodelfactory.PropertyListViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertylist.viewstate.PropertyListViewState;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private TextView textViewQuantity;

    private PropertyListViewModel propertyListViewModel;
    private PropertyDetailViewModel propertyDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
        configureListViewModel();
        configureDetailViewModel();

    }

    private void configureTextViewMain(){
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity(){
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText(String.format("%d", quantity));
    }

    private void configureListViewModel(){
        propertyListViewModel = new ViewModelProvider( this, PropertyListViewModelFactory.getInstance())
                .get(PropertyListViewModel.class);

        //propertyListViewModel.load();
        propertyListViewModel.getViewState().observe(this, new Observer<PropertyListViewState>() {
            @Override
            public void onChanged(PropertyListViewState propertyListViewState) {
                for (Agent agent : propertyListViewState.getAgents()) {
                    Log.d(Tag.TAG, "agent.id=" + agent.getId() + " agent.name=" + agent.getName());
                }
                for (PropertyType type : propertyListViewState.getTypes()){
                    Log.d(Tag.TAG, "type.id=" + type.getId() + " type.name=" + type.getName());
                }
                for (Property property : propertyListViewState.getProperties()){
                    Log.d(Tag.TAG, "property.id=" + property.getId() +
                            " property.price=" + property.getPrice() +
                            " property.description=" + property.getDescription().substring(0, 20));
                }
            }
        });
    }

    private void configureDetailViewModel(){
        propertyDetailViewModel = new ViewModelProvider( this, PropertyDetailViewModelFactory.getInstance())
                .get(PropertyDetailViewModel.class);

        //propertyListViewModel.load();
        propertyDetailViewModel.getViewState().observe(this, new Observer<PropertyDetailViewState>() {
            @Override
            public void onChanged(PropertyDetailViewState propertyDetailViewState) {
                Log.d(Tag.TAG, "agent.id = " + propertyDetailViewState.getAgent().getId());
                Log.d(Tag.TAG, "agent.name = " + propertyDetailViewState.getAgent().getName());

                Log.d(Tag.TAG, "type.id = " + propertyDetailViewState.getPropertyType().getId());
                Log.d(Tag.TAG, "type.name = " + propertyDetailViewState.getPropertyType().getName());


                Log.d(Tag.TAG, "" + propertyDetailViewState.getProperty().getId());
                Log.d(Tag.TAG, "" + propertyDetailViewState.getProperty().getPrice());
                Log.d(Tag.TAG, "" + propertyDetailViewState.getProperty().getSurface());
                Log.d(Tag.TAG, "PointsOfInterest = " + propertyDetailViewState.getProperty().getPointsOfInterest());

            }
        });
        //propertyDetailViewModel.load(1);
        propertyDetailViewModel.load(2);
    }
}
