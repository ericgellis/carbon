package com.mobithink.carbon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mobithink.carbon.preparation.ChoiceLineFromAnalyzeActivity;
import com.mobithink.carbon.preparation.ChoiceLineFromConsultActivity;
import com.mobithink.carbon.preparation.ParametersActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    Button mAnalyzeButton;
    Button mConsultButton;
    ImageView mParametersSettings;

    ImageView mMobiThinkLogo;
    String logoUrl = "http://mobithinjy.cluster003.ovh.net/wp-content/uploads/2016/12/MOBITHINK_logoSSB_Q.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        mParametersSettings = (ImageView) findViewById(R.id.parameterButton);
        mMobiThinkLogo = (ImageView) findViewById(R.id.mobithinkLogo);
        mMobiThinkLogo.setImageResource(R.drawable.mobithink_logo);

        mAnalyzeButton = (Button) findViewById(R.id.analyzeButton);
        mConsultButton = (Button) findViewById(R.id.consultButton);

        mAnalyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageToChooseLine();
            }
        });

        mConsultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageToChooseLineFromConsult();
            }
        });

        mParametersSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageToParameters();
            }
        });
    }

    public void changePageToChooseLine(){
        Intent chooseLineFromAnalyze = new Intent(this, ChoiceLineFromAnalyzeActivity.class);
        this.startActivity(chooseLineFromAnalyze);
    }

    public void changePageToChooseLineFromConsult(){
        Intent chooseLineFromConsult = new Intent(this, ChoiceLineFromConsultActivity.class);
        this.startActivity(chooseLineFromConsult);
    }

    public void changePageToParameters(){
        Intent chooseParameters = new Intent(this, ParametersActivity.class);
        this.startActivity(chooseParameters);
    }
}
