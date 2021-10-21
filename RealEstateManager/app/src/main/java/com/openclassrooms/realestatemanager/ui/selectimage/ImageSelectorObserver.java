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

import com.openclassrooms.realestatemanager.tag.Tag;

public class ImageSelectorObserver implements DefaultLifecycleObserver {
    private final ActivityResultRegistry mRegistry;
    private ActivityResultLauncher<String> mGetContent;
    private ImageSelectorObserverListener imageSelectorObserverListener;

    public interface ImageSelectorObserverListener{
        void onActivityResult(Uri uri);
    }
    public ImageSelectorObserver(@NonNull ActivityResultRegistry registry,
                                 ImageSelectorObserverListener imageSelectorObserverListener) {
        mRegistry = registry;
        this.imageSelectorObserverListener = imageSelectorObserverListener;
    }

    public void onCreate(@NonNull LifecycleOwner owner) {

        mGetContent = mRegistry.register("key", owner, new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        Log.d(Tag.TAG, "ImageSelectorObserver.onActivityResult() called with: uri = [" + uri + "]");
                        if (imageSelectorObserverListener != null) {
                            imageSelectorObserverListener.onActivityResult(uri);
                        }
                        // uri = content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F25/ORIGINAL/NONE/1341937340
                    }
                });
    }

    public void selectImage() {
        // Open the activity to select an image
        Log.d(Tag.TAG, "ImageSelectorObserver.selectImage() called");
        mGetContent.launch("image/*");
    }
}
