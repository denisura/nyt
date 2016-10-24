package com.github.denisura.nytseacher.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class IntentUtils {

    public static PendingIntent getPendingShareIntent(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        int requestCode = 100;

        return PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
