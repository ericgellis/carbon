package com.mobithink.carbon.station;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.driving.DrivingActivity;



/**
 * Created by mplaton on 02/02/2017.
 */

public class StationActivity extends Activity implements IEventSelectedListener{

    private Button mDecreaseNumberOfAddedPeopleButton;
    private Button mDecreaseNumberOfRemovedPeopleButton;
    private Button mAddPeopleButton;
    private Button mRemovePeopleButton;
    private Button mChooseEventButton;
    private Button mUnrealizedStopButton;
    private Button mStopTimeButton;

    private TextView mBoardingPeopleTextView;
    private TextView mExitPeopleTextView;

    private ListView mStationEventCustomListView;
    private StationEventCustomListViewAdapter mStationEventCustomListViewAdapter;
    private String [] eventTypeList;

    private Toolbar mStationToolBar;
    private Button mChangeStationNameButton;
    private Button mDeleteTimeCodeButton;
    private TextView mStationNameTextView;
    private Chronometer mTimeCodeChronometer;

    FragmentManager fm = getFragmentManager();

    int nStartingPersonNumber = 0;
    int nEndingPersonNumber = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station);

        mStationToolBar = (Toolbar) findViewById(R.id.stationToolBar);
        mChangeStationNameButton = (Button) findViewById(R.id.changeStationNameButton);
        mDeleteTimeCodeButton = (Button) findViewById(R.id.deleteTimeCodeButton);
        mStationNameTextView = (TextView) findViewById(R.id.stationNameTextView);
        mStationNameTextView.setText("Jean Jaures");
        mTimeCodeChronometer = (Chronometer) findViewById(R.id.timeCodeChronometer);
        mStationEventCustomListView = (ListView) findViewById(R.id.station_event_custom_listview);

        mDecreaseNumberOfAddedPeopleButton = (Button) findViewById(R.id.decreaseNumberOfAddedPeopleButton);
        mDecreaseNumberOfAddedPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBoardingPeople(v);
            }
        });
        mDecreaseNumberOfRemovedPeopleButton = (Button) findViewById(R.id.decreaseNumberOfRemovedPeopleButton);
        mDecreaseNumberOfRemovedPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countExitPeople(v);
            }
        });
        mAddPeopleButton = (Button) findViewById(R.id.addPeopleButton);
        mAddPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBoardingPeople(v);
            }
        });
        mRemovePeopleButton = (Button) findViewById(R.id.removePeopleButton);
        mRemovePeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countExitPeople(v);
            }
        });
        mChooseEventButton = (Button) findViewById(R.id.chooseEventButton);
        mUnrealizedStopButton = (Button) findViewById(R.id.unrealizedStopButton);
        mStopTimeButton = (Button) findViewById(R.id.stopTimeButton);

        mBoardingPeopleTextView = (TextView) findViewById(R.id.boardingPeopleTextView);
        mBoardingPeopleTextView.setText("0");
        mExitPeopleTextView = (TextView) findViewById(R.id.exitPeopleTextView);
        mExitPeopleTextView.setText("0");

        mChangeStationNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStationName();
            }
        });

        mDeleteTimeCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDrivingPage();
            }
        });

        mChooseEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTochooseStationEvent();
            }
        });

        mStationEventCustomListViewAdapter = new StationEventCustomListViewAdapter(this);
        mStationEventCustomListView.setAdapter(mStationEventCustomListViewAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mTimeCodeChronometer.start();

    }

    public void changeStationName(){
        mStationNameTextView.setEnabled(true);
        mStationNameTextView.getEditableText();
        mStationNameTextView.setCursorVisible(true);
        mStationNameTextView.setFocusable(true);
        mStationNameTextView.setFocusableInTouchMode(true);
    }

    public void goToDrivingPage(){
        Intent toDrivingPage = new Intent (this, DrivingActivity.class);
        this.startActivity(toDrivingPage);
    }

    //Select number of boarding people
    public void countBoardingPeople(View v) {
        java.lang.String getString = java.lang.String.valueOf(mBoardingPeopleTextView.getText());
        int current = Integer.parseInt(getString);
        if (v == mAddPeopleButton) {
            if (current < nEndingPersonNumber) {
                current++;
                mBoardingPeopleTextView.setText(java.lang.String.valueOf(current));
            }
        }
        if (v == mDecreaseNumberOfAddedPeopleButton) {
            if (current > nStartingPersonNumber) {
                current--;
                mBoardingPeopleTextView.setText(java.lang.String.valueOf(current));
            }
        }
    }

    //Select number of exit people
    public void countExitPeople(View v) {
        java.lang.String getString = java.lang.String.valueOf(mExitPeopleTextView.getText());
        int current = Integer.parseInt(getString);
        if (v == mRemovePeopleButton) {
            if (current < nEndingPersonNumber) {
                current++;
                mExitPeopleTextView.setText(java.lang.String.valueOf(current));
            }
        }
        if (v == mDecreaseNumberOfRemovedPeopleButton) {
            if (current > nStartingPersonNumber) {
                current--;
                mExitPeopleTextView.setText(java.lang.String.valueOf(current));
            }
        }
    }

    public void goTochooseStationEvent(){
        StationEventDialogFragment dialogFragment = new  StationEventDialogFragment();
        dialogFragment.setListener(this);
        dialogFragment.show(fm, "Choisir un évènement");
    }

    @Override
    public void onEventSelected(String eventType) {
        mStationEventCustomListViewAdapter.addData(eventType);
        mStationEventCustomListViewAdapter.notifyDataSetChanged();
    }
}
