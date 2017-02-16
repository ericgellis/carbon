package com.mobithink.carbon.station;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobithink.carbon.R;

import java.util.List;

/**
 * Created by mplaton on 14/02/2017.
 */

public class StationEventListViewAdapter extends ArrayAdapter<String> {

    public StationEventListViewAdapter(Context context, List<String> eventType) {
        super(context, 0, eventType);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_station_event_listview,parent, false);
        }

        EventViewHolder viewHolder = (EventViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new EventViewHolder();
            viewHolder.stationEventName = (TextView) convertView.findViewById(R.id.station_event_name);

            convertView.setTag(viewHolder);
        }

        String eventType = getItem(position);
        viewHolder.stationEventName.setText(eventType);
        /*viewHolder.stationEventName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent goToStationActivityWithEvent = new Intent (getContext(),StationActivity.class);
            getContext().startActivity(goToStationActivityWithEvent);
            }
        });*/

        return convertView;
    }

    private class EventViewHolder {
        public TextView stationEventName;

    }
}

