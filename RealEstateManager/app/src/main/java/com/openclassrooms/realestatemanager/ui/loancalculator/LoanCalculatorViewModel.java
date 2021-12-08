package com.openclassrooms.realestatemanager.ui.loancalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.LoanCalculator;
import com.openclassrooms.realestatemanager.utils.Utils;

public class LoanCalculatorViewModel extends ViewModel {

    private MutableLiveData<Float> amountMutableLiveData = new MutableLiveData<>();
    public void setAmount(float value){
        amountMutableLiveData.setValue(value);
    }

    private MutableLiveData<Float> rateMutableLiveData = new MutableLiveData<>();
    public void setRate(float value){
        rateMutableLiveData.setValue(value);
    }

    private MutableLiveData<Float> durationMutableLiveData = new MutableLiveData<>();
    public void setDuration(float value){
        durationMutableLiveData.setValue(value);
    }

    private MediatorLiveData<LoanCalculatorViewState> viewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<LoanCalculatorViewState> getViewStateLiveData(){
        return viewStateMediatorLiveData;
    }

    public LoanCalculatorViewModel() {
        configureMediatorLiveData();
    }

    private void configureMediatorLiveData() {
        viewStateMediatorLiveData.addSource(amountMutableLiveData, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                combine(aFloat, rateMutableLiveData.getValue(), durationMutableLiveData.getValue());
            }
        });

        viewStateMediatorLiveData.addSource(rateMutableLiveData, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                combine(amountMutableLiveData.getValue(), aFloat, durationMutableLiveData.getValue());
            }
        });

        viewStateMediatorLiveData.addSource(durationMutableLiveData, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                combine(amountMutableLiveData.getValue(), rateMutableLiveData.getValue(), aFloat);
            }
        });
    }

    private void combine(Float amount, Float rate, Float duration){
        if ((amount == null) || (rate == null) || (duration == null))
            return;

        String strAmount = LoanCalculatorUtils.formatAmount(amount);
        String strRate = LoanCalculatorUtils.formatRate(rate);
        String strDuration = LoanCalculatorUtils.formatDuration(duration);

        LoanCalculator calculator = new LoanCalculator();
        Double payment = calculator.calculateMonthlyPayment(amount, rate, duration.intValue());
        String strPayment = LoanCalculatorUtils.formatPayment(payment);

        LoanCalculatorViewState viewState = new LoanCalculatorViewState(strPayment, strAmount, strRate, strDuration,
            amount, rate, duration);
        viewStateMediatorLiveData.setValue(viewState);
    }
}
