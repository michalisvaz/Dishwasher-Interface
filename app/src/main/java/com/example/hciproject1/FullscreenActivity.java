package com.example.hciproject1;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private final Handler mHideHandler = new Handler();

    static int washCode;
    static Drawable dim;

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    String[] descriptionData = {"Προετοιμασία","Σαπούνισμα", "Ξέβγαλμα", "Στέγνωμα"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dim = getResources().getDrawable(R.drawable.window_dim);
        setContentView(R.layout.activity_fullscreen);

        StateProgressBar stateProgressBar = findViewById(R.id.your_state_progress_bar_id1);
        stateProgressBar.setStateDescriptionData(descriptionData);

        stateProgressBar = findViewById(R.id.your_state_progress_bar_id2);
        stateProgressBar.setStateDescriptionData(descriptionData);

        stateProgressBar = findViewById(R.id.your_state_progress_bar_id3);
        stateProgressBar.setStateDescriptionData(descriptionData);

        stateProgressBar = findViewById(R.id.your_state_progress_bar_id4);
        stateProgressBar.setStateDescriptionData(descriptionData);

        stateProgressBar = findViewById(R.id.your_state_progress_bar_id5);
        stateProgressBar.setStateDescriptionData(descriptionData);

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
