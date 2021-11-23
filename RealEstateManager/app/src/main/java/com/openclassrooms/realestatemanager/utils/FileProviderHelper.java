package com.openclassrooms.realestatemanager.utils;

import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.openclassrooms.realestatemanager.MainApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileProviderHelper {
    private static final String SUB_DIRECTORY = "picFromCamera";
    private static final String DATE_PATTERN = "yyyy-MM-dd-HH-mm-ss-SSS";

    private static String createFileName(){
        // create unique file name with date and time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        String strDate = simpleDateFormat.format(new Date());
        String fileName = SUB_DIRECTORY + "-" + strDate;
        Log.d("TAG", "FileProviderHelper.createFileName() = " + fileName);

        return fileName;
    }

    public static Uri createFileUri(){
        File file = new File(MainApplication.getApplication().getFilesDir(), createFileName());
        Uri uri = FileProvider.getUriForFile(MainApplication.getApplication(),
                MainApplication.getApplication().getApplicationContext().getPackageName() + ".provider", file);
        Log.d("TAG", "FileProviderHelper.createFileUri() = " + uri);
        return uri;
    }
}
