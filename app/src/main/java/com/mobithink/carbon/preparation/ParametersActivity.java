package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.managers.PreferenceManager;

/**
 * Created by mplaton on 31/01/2017.
 */

public class ParametersActivity extends Activity {


    private static final int MINIMUM_FREQUENCY_VALUE = 20;
    private static final int FREQUENCY_STEP = 10;

    private static final int MINIMUM_RADIUS_VALUE = 50;
    private static final int RADIUS_STEP = 50;

    SeekBar mFrequencySeekBar;
    SeekBar mRadiusSeekBar;

    TextView mFrequencyNumberTextView;
    TextView mRadiusNumberTextView;

    Button mValidationButton;
    Toolbar mParametersToolBar;

    private int mRadiusValue;
    private int mFrequencyValue;

    private boolean hasFrequencyChanged = false;
    private boolean hasRadiusChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_parameters);

        mParametersToolBar = (Toolbar) findViewById(R.id.parametersToolBar);
        mParametersToolBar.setTitle("Paramètres");
        mParametersToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mFrequencyValue = (PreferenceManager.getInstance().getTimeFrequency() - MINIMUM_FREQUENCY_VALUE);
        mRadiusValue = (PreferenceManager.getInstance().getStationRadius() - MINIMUM_RADIUS_VALUE);

        mFrequencySeekBar = (SeekBar) findViewById(R.id.frequency_seekbar);
        mFrequencySeekBar.setProgress(mFrequencyValue);

        mRadiusSeekBar = (SeekBar) findViewById(R.id.radius_seekbar);
        mRadiusSeekBar.setProgress(mRadiusValue);

        mFrequencyNumberTextView = (TextView) findViewById(R.id.frequencyNumber);
        mFrequencyNumberTextView.setText(String.valueOf(PreferenceManager.getInstance().getTimeFrequency()));


        mRadiusNumberTextView = (TextView) findViewById(R.id.radiusNumber);
        mRadiusNumberTextView.setText(String.valueOf(PreferenceManager.getInstance().getStationRadius()));


        mFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hasFrequencyChanged = true;
                mFrequencyValue = (Math.round(progress/FREQUENCY_STEP))*FREQUENCY_STEP + MINIMUM_FREQUENCY_VALUE;
                mFrequencyNumberTextView.setText(String.valueOf(mFrequencyValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hasRadiusChanged = true;
                mRadiusValue = (Math.round(progress/RADIUS_STEP))*RADIUS_STEP + MINIMUM_RADIUS_VALUE;
                mRadiusNumberTextView.setText(String.valueOf(mRadiusValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mValidationButton = (Button) findViewById(R.id.register_parameter_button);
        mValidationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPreference();
            }
        });


    }

    private void registerPreference() {
        if (hasFrequencyChanged) {
            PreferenceManager.getInstance().setTimeFrequency(mFrequencyValue);
        }
        if (hasRadiusChanged) {
            PreferenceManager.getInstance().setStationRadius(mRadiusValue);
        }

        if (hasRadiusChanged || hasFrequencyChanged) {
            setResult(RESULT_OK);

        }
        this.finish();

    }
}
