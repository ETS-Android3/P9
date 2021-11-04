package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.openclassrooms.realestatemanager.R;

import java.util.logging.LogManager;

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
            .setMessage(R.string.confirm_delete_photo)

            .setPositiveButton(R.string.ok, (dialog, which) -> {
                if (listener != null) {
                    listener.onConfirmDeletePhoto();
                }
            })

            .setNegativeButton(R.string.no, (dialog, which) -> {
                if (listener != null) {
                    listener.onCancelDeletePhoto();
                }
            })

            .create();
    }

    public static String TAG = "ConfirmationDeletePhotoDialogFragment";

}
