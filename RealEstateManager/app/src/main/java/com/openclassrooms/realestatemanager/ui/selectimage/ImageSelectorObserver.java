package com.openclassrooms.realestatemanager.ui.selectimage;

import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.tag.Tag;

import java.util.List;

public class ImageSelectorObserver implements DefaultLifecycleObserver {
    private final ActivityResultRegistry mRegistry;
    private ActivityResultLauncher<String> mGetContent;
    private ActivityResultLauncher<String[]> mGetDocument;
    private ActivityResultLauncher<String[]> mGetMultipleDocuments;


    private final MutableLiveData<Uri> uriMutableLiveData = new MutableLiveData<>();
    public LiveData<Uri> getUriLiveData() { return uriMutableLiveData; }

    private final MutableLiveData<List<Uri>> multipleUrisMutableLiveData = new MutableLiveData<>();
    public LiveData<List<Uri>> getMultipleUrisLiveData() { return multipleUrisMutableLiveData; }

    public ImageSelectorObserver(@NonNull ActivityResultRegistry registry) {
        mRegistry = registry;
    }

    public void onCreate(@NonNull LifecycleOwner owner) {

        mGetContent = mRegistry.register("key", owner, new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        Log.d(Tag.TAG, "ImageSelectorObserver.onActivityResult() called with: uri = [" + uri + "]");
                        uriMutableLiveData.setValue(uri);
                        // uri = content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F25/ORIGINAL/NONE/1341937340
                    }
                });

        mGetDocument = mRegistry.register("key2", owner, new ActivityResultContracts.OpenDocument(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Log.d(Tag.TAG, "ImageSelectorObserver.OpenDocument() onActivityResult() called with: result = [" + result + "]");
                        uriMutableLiveData.setValue(result);
                    }
                });

        mGetMultipleDocuments = mRegistry.register("key3", owner, new ActivityResultContracts.OpenMultipleDocuments(),
                new ActivityResultCallback<List<Uri>>() {
                    @Override
                    public void onActivityResult(List<Uri> result) {
                        multipleUrisMutableLiveData.setValue(result);
                    }
                });
    }

    public void selectImage() {
        // Open the activity to select an image
        Log.d(Tag.TAG, "ImageSelectorObserver.selectImage() called");
        mGetContent.launch("image/*");
    }

    public void openImage() {
        // Open the activity to select an image
        Log.d(Tag.TAG, "ImageSelectorObserver.selectImage() called");
        mGetDocument.launch(new String[] {"image/*"});
    }

    public void openMultipleImages() {
        Log.d(Tag.TAG, "ImageSelectorObserver.openMultipleImages() called");
        //mGetFiles.launch(new String[] {"image/*"});
        mGetMultipleDocuments.launch(new String[] {"image/*"});
    }

}
