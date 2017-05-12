package com.mobithink.carbon.consultation.childfragmentevent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.adapter.EventMainListViewAdapter;
import com.mobithink.carbon.consultation.fragments.GenericTabFragment;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.PreferenceManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mplaton on 26/04/2017.
 */

public class GeneralEventFragment extends GenericTabFragment {

    public static final int MY_REQUEST_CODE = 0;

    private TextView mEventsNumberTextView;
    private TextView mEventsTotalTimeTextView;
    private TextView mLoadTextView;
    private TextView mPossibleTimeSavingTextView;
    private TextView mSavingInEuroTextView;

    private ListView mEventListView;
    private EventMainListViewAdapter mEventMainListViewAdapter;
    private List<EventDTO> eventsList;

    ArrayList<EventDTO> eventInDrivingTab = new ArrayList<>();
    ArrayList<EventDTO> eventInCrossroadTab = new ArrayList<>();
    ArrayList<EventDTO> eventInStationTab = new ArrayList<>();

    ArrayList<Double> eventTimeTab = new ArrayList<>();
    double eventTime;
    Double eventTimeSum = 0.0;

    ArrayList<Long> eventTimeSaving = new ArrayList<>();
    long eventTimeSavingSum = 0;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

    public GeneralEventFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.child_fragment_event, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        mEventsNumberTextView = (TextView) rootView.findViewById(R.id.eventsNumberTextView);
        mEventsTotalTimeTextView = (TextView) rootView.findViewById(R.id.eventsTotalTimeTextView);
        mLoadTextView = (TextView) rootView.findViewById(R.id.loadTextView);
        mPossibleTimeSavingTextView = (TextView) rootView.findViewById(R.id.possibleTimeSavingTextView);
        mSavingInEuroTextView = (TextView) rootView.findViewById(R.id.savingInEuroTextView);

        mEventListView = (ListView) rootView.findViewById(R.id.eventListView);
        eventsList = new ArrayList<>();
        mEventMainListViewAdapter = new EventMainListViewAdapter(getContext(), eventsList);
        mEventListView.setAdapter(mEventMainListViewAdapter);
        mEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetailedInformations(position);
            }
        });

        Bundle extras = getArguments();
        eventInDrivingTab = (ArrayList<EventDTO>) extras.getSerializable("eventInDrivingList");
        eventInCrossroadTab = (ArrayList<EventDTO>) extras.getSerializable("eventInCrossroadList");
        eventInStationTab = (ArrayList<EventDTO>) extras.getSerializable("eventInStationList");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        double tripTotalTime = getTripDTO().getEndTime() - getTripDTO().getStartTime();

        if (eventInDrivingTab != null){
            for (EventDTO eventDTO : eventInDrivingTab){
                mEventsNumberTextView.setText(String.valueOf(eventInDrivingTab.size()));
                eventsList.add(eventDTO);

                eventTime = eventDTO.getEndTime() - eventDTO.getStartTime();
                eventTimeTab.add(eventTime);
                eventTimeSum +=eventTime;

                eventTimeSaving.add(eventDTO.getTimeSaving());
                eventTimeSavingSum += eventDTO.getTimeSaving();
            }
        }

        if (eventInCrossroadTab != null) {
            for (EventDTO eventDTO : eventInCrossroadTab) {
                mEventsNumberTextView.setText(String.valueOf(eventInCrossroadTab.size()));
                eventsList.add(eventDTO);

                eventTime = eventDTO.getEndTime() - eventDTO.getStartTime();
                eventTimeTab.add(eventTime);
                eventTimeSum +=eventTime;

                eventTimeSaving.add(eventDTO.getTimeSaving());
                eventTimeSavingSum += eventDTO.getTimeSaving();
            }
        }

        if (eventInStationTab != null) {
            for (EventDTO eventDTO : eventInStationTab) {
                mEventsNumberTextView.setText(String.valueOf(eventInStationTab.size()));
                eventsList.add(eventDTO);

                eventTime = eventDTO.getEndTime() - eventDTO.getStartTime();
                eventTimeTab.add(eventTime);
                eventTimeSum +=eventTime;

                eventTimeSaving.add(eventDTO.getTimeSaving());
                eventTimeSavingSum += eventDTO.getTimeSaving();
            }
        }

        mEventsTotalTimeTextView.setText(timeFormat.format(eventTimeSum));

        double eventTimeLoad =(eventTimeSum/tripTotalTime)*100;
        mLoadTextView.setText(String.valueOf(new DecimalFormat("#.#").format(eventTimeLoad)) + " %");

        mPossibleTimeSavingTextView.setText(timeFormat.format(eventTimeSavingSum));
        int timeSavingInMinutes = (int) (((eventTimeSavingSum / 1000)/60) % 60);
        mSavingInEuroTextView.setText(Math.round(timeSavingInMinutes* PreferenceManager.getInstance().getCostOfProductionByMinute())  + " euro");

        mEventMainListViewAdapter.notifyDataSetChanged();

    }

    public void showDetailedInformations (int position){
        final EventDTO eventDTO;
        eventDTO = eventsList.get(position);
        DetailedEventFragment goToDetailedEvent = new DetailedEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("eventDTO", eventDTO);
        goToDetailedEvent.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.your_placeholder,goToDetailedEvent).addToBackStack(null).commit();

    }

}