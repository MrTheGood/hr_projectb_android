package nl.hogeschoolrotterdam.projectb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.WhibApp;
import nl.hogeschoolrotterdam.projectb.util.SimpleOnItemSelectedListener;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class SettingsFragment extends Fragment {
    private static final int LIGHT = 0;
    private static final int DARK = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Spinner spinner = getView().findViewById(R.id.theme_selector);
        int themeId = WhibApp.getInstance().getThemeId();
        spinner.setSelection(
                themeId == R.style.AppTheme_Light ? LIGHT : DARK,
                false);
        spinner.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case LIGHT: // Light theme
                        WhibApp.getInstance().setCurrentTheme(R.style.AppTheme_Light, false);
                        requireActivity().recreate();
                        break;
                    case DARK: // Dark theme
                        WhibApp.getInstance().setCurrentTheme(R.style.AppTheme_Dark, true);
                        requireActivity().recreate();
                        break;
                }
            }
        });
    }
}
