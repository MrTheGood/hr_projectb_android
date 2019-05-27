package nl.hogeschoolrotterdam.projectb;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import static com.crashlytics.android.core.CrashlyticsCore.Builder;


/**
 * Created by maartendegoede on 2019-04-02.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class WhibApp extends Application {
    private static WhibApp instance;
    private int themeId = R.style.AppTheme_Light;
    private boolean isDarkTheme = false;
    private boolean crashlyticsDisabled = false;
    private boolean analyticsDisabled = false;

    public static WhibApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        analyticsDisabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("disableAnalytics", false);
        crashlyticsDisabled = BuildConfig.DEBUG || PreferenceManager.getDefaultSharedPreferences(this).getBoolean("disableCrashlytics", false);
        Fabric.with(this, new Crashlytics.Builder().core(new Builder().disabled(crashlyticsDisabled).build()).build());

        themeId = PreferenceManager.getDefaultSharedPreferences(this).getInt("themeId", R.style.AppTheme_Light);
        isDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isDarkTheme", false);
        setCurrentTheme(themeId, isDarkTheme);
    }

    public int getThemeId() {
        return themeId;
    }

    /**
     * Workaround for a bug in MaterialComponents
     */
    public void tintMenuItems(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable drawable = DrawableCompat.wrap(item.getIcon());
            tintDrawable(drawable);
            item.setIcon(drawable);
        }
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    /**
     * Workaround for a bug in MaterialComponents
     */
    public Drawable tintDrawable(Drawable drawable) {
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, isDarkTheme ? R.color.colorOnSurfaceDark : R.color.colorOnSurface));
        return drawable;
    }

    public void setCurrentTheme(@StyleRes int themeId, boolean isDarkTheme) {
        this.themeId = themeId;
        this.isDarkTheme = isDarkTheme;
        setTheme(themeId);
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putInt("themeId", themeId)
                .apply();
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean("isDarkTheme", isDarkTheme)
                .apply();
    }

    public boolean isAnalyticsDisabled() {
        return analyticsDisabled;
    }

    public void setAnalyticsDisabled(boolean value) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean("disableAnalytics", value)
                .apply();
        analyticsDisabled = value;
    }

    public boolean isCrashlyticsDisabled() {
        return crashlyticsDisabled;
    }

    public void setCrashlyticsDisabled(boolean value) {
        Fabric.with(this, new Crashlytics.Builder().core(new Builder().disabled(value).build()).build());
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean("disableCrashlytics", value)
                .apply();
        crashlyticsDisabled = value;
    }
}