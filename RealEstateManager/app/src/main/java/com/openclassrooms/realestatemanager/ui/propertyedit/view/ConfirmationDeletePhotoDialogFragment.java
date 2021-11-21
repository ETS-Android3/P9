package com.openclassrooms.realestatemanager.ui.propertyedit.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.propertyedit.listener.ConfirmationDeletePhotoListener;

public class ConfirmationDeletePhotoDialogFragment extends DialogFragment  {

    private ConfirmationDeletePhotoListener listener;
    public void setListener(ConfirmationDeletePhotoListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_delete_photo)
            .setMessage(R.string.please_confirm_delete_photo)

            .setPositiveButton(R.string.confirm_delete_photo, (dialog, which) -> {
                if (listener != null) {
                    listener.onConfirmDeletePhoto();
                }
            })

            .setNegativeButton(R.string.cancel, (dialog, which) -> {
                if (listener != null) {
                    listener.onCancelDeletePhoto();
                }
            })
            .create();
    }

    public static String TAG = "ConfirmationDeletePhotoDialogFragment";

}
