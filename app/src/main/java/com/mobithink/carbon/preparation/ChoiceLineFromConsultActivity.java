package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.SplashScreenActivity;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.TripDTO;
import com.mobithink.carbon.managers.RetrofitManager;
import com.mobithink.carbon.webservices.LineService;
import com.mobithink.carbon.webservices.TripService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mplaton on 31/01/2017.
 */

public class ChoiceLineFromConsultActivity extends Activity {

    private static final String TAG = ChoiceLineFromConsultActivity.class.getName();

    public static final int SPLASH_SCREEN = 1;

    private TextInputLayout mWriteCityNameTextInputLayout;
    private TextInputLayout mWriteLineTextInputLayout;
    private TextInputLayout mWriteDirectionTextInputLayout;

    private AutoCompleteTextView mWriteCityNameAutoCompleteTextView;
    private TextInputEditText mWriteLineTextInputEditText;
    private TextInputEditText mWriteDirectionTextInputEditText;

    private ArrayAdapter<CityDTO> cityAdapter;
    private ArrayAdapter<BusLineDTO> lineAdapter;
    private ArrayAdapter<StationDTO> directionAdapter;
    private CityDTO mSelectedCityDTO;
    private BusLineDTO mSelectedLineDTO;
    private Toolbar mConsultLineToolBar;
    private ListView tripResultListView;
    private TripResultListViewAdapter tripResultListViewAdapter;
    private ArrayList<TripDTO> mTripList;
    private Button mConsultButton;
    private StationDTO mSelectedDirection;
    private ArrayList<StationDTO> listStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_choice_line_from_consult);

        tripResultListView = (ListView) findViewById(R.id.tripResultListView);
        tripResultListViewAdapter = new TripResultListViewAdapter(this, mTripList);
        tripResultListView.setAdapter(tripResultListViewAdapter);
        mTripList = new ArrayList<>();


        cityAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        lineAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        directionAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        listStation = new ArrayList<>();


        mConsultLineToolBar = (Toolbar) findViewById(R.id.consultLineToolBar);
        mConsultLineToolBar.setTitle("Choix d'une ligne");
        mConsultLineToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mWriteCityNameTextInputLayout = (TextInputLayout) findViewById(R.id.city_textinputlayout);
        mWriteLineTextInputLayout = (TextInputLayout) findViewById(R.id.line_textinputlayout);
        mWriteDirectionTextInputLayout  = (TextInputLayout) findViewById(R.id.direction_textinputlayout);

        mWriteCityNameAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.Writing_City_Name);
        mWriteLineTextInputEditText = (TextInputEditText) findViewById(R.id.Line_edtitext);
        mWriteDirectionTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Direction);

        mWriteCityNameAutoCompleteTextView.setThreshold(1);//will start working from first character
        mWriteCityNameAutoCompleteTextView.setAdapter(cityAdapter);

        mWriteCityNameAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long arg3) {
                //hide keyboard
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(parent.getApplicationWindowToken(), 0);
                mSelectedCityDTO = (CityDTO) parent.getAdapter().getItem(position);
                getCityLines();

            }
        });

        mWriteLineTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedCityDTO != null) {
                    new AlertDialog.Builder(ChoiceLineFromConsultActivity.this)
                            .setCancelable(true)
                            .setAdapter(lineAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSelectedLineDTO = lineAdapter.getItem(which);
                                    if (mSelectedLineDTO != null) {
                                        mWriteLineTextInputEditText.setText(mSelectedLineDTO.getName());
                                    }
                                    getLineStations();
                                }
                            })
                            .create()
                            .show();
                } else {
                    mWriteCityNameTextInputLayout.setErrorEnabled(true);
                    mWriteCityNameTextInputLayout.setError("Vous devez sélectionner une ville");
                }
            }
        });

        mWriteDirectionTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedLineDTO != null) {

                    new AlertDialog.Builder(ChoiceLineFromConsultActivity.this)
                            .setCancelable(true)
                            .setAdapter(directionAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSelectedDirection = directionAdapter.getItem(which);
                                    if (mSelectedDirection != null) {
                                        mWriteDirectionTextInputEditText.setText(mSelectedDirection.getStationName());
                                    }

                                    if (mSelectedDirection.equals(directionAdapter.getItem(0))) {
                                        Collections.reverse(listStation);
                                    }
                                }
                            })
                            .create()
                            .show();
                } else {
                    mWriteLineTextInputLayout.setErrorEnabled(true);
                    mWriteLineTextInputLayout.setError("Vous devez sélectionner une ligne");
                }
            }
        });

        mConsultButton = (Button) findViewById(R.id.consultButton);
        mConsultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField();
            }
        });

    }

    private void checkField() {
        boolean hasError = false;

        if ((mWriteCityNameAutoCompleteTextView.getText().toString().equals(""))) {
            hasError = true;
            mWriteCityNameTextInputLayout.setErrorEnabled(true);
            mWriteCityNameTextInputLayout.setError("Vous devez sélectionner une ville");
        }

        if ((mWriteLineTextInputEditText.getText().toString().equals(""))) {
            hasError = true;
            mWriteLineTextInputLayout.setErrorEnabled(true);
            mWriteLineTextInputLayout.setError("Vous devez sélectionner une ligne");
        }

        if ((mWriteDirectionTextInputEditText.getText().toString().equals(""))) {
            hasError = true;
            mWriteDirectionTextInputLayout.setErrorEnabled(true);
            mWriteDirectionTextInputLayout.setError("Vous devez sélectionner une direction");
        }

        if (!hasError) {
            showResultListView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCities();

    }

    public void showResultListView() {

        TripService tripService = RetrofitManager.build().create(TripService.class);
        Call<List<TripDTO>> call = tripService.showTripList(mSelectedLineDTO.getId());
        call.enqueue(new Callback<List<TripDTO>>() {
            @Override
            public void onResponse(Call<List<TripDTO>> call, Response<List<TripDTO>> response) {
                Log.i("retrofit", "Chargement réussi");

                switch (response.code()) {
                    case 200:
                        Log.i(TAG,"showResultListView success with size  : " + response.body().size());

                        mTripList.clear();
                        mTripList.addAll(response.body());

                        tripResultListViewAdapter.setData(mTripList);
                        tripResultListViewAdapter.notifyDataSetChanged();
                        tripResultListView.setVisibility(View.VISIBLE);

                        break;
                    default:
                        Log.e(TAG, "showResultListView fail with code " + response.code() +" and message " + response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<TripDTO>> call, Throwable t) {
                Log.e(TAG, "showResultListView error", t);
            }
        });
    }

    private void getLineStations() {
        LineService groupService = RetrofitManager.build().create(LineService.class);

        Call<List<StationDTO>> call = groupService.getLineStations(mSelectedLineDTO.getId());

        call.enqueue(new Callback<List<StationDTO>>() {
            @Override
            public void onResponse(Call<List<StationDTO>> call, Response<List<StationDTO>> response) {
                switch (response.code()) {
                    case 200:
                        Log.i(TAG,"getLineStations success with size  : " + response.body().size());
                        listStation.clear();
                        listStation.addAll(response.body());

                        directionAdapter.clear();
                        if (listStation.size() >= 2) {
                            directionAdapter.add(listStation.get(0));
                            directionAdapter.add(listStation.get(listStation.size() - 1));
                            directionAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        Log.e(TAG, "getLineStations fail with code " + response.code() +" and message " + response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<StationDTO>> call, Throwable t) {
                Log.e(TAG, "getLineStations error", t);
            }
        });
    }

    private void getCityLines() {

        LineService groupService = RetrofitManager.build().create(LineService.class);

        Call<List<BusLineDTO>> call = groupService.getCityLines(mSelectedCityDTO.getName());

        call.enqueue(new Callback<List<BusLineDTO>>() {
            @Override
            public void onResponse(Call<List<BusLineDTO>> call, Response<List<BusLineDTO>> response) {
                switch (response.code()) {
                    case 200:
                        Log.i(TAG,"getCityLines success with size  : " + response.body().size());

                        lineAdapter.clear();
                        lineAdapter.addAll(response.body());
                        lineAdapter.notifyDataSetChanged();

                        break;
                    default:
                        Log.e(TAG, "getCityLines fail with code " + response.code() +" and message " + response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusLineDTO>> call, Throwable t) {
                Log.e(TAG, "getCityLines error", t);
            }
        });
    }

    private void getCities() {
        LineService groupService = RetrofitManager.build().create(LineService.class);

        Call<List<CityDTO>> call = groupService.getCities();

        call.enqueue(new Callback<List<CityDTO>>() {
            @Override
            public void onResponse(Call<List<CityDTO>> call, Response<List<CityDTO>> response) {
                switch (response.code()) {
                    case 200:
                        Log.i(TAG,"getCities success with size  : " + response.body().size());

                        cityAdapter.clear();
                        cityAdapter.addAll(response.body());
                        cityAdapter.notifyDataSetChanged();

                        break;
                    default:
                        Log.e(TAG, "getCities fail with code " + response.code() +" and message " + response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<CityDTO>> call, Throwable t) {
                Log.e(TAG, "getCities error", t);
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent toSplashScreenPage = new Intent (this,SplashScreenActivity.class);
        this.startActivityForResult(toSplashScreenPage, SPLASH_SCREEN);
    }
}
