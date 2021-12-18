package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
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

        assertFalse(isInternetAvailable);
    }

    @Test
    public void isInternetAvailable_must_return_true_test(){
        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        ShadowNetworkInfo shadowOfActiveNetworkInfo = shadowOf(Objects.requireNonNull(connectivityManager).getActiveNetworkInfo());

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED);
        boolean isInternetAvailable = Utils.isInternetAvailable(MainApplication.getApplication());

        assertTrue(isInternetAvailable);
    }
}
