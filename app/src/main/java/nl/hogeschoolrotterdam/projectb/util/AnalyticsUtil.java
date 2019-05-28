package nl.hogeschoolrotterdam.projectb.util;

import android.content.Context;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import nl.hogeschoolrotterdam.projectb.WhibApp;

/**
 * Created by maartendegoede on 30/04/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class AnalyticsUtil {

    public static void share(Context context, String shareApp) {
        Bundle bundle = new Bundle();
        bundle.putString("share_app", shareApp);
        logEvent(context, FirebaseAnalytics.Event.SHARE, bundle);
    }

    public static void search(Context context) {
        logEvent(context, FirebaseAnalytics.Event.SEARCH, new Bundle());
    }

    public static void changeTheme(Context context, String theme) {
        Bundle bundle = new Bundle();
        bundle.putString("theme", theme);
        logEvent(context, "change_theme", bundle);
    }

    public static void selectContent(Context context, String variant) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, variant);
        logEvent(context, FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }


    public static void addContent(Context context, String variant) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, variant);
        logEvent(context, "add_content", bundle);
    }

    public static void disableCrashlytics(Context context, boolean value) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, String.valueOf(value));

        // executed even if analytics are disabled
        FirebaseAnalytics.getInstance(context).logEvent("disable_crashlytics", bundle);
    }

    public static void disableAnalytics(Context context, boolean value) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, String.valueOf(value));

        // executed even if analytics are disabled
        FirebaseAnalytics.getInstance(context).logEvent("disable_analytics", bundle);
    }

    public static void cancelEditContent(Context context) {
        logEvent(context, "cancel_edit_content", new Bundle());
    }

    public static void cancelAddContent(Context context) {
        logEvent(context, "cancel_add_content", new Bundle());
    }


    public static void editContent(Context context) {
        logEvent(context, "edit_content", new Bundle());
    }

    public static void deleteContent(Context context) {
        logEvent(context, "delete_content", new Bundle());
    }

    private static void logEvent(Context context, String event, Bundle bundle) {
        if (!WhibApp.getInstance().isAnalyticsDisabled())
            FirebaseAnalytics.getInstance(context).logEvent(event, bundle);
    }
}
