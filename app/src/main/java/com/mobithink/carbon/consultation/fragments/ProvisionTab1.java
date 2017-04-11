package com.mobithink.carbon.consultation.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.RollingPointDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.utils.Mathematics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProvisionTab1 extends GenericTabFragment implements OnMapReadyCallback {

    MapView mtripMapView;
    TextView minDistanceBetweenStations;
    TextView averageDistanceBetweenStationsTextView;
    TextView maxDistanceBetweenStations;
    TextView savingInMinutesTextView;
    TextView savingInEuroTextView;

    long interStationObjective = 600;
    long timeInStation;
    long totalTimeInStation = 0;
    long averageTimeInStation;

    long averageDistanceBetweenStations;
    double tripBetweenStationsDistance = 0;
    long tripDistance = 0;

    long timeSavingResult;
    int timeSavingInMinutes;

    public ProvisionTab1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_provision_tab1, container, false);

        mtripMapView = (MapView) rootView.findViewById(R.id.tripMapView);
        mtripMapView.onCreate(savedInstanceState);
        mtripMapView.onResume();
        mtripMapView.getMapAsync(this);

        minDistanceBetweenStations = (TextView) rootView.findViewById(R.id.minDistance);
        averageDistanceBetweenStationsTextView = (TextView) rootView.findViewById(R.id.averageDistance);
        maxDistanceBetweenStations = (TextView) rootView.findViewById(R.id.maxDistance);
        savingInMinutesTextView = (TextView) rootView.findViewById(R.id.savingInMinutesTextView);
        savingInEuroTextView = (TextView) rootView.findViewById(R.id.savingInEuroTextView);

        timeSavingInStation();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();
        savingInMinutesTextView.setText(timeSavingInMinutes+ " min");
        savingInEuroTextView.setText("soit " +  timeSavingInMinutes*1.15 + " euro");

        savingInMinutesTextView.setText(timeSavingInMinutes+ " min");
        savingInEuroTextView.setText("soit " +  Math.round(timeSavingInMinutes*1.15)  + " euro");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        List<LatLng> rollingPointlatLngList = new ArrayList<>();
        List<LatLng> stationlatLngList = new ArrayList<>();
        List<LatLng> eventStationlatLngList = new ArrayList<>();
        List<LatLng> eventRollinglatLngList = new ArrayList<>();

        for (RollingPointDTO rollingPointDTO : getTripDTO().getRollingPointDTOList()) {
            rollingPointlatLngList.add(new LatLng(rollingPointDTO.getGpsLat(), rollingPointDTO.getGpsLong()));
        }

        for (StationDataDTO stationDTO : getTripDTO().getStationDataDTOList()) {
            stationlatLngList.add(new LatLng(stationDTO.getGpsLat(), stationDTO.getGpsLong()));
        }

        for (EventDTO eventDTO : getTripDTO().getEventDTOList()) {
            if (eventDTO.getStationName() != null && eventDTO.getStationName().length() > 0) {
                eventStationlatLngList.add(new LatLng(eventDTO.getGpsLat(), eventDTO.getGpsLong()));
            } else {
                eventRollinglatLngList.add(new LatLng(eventDTO.getGpsLat(), eventDTO.getGpsLong()));
            }
        }

        //draw polyline
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(rollingPointlatLngList);
        googleMap.addPolyline(polylineOptions.color(R.color.mobiThinkBlue).geodesic(true));

        //draw marker and circle and marker
        for (LatLng latLng : stationlatLngList) {
            googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_grey_point)).anchor(0.5f, 0.5f));
            googleMap.addCircle(new CircleOptions().center(latLng).radius(150d));
        }

        LatLng firstStation = stationlatLngList.get(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstStation, 13));

        //draw event
        for (LatLng latLng : eventStationlatLngList) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_blue_red_danger))
                    .anchor(0.5f, 0.5f)
            );
        }

        for (LatLng latLng : eventRollinglatLngList) {
            googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_yellow_red_danger)).anchor(0.5f, 0.5f));
        }
    }

    public void timeSavingInStation(){

        ArrayList<Double> distanceTab = new ArrayList<>() ;
        if (getTripDTO().getStationDataDTOList() != null) {
            for (int i = 0; i + 1 < getTripDTO().getStationDataDTOList().size(); i++) {
                timeInStation = getTripDTO().getStationDataDTOList().get(i).getEndTime() - getTripDTO().getStationDataDTOList().get(i).getStartTime();
                totalTimeInStation += timeInStation;
                tripBetweenStationsDistance = Math.round(Mathematics.calculateGPSDistance(getTripDTO().getStationDataDTOList().get(i).getGpsLat(), getTripDTO().getStationDataDTOList().get(i).getGpsLong(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLat(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLong()));
                distanceTab.add(tripBetweenStationsDistance);
                tripDistance += tripBetweenStationsDistance;
            }

        }

        double minVal = Collections.min(distanceTab);
        double maxVal = Collections.max(distanceTab);

        minDistanceBetweenStations.setText(String.valueOf(minVal) + " m");
        maxDistanceBetweenStations.setText(String.valueOf(maxVal)+ " m");

        averageDistanceBetweenStations = tripDistance/(getTripDTO().getStationDataDTOList().size()-1);
        averageDistanceBetweenStationsTextView.setText(String.valueOf(averageDistanceBetweenStations) + " m");

        averageTimeInStation = totalTimeInStation/getTripDTO().getStationDataDTOList().size();

        timeSavingResult = totalTimeInStation-(((tripDistance/interStationObjective)+ 1)*averageTimeInStation);

        timeSavingInMinutes = (int) (((timeSavingResult / 1000)/60) % 60);


    }
}
