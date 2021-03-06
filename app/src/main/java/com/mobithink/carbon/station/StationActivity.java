package com.mobithink.carbon.station;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.driving.DrivingActivity;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;

import static android.content.ContentValues.TAG;


/**
 * Created by mplaton on 02/02/2017.
 */

public class StationActivity extends Activity implements IEventSelectedListener, OnMapReadyCallback {

    FragmentManager fm = getFragmentManager();
    StationDataDTO stationDataDTO;
    long stationId;
    String stationName;
    int stationStep;
    long stationStartTime;
    int nStartingPersonNumber = 0;
    int nEndingPersonNumber = 50;
    int numberOfPeopleIn = 0;
    int numberOfPeopleOut = 0;
    Location location = null;
    double longitude;
    double latitude;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.station);

        mStationToolBar = (Toolbar) findViewById(R.id.stationToolBar);
        mChangeStationNameButton = (Button) findViewById(R.id.changeStationNameButton);
        mDeleteTimeCodeButton = (Button) findViewById(R.id.deleteTimeCodeButton);
        mStationNameTextView = (TextView) findViewById(R.id.stationNameTextView);
        mTimeCodeChronometer = (Chronometer) findViewById(R.id.timeCodeChronometer);
        mStationEventCustomListView = (ListView) findViewById(R.id.station_event_custom_listview);

        stationDataDTO = new StationDataDTO();
        Bundle extras = getIntent().getExtras();
        stationId = (long) extras.getSerializable("stationId");
        stationName = (String) extras.getSerializable("stationName");
        stationStep = (int) extras.getSerializable("stationStep");
        stationStartTime = (long) extras.getSerializable("stationStartTime");
        stationDataDTO.setId(stationId);
        mStationNameTextView.setText(stationName);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            public void onProviderEnabled(String s) {
            }

            public void onProviderDisabled(String s) {
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 2000, (float) 10, locationListener);

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

        mStationEventCustomListViewAdapter = new StationEventCustomListViewAdapter(this);
        mStationEventCustomListView.setAdapter(mStationEventCustomListViewAdapter);

        mChangeStationNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStationName();
            }
        });

        mDeleteTimeCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stationDataDTO.setStartTime(null);
                DatabaseManager.getInstance().deleteStationData(CarbonApplicationManager.getInstance().getCurrentTripId(), stationDataDTO);
                backToDrivingPage(RESULT_CANCELED);
            }
        });

        mChooseEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTochooseStationEvent();
            }
        });

        mUnrealizedStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stationSkip();
            }
        });

        mStopTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStationData();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mTimeCodeChronometer.start();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void changeStationName(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alertDialog.setTitle("Ce n'est pas " + mStationNameTextView.getText().toString() + "?");
        alertDialog.setMessage("Entrer le nouveau nom de la station");
        alertDialog.setView(edittext);

        alertDialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String item = edittext.getEditableText().toString();
                mStationNameTextView.setText(item);
            }
        });

        alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alertDialog.show();

    }

    public void backToDrivingPage(int resultCode) {

        Intent toDrivingPage = new Intent (this, DrivingActivity.class);
        setResult(resultCode, toDrivingPage);
        finish();
    }

    public void stationSkip() {

        stationDataDTO.setEndTime(stationStartTime);
        stationDataDTO.setGpsLat((long) latitude);
        stationDataDTO.setGpsLong((long) longitude);
        DatabaseManager.getInstance().updateStationData(CarbonApplicationManager.getInstance().getCurrentTripId(), stationDataDTO);
        backToDrivingPage(Activity.RESULT_OK);
    }

    public void registerStationData() {

        stationDataDTO.setId(stationId);
        stationDataDTO.setStationName(mStationNameTextView.getText().toString());
        stationDataDTO.setNumberOfComeIn(numberOfPeopleIn);
        stationDataDTO.setNumberOfGoOut(numberOfPeopleOut);
        stationDataDTO.setEndTime(System.currentTimeMillis());
        stationDataDTO.setGpsLat((long) latitude);
        stationDataDTO.setGpsLong((long) longitude);
        stationDataDTO.setStationStep(stationStep);

        DatabaseManager.getInstance().updateStationData(CarbonApplicationManager.getInstance().getCurrentTripId(), stationDataDTO);
        Log.i(TAG, "registerStationData: Station has been updated");
        backToDrivingPage(Activity.RESULT_OK);
    }

    //Select number of boarding people
    public void countBoardingPeople(View v) {

        if (v == mAddPeopleButton) {
            if (numberOfPeopleIn < nEndingPersonNumber) {
                numberOfPeopleIn++;
                mBoardingPeopleTextView.setText(java.lang.String.valueOf(numberOfPeopleIn));
            }
        }
        if (v == mDecreaseNumberOfAddedPeopleButton) {
            if (numberOfPeopleIn > nStartingPersonNumber) {
                numberOfPeopleIn--;
                mBoardingPeopleTextView.setText(java.lang.String.valueOf(numberOfPeopleIn));
            }
        }
    }

    //Select number of exit people
    public void countExitPeople(View v) {
        if (v == mRemovePeopleButton) {
            if (numberOfPeopleOut < nEndingPersonNumber) {
                numberOfPeopleOut++;
                mExitPeopleTextView.setText(java.lang.String.valueOf(numberOfPeopleOut));
            }
        }
        if (v == mDecreaseNumberOfRemovedPeopleButton) {
            if (numberOfPeopleOut > nStartingPersonNumber) {
                numberOfPeopleOut--;
                mExitPeopleTextView.setText(java.lang.String.valueOf(numberOfPeopleOut));
            }
        }
    }

    public void goTochooseStationEvent(){


        Bundle bundle = new Bundle();
        bundle.putSerializable("stationLongitude", longitude);
        bundle.putSerializable("stationLatitude", latitude);
        bundle.putSerializable("stationName", mStationNameTextView.getText().toString());

        StationEventDialogFragment dialogFragment = new StationEventDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.setListener(this);
        dialogFragment.show(fm, "Choisir un évènement");
    }

    @Override
    public void onEventSelected(EventDTO event) {
        mStationEventCustomListViewAdapter.addData(event);
        mStationEventCustomListViewAdapter.notifyDataSetChanged();
    }
}
