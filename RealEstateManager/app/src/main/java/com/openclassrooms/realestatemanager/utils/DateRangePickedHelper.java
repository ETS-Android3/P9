package com.openclassrooms.realestatemanager.utils;

import android.util.Log;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.openclassrooms.realestatemanager.tag.Tag;

public class DateRangePickedHelper {

    public interface DateRangePickerHelperInterface {
        void onValidate(Object selection);
    }

    public static void Show(FragmentManager fragmentManager, DateRangePickerHelperInterface dateRangePickerHelperInterface){
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker rangePicker = builder.build();

        rangePicker.addOnPositiveButtonClickListener(selection -> {
            //selection = [Pair{1638835200000 1639526400000}]
            Log.d(Tag.TAG, "onPositiveButtonClick() called with: selection = [" + selection + "]");
            if (dateRangePickerHelperInterface != null)
                dateRangePickerHelperInterface.onValidate(selection);
        });

        rangePicker.show(fragmentManager, "MATERIAL_DATE_PICKER");
    }
}
