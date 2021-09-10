package com.openclassrooms.realestatemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.viewmodel.MainViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodelfactory.MainViewModelFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private TextView textViewQuantity;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
        configureViewModel();
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

    private void configureViewModel(){
        mainViewModel = new ViewModelProvider( this, MainViewModelFactory.getInstance())
                .get(MainViewModel.class);

        mainViewModel.load();
        mainViewModel.getAgentLiveData().observe(this, new Observer<List<Agent>>() {
            @Override
            public void onChanged(List<Agent> agents) {
                Log.d(Tag.TAG, "onChanged() called with: agents = [" + agents + "]");
                for (Agent agent : agents) {
                    Log.d(Tag.TAG, "agent.id=" + agent.getId() + " agent.name=" + agent.getName());
                }
            }
        });
        mainViewModel.getPropertysLiveData().observe(this, new Observer<List<Property>>() {
            @Override
            public void onChanged(List<Property> properties) {
                Log.d(Tag.TAG, "onChanged() called with: properties = [" + properties + "]");
                for (Property property : properties) {
                    Log.d(Tag.TAG, "property.id=" + property.getId() +
                            " property.price=" + property.getPrice() +
                            " property.description=" + property.getDescription().substring(0, 20));
                }

            }
        });
    }
}
