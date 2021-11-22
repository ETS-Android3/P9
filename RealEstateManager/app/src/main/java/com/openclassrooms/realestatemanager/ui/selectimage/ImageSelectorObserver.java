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
    private final MutableLiveData<List<Uri>> multipleUrisMutableLiveData = new MutableLiveData<>();
    public LiveData<List<Uri>> getMultipleUrisLiveData() { return multipleUrisMutableLiveData; }
    private ActivityResultLauncher<String[]> mGetMultipleDocuments;

    private LifecycleOwner owner;

    public ImageSelectorObserver(@NonNull ActivityResultRegistry registry) {
        super();
        mRegistry = registry;
    }

    public void onCreate(@NonNull LifecycleOwner owner) {
        this.owner = owner;
        Log.d(Tag.TAG, "ImageSelectorObserver.onCreate() called with: owner = [" + owner + "]");
        mGetMultipleDocuments = mRegistry.register("key", owner, new ActivityResultContracts.OpenMultipleDocuments(),
                new ActivityResultCallback<List<Uri>>() {
                    @Override
                    public void onActivityResult(List<Uri> result) {
                        Log.d(Tag.TAG, "ImageSelectorObserver.OpenMultipleDocuments -> onActivityResult() called with: result = [" + result + "]");
                        multipleUrisMutableLiveData.setValue(result);
                    }
                });
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.d(Tag.TAG, "ImageSelectorObserver.onPause() called with: owner = [" + owner + "]");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d(Tag.TAG, "ImageSelectorObserver.onStop() called with: owner = [" + owner + "]");
        mGetMultipleDocuments.unregister();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.d(Tag.TAG, "ImageSelectorObserver.onDestroy() called with: owner = [" + owner + "]");
    }

    public void openMultipleImages() {
        Log.d(Tag.TAG, "ImageSelectorObserver.openMultipleImages() called");
        //mGetFiles.launch(new String[] {"image/*"});
        mGetMultipleDocuments.launch(new String[] {"image/*"});
    }
}
