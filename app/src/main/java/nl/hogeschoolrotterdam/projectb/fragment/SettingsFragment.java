package nl.hogeschoolrotterdam.projectb.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.hogeschoolrotterdam.projectb.BuildConfig;
import nl.hogeschoolrotterdam.projectb.OnboardingActivity;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.WhibApp;
import nl.hogeschoolrotterdam.projectb.util.AnalyticsUtil;
import nl.hogeschoolrotterdam.projectb.util.SimpleOnItemSelectedListener;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class SettingsFragment extends Fragment {
    private static final int LIGHT = 0;
    private static final int DARK = 1;
    private static final int LIGHT_PURPLE = 2;
    private static final int DARK_PURPLE = 3;
    private static final int LIGHT_RED = 4;
    private static final int DARK_RED = 5;
    private Spinner spinner;
    private ImageView previewImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        spinner = getView().findViewById(R.id.theme_selector);
        previewImage = getView().findViewById(R.id.theme_preview);
        final CheckBox enableAnalyticsBox = getView().findViewById(R.id.enable_analytics);
        final CheckBox enableCrashlyticsBox = getView().findViewById(R.id.enable_crashlytics);
        Button applyButton = getView().findViewById(R.id.settings_apply);
        Button privacyPolicyButton = getView().findViewById(R.id.privacy_policy);
        Button restartTutorialButton = getView().findViewById(R.id.restart_tutorial);
        Button openSourceButton = getView().findViewById(R.id.open_source_code);

        enableAnalyticsBox.setChecked(!WhibApp.getInstance().isAnalyticsDisabled());
        enableCrashlyticsBox.setChecked(!WhibApp.getInstance().isCrashlyticsDisabled());
        enableCrashlyticsBox.setEnabled(!BuildConfig.DEBUG);

        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.url_privacy_policy)));
                startActivity(i);
            }
        });
        openSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.url_github)));
                startActivity(i);
            }
        });
        restartTutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OnboardingActivity.class));
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("hasShownMapTooltipTutorial", false).apply();
            }
        });
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyTheme();

                boolean analyticsDisabled = !enableAnalyticsBox.isChecked();
                WhibApp.getInstance().setAnalyticsDisabled(analyticsDisabled);
                AnalyticsUtil.disableAnalytics(getContext(), analyticsDisabled);

                boolean crashlyticsDisabled = !enableCrashlyticsBox.isChecked();
                WhibApp.getInstance().setCrashlyticsDisabled(crashlyticsDisabled);
                AnalyticsUtil.disableCrashlytics(getContext(), crashlyticsDisabled);
            }
        });
        spinner.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                themeSelected(position);
            }
        });

        switch (WhibApp.getInstance().getThemeId()) {
            case R.style.AppTheme_Light:
                spinner.setSelection(LIGHT, true);
                AnalyticsUtil.changeTheme(getContext(), "AppTheme_Light");
                break;
            case R.style.AppTheme_Dark:
                spinner.setSelection(DARK, true);
                AnalyticsUtil.changeTheme(getContext(), "AppTheme_Dark");
                break;
            case R.style.AppTheme_Light_Purple:
                spinner.setSelection(LIGHT_PURPLE, true);
                AnalyticsUtil.changeTheme(getContext(), "AppTheme_Light_Purple");
                break;
            case R.style.AppTheme_Dark_Purple:
                spinner.setSelection(DARK_PURPLE, true);
                AnalyticsUtil.changeTheme(getContext(), "AppTheme_Dark_Purple");
                break;
            case R.style.AppTheme_Dark_Red:
                spinner.setSelection(DARK_RED, true);
                AnalyticsUtil.changeTheme(getContext(), "AppTheme_Dark_Red");
                break;
            case R.style.AppTheme_Light_Red:
                spinner.setSelection(LIGHT_RED, true);
                AnalyticsUtil.changeTheme(getContext(), "AppTheme_Light_Red");
                break;
        }
    }

    private void themeSelected(int theme) {
        switch (theme) {
            case LIGHT:
                previewImage.setImageResource(R.drawable.img_theme_light);
                break;
            case DARK:
                previewImage.setImageResource(R.drawable.img_theme_dark);
                break;
            case LIGHT_PURPLE:
                previewImage.setImageResource(R.drawable.img_theme_purple);
                break;
            case DARK_PURPLE:
                previewImage.setImageResource(R.drawable.img_theme_purple_dark);
                break;
            case DARK_RED:
                previewImage.setImageResource(R.drawable.img_theme_red_dark);
                break;
            case LIGHT_RED:
                previewImage.setImageResource(R.drawable.img_theme_red);
                break;
        }
    }

    private void applyTheme() {
        switch (spinner.getSelectedItemPosition()) {
            case LIGHT:
                WhibApp.getInstance().setCurrentTheme(R.style.AppTheme_Light, false);
                requireActivity().recreate();
                break;
            case DARK:
                WhibApp.getInstance().setCurrentTheme(R.style.AppTheme_Dark, true);
                requireActivity().recreate();
                break;
            case LIGHT_PURPLE:
                WhibApp.getInstance().setCurrentTheme(R.style.AppTheme_Light_Purple, false);
                requireActivity().recreate();
                break;
            case DARK_PURPLE:
                WhibApp.getInstance().setCurrentTheme(R.style.AppTheme_Dark_Purple, true);
                requireActivity().recreate();
                break;
            case LIGHT_RED:
                WhibApp.getInstance().setCurrentTheme(R.style.AppTheme_Light_Red, false);
                requireActivity().recreate();
                break;
            case DARK_RED:
                WhibApp.getInstance().setCurrentTheme(R.style.AppTheme_Dark_Red, true);
                requireActivity().recreate();
                break;
        }
    }
}
