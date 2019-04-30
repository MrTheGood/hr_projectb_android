package nl.hogeschoolrotterdam.projectb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
        Button applyButton = getView().findViewById(R.id.theme_apply);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyTheme();
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
        }
    }
}
