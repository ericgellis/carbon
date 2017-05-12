package com.mobithink.carbon.consultation.childfragmentevent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.fragments.GenericTabFragment;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.utils.PerformanceExplanations;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by mplaton on 26/04/2017.
 */

public class DetailedEventFragment extends GenericTabFragment implements OnMapReadyCallback {

    public static final int MY_REQUEST_CODE = 0;

    EventDTO mEventDTO;

    private TextView mEventNameTextView;
    private TextView mEventTypeTextView;
    private TextView mTimeTextView;
    private TextView mEventExplanations;

    private ImageView mEventPhoto;
    private ImageView mEventAudio;

    MapView mapView;
    private CameraPosition cameraposition;

    MediaPlayer mediaPlayer;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

    public DetailedEventFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.child_fragment_detailed_event, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        mEventNameTextView = (TextView) rootView.findViewById(R.id.eventNameTextView);
        mEventTypeTextView = (TextView) rootView.findViewById(R.id.eventTypeTextView);
        mTimeTextView = (TextView) rootView.findViewById(R.id.timeTextView);
        mEventExplanations = (TextView) rootView.findViewById(R.id.eventExplanations);

        mEventPhoto = (ImageView) rootView.findViewById(R.id.eventPhoto);
        mEventAudio = (ImageView) rootView.findViewById(R.id.eventAudio);
        mapView = (MapView) rootView.findViewById(R.id.map);

        Bundle extras = getArguments();
        mEventDTO = (EventDTO) extras.getSerializable("eventDTO");

        if(mapView != null){
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        mEventNameTextView.setText(mEventDTO.getEventName());
        mEventTypeTextView.setText(mEventDTO.getEventType());
        double eventTime = mEventDTO.getEndTime()-mEventDTO.getStartTime();
        mTimeTextView.setText(timeFormat.format(eventTime));
        PerformanceExplanations performanceExplanations = new PerformanceExplanations();
        mEventExplanations.setText(performanceExplanations.performanceExplanations(mEventDTO));

        final String picturePath = mEventDTO.getPicture() + ".jpg" ;
        boolean existsImage = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkPhoto/"+picturePath)).exists();
        if(mEventDTO.getPicture() != null && existsImage){
            Bitmap bitmap = BitmapFactory.decodeFile(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkPhoto/" +picturePath);
            mEventPhoto.setImageBitmap(bitmap);

        }

        final String audioPath = mEventDTO.getVoiceMemo() + ".3gp" ;
        final boolean existsAudio = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkAudio/"+audioPath)).exists();
        if (mEventDTO.getVoiceMemo() != null && existsAudio){
            mEventAudio.setVisibility(View.VISIBLE);
            mEventAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkAudio/"+audioPath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        if (mEventDTO.getGpsLat()!= null && mEventDTO.getGpsLong() != null){
        googleMap.addMarker(new MarkerOptions().position(new LatLng(mEventDTO.getGpsLat(), mEventDTO.getGpsLong())));
         cameraposition = CameraPosition.builder().target(new LatLng(mEventDTO.getGpsLat(), mEventDTO.getGpsLong())).zoom(16).bearing(0).build();
        }
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraposition));
    }
}
