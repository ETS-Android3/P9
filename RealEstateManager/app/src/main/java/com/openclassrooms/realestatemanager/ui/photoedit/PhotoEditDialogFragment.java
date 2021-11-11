package com.openclassrooms.realestatemanager.ui.photoedit;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertyedit.view.PropertyEditFragment;

public class PhotoEditDialogFragment extends DialogFragment {

    private String title;
    private long id;
    private int order;
    private String uri;
    private String caption;
    private long propertyId;

    private TextView textViewTitle;
    private ImageView imageViewPhoto;
    private TextInputLayout textInputLayoutCaption;
    private Button buttonOk;
    private Button buttonCancel;

    private OnPhotoEditListener photoEditListener;

    public PhotoEditDialogFragment setPhotoEditListener(OnPhotoEditListener photoEditListener) {
        this.photoEditListener = photoEditListener;
        return this;
    }

    public static PhotoEditDialogFragment newInstance(String title, long id, int order, String uri, String caption, long propertyId) {
        Log.d(Tag.TAG, "PhotoEditDialogFragment.newInstance() called with: title = [" + title + "], id = [" + id + "], order = [" + order + "], uri = [" + uri + "], caption = [" + caption + "], propertyId = [" + propertyId + "]");
        PhotoEditDialogFragment fragment = new PhotoEditDialogFragment();
        Bundle args = new Bundle();
        args.putString(PropertyConst.ARG_PHOTO_TITLE, title);
        args.putLong(PropertyConst.ARG_PHOTO_ID, id);
        args.putInt(PropertyConst.ARG_PHOTO_ORDER, order);
        args.putString(PropertyConst.ARG_PHOTO_URI, uri);
        args.putString(PropertyConst.ARG_PHOTO_CAPTION, caption);
        args.putLong(PropertyConst.ARG_PHOTO_PROPERTY_ID, propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PHOTO_TITLE))){
            title = getArguments().getString(PropertyConst.ARG_PHOTO_TITLE, "");
        } else {
            title = "";
        }

        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PHOTO_ID))){
            id = getArguments().getLong(PropertyConst.ARG_PHOTO_ID, 0);
        } else {
            id = 0;
        }

        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PHOTO_ORDER))){
            order = getArguments().getInt(PropertyConst.ARG_PHOTO_ID, 0);
        } else {
            order = 0;
        }

        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PHOTO_URI))){
            uri = getArguments().getString(PropertyConst.ARG_PHOTO_URI, "");
        } else {
            uri = "";
        }

        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PHOTO_CAPTION))){
            caption = getArguments().getString(PropertyConst.ARG_PHOTO_CAPTION, "");
        } else {
            caption = "";
        }

        if ((getArguments() != null) && (getArguments().containsKey(PropertyConst.ARG_PHOTO_PROPERTY_ID))){
            propertyId = getArguments().getLong(PropertyConst.ARG_PHOTO_PROPERTY_ID, 0);
        } else {
            propertyId = 0;
        }

        setTitle(title);
        setUri(uri);
        setCaption(caption);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.photo_edit_dialog_fragment, container, false);

        textViewTitle = view.findViewById(R.id.photo_edit_dialogue_fragment_textViewTitle);
        imageViewPhoto = view.findViewById(R.id.photo_edit_dialogue_fragment_imageViewPhoto);
        textInputLayoutCaption = view.findViewById(R.id.photo_edit_dialogue_fragment_text_input_layout_caption);
        buttonOk = view.findViewById(R.id.photo_edit_dialogue_fragment_button_ok);
        buttonCancel = view.findViewById(R.id.photo_edit_dialogue_fragment_button_cancel);

        if (photoEditListener == null) {
            buttonOk.setEnabled(false);
            textInputLayoutCaption.setEnabled(false);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(Tag.TAG, "PhotoEditDialogFragment.onClick() called with: v = [" + v + "]");
                    dismiss();
                    photoEditListener.onPhotoEditOk(id, order, uri, getCaption(), propertyId);
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    photoEditListener.onPhotoEditCancel();
                }
            });
        }

        textInputLayoutCaption.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.isEmpty()) {
                    buttonOk.setEnabled(false);
                    textInputLayoutCaption.setErrorEnabled(true);
                    textInputLayoutCaption.setError(getString(R.string.caption_required));
                } else {
                    buttonOk.setEnabled(true);
                    textInputLayoutCaption.setErrorEnabled(false);
                }
            }
        });

        return view;
    }

    private void setTitle(String title){
        textViewTitle.setText(title);
    }

    private void setUri(String uri) {
        if ((uri == null) || (uri.trim().isEmpty())) {
            // Clear picture
            Glide.with(imageViewPhoto.getContext())
                    .load("")
                    .placeholder(R.drawable.ic_house)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imageViewPhoto);
        } else {
            //load restaurant picture
            Glide.with(imageViewPhoto.getContext())
                    .load(uri)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(imageViewPhoto);
        }
    }

    private void setCaption(String caption) {
        textInputLayoutCaption.getEditText().setText(caption);
    }

    private String getCaption(){
        return textInputLayoutCaption.getEditText().getText().toString().trim();
    }
}
