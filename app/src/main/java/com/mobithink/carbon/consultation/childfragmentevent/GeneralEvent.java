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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by mplaton on 26/04/2017.
 */

public class GeneralEvent extends GenericTabFragment {

    public static final int MY_REQUEST_CODE = 0;

    private TextView mEventsNumberTextView;
    private TextView mEventsTotalTimeTextView;
    private TextView mLoadTextView;
    private TextView mPossibleTimeSavingTextView;
    private TextView mSavingInEuroTextView;

    private ListView mEventListView;
    private EventMainListViewAdapter mEventMainListViewAdapter;
    private List<EventDTO> eventsList;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

    public GeneralEvent() {
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
        mEventMainListViewAdapter = new EventMainListViewAdapter(getContext(), eventsList);
        mEventListView.setAdapter(mEventMainListViewAdapter);
        mEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetailedInformations(position);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        if (getTripDTO().getEventDTOList() != null){
            for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
                mEventsNumberTextView.setText(String.valueOf(getTripDTO().getEventDTOList().size()));
            }
        }

    }

    public void showDetailedInformations (int position){

    }
}
