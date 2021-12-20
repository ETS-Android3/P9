package com.openclassrooms.realestatemanager.ui.loancalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.utils.LoanCalculator;

public class LoanCalculatorViewModel extends ViewModel {

    private final MutableLiveData<Float> amountMutableLiveData = new MutableLiveData<>();
    public void setAmount(float value){
        amountMutableLiveData.setValue(value);
    }

    private final MutableLiveData<Float> rateMutableLiveData = new MutableLiveData<>();
    public void setRate(float value){
        rateMutableLiveData.setValue(value);
    }

    private final MutableLiveData<Float> durationMutableLiveData = new MutableLiveData<>();
    public void setDuration(float value){
        durationMutableLiveData.setValue(value);
    }

    private final MediatorLiveData<LoanCalculatorViewState> viewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<LoanCalculatorViewState> getViewStateLiveData(){
        return viewStateMediatorLiveData;
    }

    public LoanCalculatorViewModel() {
        configureMediatorLiveData();
    }

    private void configureMediatorLiveData() {
        viewStateMediatorLiveData.addSource(amountMutableLiveData, aFloat -> combine(aFloat, rateMutableLiveData.getValue(), durationMutableLiveData.getValue()));

        viewStateMediatorLiveData.addSource(rateMutableLiveData, aFloat -> combine(amountMutableLiveData.getValue(), aFloat, durationMutableLiveData.getValue()));

        viewStateMediatorLiveData.addSource(durationMutableLiveData, aFloat -> combine(amountMutableLiveData.getValue(), rateMutableLiveData.getValue(), aFloat));
    }

    private void combine(Float amount, Float rate, Float duration){
        if ((amount == null) && (rate == null) && (duration == null))
            return;

        if (amount == null) amount = 0f;
        if (rate == null) rate = 0f;
        if (duration == null) duration = 0f;

        String strAmount = LoanCalculatorUtils.formatAmount(amount);
        String strRate = LoanCalculatorUtils.formatRate(rate);
        String strDuration = LoanCalculatorUtils.formatDuration(duration);

        LoanCalculator calculator = new LoanCalculator();
        double payment = calculator.calculateMonthlyPayment(amount, rate, duration.intValue());
        String strPayment = LoanCalculatorUtils.formatPayment(payment);

        LoanCalculatorViewState viewState = new LoanCalculatorViewState(strPayment, strAmount, strRate, strDuration,
            amount, rate, duration);
        viewStateMediatorLiveData.setValue(viewState);
    }
}
