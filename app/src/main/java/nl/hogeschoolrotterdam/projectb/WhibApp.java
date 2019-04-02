package nl.hogeschoolrotterdam.projectb;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


/**
 * Created by maartendegoede on 2019-04-02.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class WhibApp extends Application {
    private static WhibApp instance;
    private int themeId = R.style.AppTheme_Light;
    private boolean isDarkTheme = false;

    public static WhibApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
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
                .putInt("defaultTheme", themeId)
                .apply();
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean("isDarkTheme", isDarkTheme)
                .apply();
    }
}