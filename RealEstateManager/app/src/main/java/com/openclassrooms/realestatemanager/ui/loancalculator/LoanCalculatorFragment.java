package com.openclassrooms.realestatemanager.ui.loancalculator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.ui.view_model_factory.AppViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

public class LoanCalculatorFragment extends Fragment {

    private Slider sliderAmount;
    private Slider sliderRate;
    private Slider sliderDuration;

    private TextView textViewsMonthlyPayment;
    private TextView textViewAmount;
    private TextView textViewRate;
    private TextView textViewDuration;

    LoanCalculatorViewModel viewModel;

    public LoanCalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loan_calculator, container, false);
        configureComponents(view);
        configureViewModel();
        return view;
    }

    private void configureComponents(View view){
        sliderAmount = view.findViewById(R.id.fragment_loan_slider_amount);
        configureSliderAmount();

        sliderRate = view.findViewById(R.id.fragment_loan_slider_rate);
        configureSliderRate();

        sliderDuration = view.findViewById(R.id.fragment_loan_slider_duration);
        configureSliderDuration();

        textViewsMonthlyPayment = view.findViewById(R.id.fragment_loan_text_view_payment_value);
        textViewAmount = view.findViewById(R.id.fragment_loan_text_view_amount_value);
        textViewRate = view.findViewById(R.id.fragment_loan_text_view_rate_value);
        textViewDuration = view.findViewById(R.id.fragment_loan_text_view_duration_value);
    }

    private void configureSliderAmount(){
        LabelFormatter formatter = new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return LoanCalculatorUtils.formatAmount(value);
            }
        };

        sliderAmount.setLabelFormatter(formatter);
        sliderAmount.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if (viewModel != null) viewModel.setAmount(value);
            }
        });
    }

    private void configureSliderRate(){
        LabelFormatter formatter = new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return LoanCalculatorUtils.formatRate(value);
            }
        };

        sliderRate.setLabelFormatter(formatter);
        sliderRate.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if (viewModel != null) viewModel.setRate(value);
            }
        });
    }

    private void configureSliderDuration(){
        LabelFormatter formatter = new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return LoanCalculatorUtils.formatDuration(value);
            }
        };

        sliderDuration.setLabelFormatter(formatter);
        sliderDuration.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if (viewModel != null) viewModel.setDuration(value);
            }
        });
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), AppViewModelFactory.getInstance())
                .get(LoanCalculatorViewModel.class);
        viewModel.getViewStateLiveData().observe(getViewLifecycleOwner(), new Observer<LoanCalculatorViewState>() {
            @Override
            public void onChanged(LoanCalculatorViewState loanCalculatorViewState) {
                setStrMonthlyPayment(loanCalculatorViewState.getStrMonthlyPayment());
                setStrAmount(loanCalculatorViewState.getStrAmount());
                setStrRate(loanCalculatorViewState.getStrRate());
                setStrDuration(loanCalculatorViewState.getStrDuration());
                setAmount(loanCalculatorViewState.getAmount());
                setRate(loanCalculatorViewState.getRate());
                setDuration(loanCalculatorViewState.getDuration());
            }
        });
    }

    private void setStrMonthlyPayment(String monthlyPayment) {
        textViewsMonthlyPayment.setText(monthlyPayment);
    }

    private void setStrAmount(String amount) {
        textViewAmount.setText(amount);
    }

    private void setStrRate(String rate) {
        textViewRate.setText(rate);
    }

    private void setStrDuration(String duration) {
        textViewDuration.setText(duration);
    }

    private void setAmount(float amount) {
        if (sliderAmount.getValue() != amount)
            sliderAmount.setValue(amount);

    }

    private void setRate(float rate) {
        if (sliderRate.getValue() != rate)
            sliderRate.setValue(rate);
    }

    private void setDuration(float duration) {
        if (sliderDuration.getValue() != duration)
            sliderDuration.setValue(duration);
    }
}