package nl.hogeschoolrotterdam.projectb;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import nl.hogeschoolrotterdam.projectb.adapter.OnboardingPageAdapter;

/**
 * Created by maartendegoede on 2019-05-22.
 * Copyright © 2019 insertCode.eu. All rights reserved.
 */
public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 onboardingPager;
    private Button nextButton;
    private Button skipButton;
    private boolean startMainActivityOnFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(WhibApp.getInstance().getThemeId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        startMainActivityOnFinish = getIntent().getExtras() != null && getIntent().getExtras().getBoolean("startMainActivity", false);


        onboardingPager = findViewById(R.id.onboarding_pager);
        nextButton = findViewById(R.id.onboarding_next);
        skipButton = findViewById(R.id.onboarding_skip);


        onboardingPager.setAdapter(new OnboardingPageAdapter());
        onboardingPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int pagesCount = onboardingPager.getAdapter().getItemCount();
                if (position + 1 == pagesCount) {
                    skipButton.setVisibility(View.GONE);
                    nextButton.setText(R.string.action_finish);
                } else {
                    skipButton.setVisibility(View.VISIBLE);
                    nextButton.setText(R.string.action_next);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pagesCount = onboardingPager.getAdapter().getItemCount();
                if (onboardingPager.getCurrentItem() + 1 == pagesCount)
                    finishOnboarding();
                else onboardingPager.setCurrentItem(onboardingPager.getCurrentItem() + 1);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });
    }

    private void finishOnboarding() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean("hasShownTutorial", true)
                .apply();
        if (startMainActivityOnFinish)
            startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
