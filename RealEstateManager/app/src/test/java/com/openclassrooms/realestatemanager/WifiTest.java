package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.openclassrooms.realestatemanager.ui.main.view.MainActivity;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNetworkInfo;

import java.util.Objects;

@RunWith(RobolectricTestRunner.class)
public class WifiTest {

    @Test
    public void isInternetAvailable_must_return_false_test(){
        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        ShadowNetworkInfo shadowOfActiveNetworkInfo = shadowOf(Objects.requireNonNull(connectivityManager).getActiveNetworkInfo());
        // disconnect
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED);
        boolean isInternetAvailable = Utils.isInternetAvailable(MainApplication.getApplication());

        assertEquals(false, isInternetAvailable);
    }

    @Test
    public void isInternetAvailable_must_return_true_test(){
        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        ShadowNetworkInfo shadowOfActiveNetworkInfo = shadowOf(Objects.requireNonNull(connectivityManager).getActiveNetworkInfo());

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED);
        boolean isInternetAvailable = Utils.isInternetAvailable(MainApplication.getApplication());

        assertEquals(true, isInternetAvailable);
    }
}
