package com.mobithink.carbon.consultation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.database.model.TripDTO;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;


public class CapacityTab4 extends GenericTabFragment {

    LinearLayout capacityChartLinearLayout;

    TextView maxPeopleTextView;
    TextView averagePeopleTextView;
    TextView minPeopleTextView;
    TextView capacityLess50TextView;
    TextView capacityLess50;

    Button movementByStationButton;
    Button detailByStationButton;

    TripDTO tripDTO;
    StationDataDTO stationDataDTO;

    GraphicalView capacityChartGraphicalView;
    //CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[] {new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0), new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 1), new CombinedXYChart.XYCombinedChartDef(BarChart.TYPE, 2), new CombinedXYChart.XYCombinedChartDef(BarChart.TYPE, 4)};
    CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[]{new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0), new CombinedXYChart.XYCombinedChartDef(BarChart.TYPE, 1), new CombinedXYChart.XYCombinedChartDef(BarChart.TYPE, 2)};
    // Creating a dataset to hold each series
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    // Creating a XYMultipleSeriesRenderer to customize the whole chart
    XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

    public CapacityTab4() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_capacity_tab4, container, false);

        capacityChartLinearLayout = (LinearLayout) rootView.findViewById(R.id.capacity_chart);

        movementByStationButton = (Button) rootView.findViewById(R.id.movementByStationButton);
        detailByStationButton = (Button) rootView.findViewById(R.id.detailByStationButton);

        maxPeopleTextView = (TextView) rootView.findViewById(R.id.maxPeople);
        averagePeopleTextView = (TextView) rootView.findViewById(R.id.averagePeople);
        minPeopleTextView = (TextView) rootView.findViewById(R.id.minPeople);
        capacityLess50TextView = (TextView) rootView.findViewById(R.id.capacityLess50TextView);
        capacityLess50 = (TextView) rootView.findViewById(R.id.capacityLess50);

        capacityLess50TextView.setVisibility(View.GONE);
        capacityLess50.setVisibility(View.GONE);

        movementByStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movementByStationMethod();
            }
        });

        detailByStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailByStationMethod();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        if (capacityChartGraphicalView == null) {
            initCombinedChart();
            capacityChartGraphicalView = ChartFactory.getCombinedXYChartView(getContext(), dataset, multiRenderer, types);
            capacityChartLinearLayout.addView(capacityChartGraphicalView);
        } else {
            capacityChartGraphicalView.repaint();
        }
    }


    public void movementByStationMethod() {
        /*if (capacityChartGraphicalView == null) {
            initBarCombinedChart();
            capacityChartGraphicalView = ChartFactory.getCombinedXYChartView(this, dataset, multiRenderer, types);
            capacityChartLinearLayout.addView(mCombinedChart);
        } else {
            capacityChartGraphicalView.repaint();
        }
        capacityLess50TextView.setVisibility(View.VISIBLE);
        capacityLess50.setVisibility(View.VISIBLE);*/
    }

    public void detailByStationMethod() {
        /*if (capacityChartGraphicalView == null) {
            initCombinedChart();
            capacityChartGraphicalView = ChartFactory.getCombinedXYChartView(this, dataset, multiRenderer, types);
            capacityChartLinearLayout.addView(mCombinedChart);
        } else {
            capacityChartGraphicalView.repaint();
        }
        capacityLess50TextView.setVisibility(View.GONE);
        capacityLess50.setVisibility(View.GONE);*/
    }

    public void initCombinedChart() {


        String[] mStationName;
        Integer[] stationNumber;
        int maxPeopleNumber;
        Integer[] peopleNumberInBus;
        Integer[] peopleNumberComeInBus;
        Integer[] peopleNumberGoOutBus;

        ArrayList<String> names = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            names.add(stationDataDTO.getStationName());
        }
        mStationName = new String[names.size()];
        mStationName = names.toArray(mStationName);

        ArrayList<Integer> stationNb = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            stationNb.add(stationDataDTO.getStationStep());
        }
        stationNumber = new Integer[stationNb.size()];
        stationNumber = stationNb.toArray(stationNumber);

        maxPeopleNumber = getTripDTO().getVehicleCapacity();

        ArrayList<Integer> peopleNbComeInBus = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            peopleNbComeInBus.add(stationDataDTO.getNumberOfComeIn());
        }
        peopleNumberComeInBus = new Integer[peopleNbComeInBus.size()];
        peopleNumberComeInBus = peopleNbComeInBus.toArray(peopleNumberComeInBus);

        ArrayList<Integer> peopleNbGoOutBus = new ArrayList<>();
        for (StationDataDTO stationDataDTO : getTripDTO().getStationDataDTOList()) {
            peopleNbGoOutBus.add(stationDataDTO.getNumberOfGoOut());
        }
        peopleNumberGoOutBus = new Integer[peopleNbGoOutBus.size()];
        peopleNumberGoOutBus = peopleNbGoOutBus.toArray(peopleNumberComeInBus);


        // Creating an  XYSeries for maximal capacity of bus
        XYSeries maxCapacitySeries = new XYSeries("Capacité maximale");
        // Creating an  XYSeries for number of people in bus
        //XYSeries peopleNumberInBusSeries = new XYSeries("Nombre de personnes dans le bus");
        // Creating an  XYSeries for number of people come in bus
        XYSeries peopleNumberComeInSeries = new XYSeries("Nombre de personnes montant dans le bus");
        // Creating an  XYSeries for number of people go out bus
        XYSeries peopleNumberGoOutSeries = new XYSeries("Nombre de personnes descendant du bus");

        // Adding data to XYSeries for maximal capacity of bus, XYSeries for number of people in bus, XYSeries for number of people come in bus and XYSeries for number of people go out bus
        for (int i = 0; i < stationNumber.length; i++) {
            maxCapacitySeries.add(stationNumber[i], maxPeopleNumber);
            //peopleNumberInBusSeries.add(stationNumber[i],peopleNumberInBus[i]);
            peopleNumberComeInSeries.add(stationNumber[i], peopleNumberComeInBus[i]);
            peopleNumberGoOutSeries.add(stationNumber[i], peopleNumberGoOutBus[i]);
        }

        // Adding Series to the dataset
        dataset.addSeries(maxCapacitySeries);
        //dataset.addSeries(peopleNumberInBusSeries);
        dataset.addSeries(peopleNumberComeInSeries);
        dataset.addSeries(peopleNumberGoOutSeries);

        // Creating XYSeriesRenderer to customize maxCapacitySeries
        XYSeriesRenderer maxCapacityRenderer = new XYSeriesRenderer();
        maxCapacityRenderer.setColor(R.color.purple_chart);
        maxCapacityRenderer.setLineWidth(2);
        maxCapacityRenderer.setDisplayChartValues(true);

        // Creating XYSeriesRenderer to customize peopleNumberInBusSeries
        /*XYSeriesRenderer peopleNumberInBusRenderer = new XYSeriesRenderer();
        peopleNumberInBusRenderer.setColor(R.color.orange_chart);
        peopleNumberInBusRenderer.setPointStyle(PointStyle.CIRCLE);
        peopleNumberInBusRenderer.setFillPoints(true);
        peopleNumberInBusRenderer.setLineWidth(2);
        peopleNumberInBusRenderer.setDisplayChartValues(true);*/

        // Creating XYSeriesRenderer to customize peopleNumberComeInSeries
        XYSeriesRenderer peopleNumberComeInRenderer = new XYSeriesRenderer();
        peopleNumberComeInRenderer.setColor(R.color.green_chart);
        peopleNumberComeInRenderer.setFillPoints(true);
        peopleNumberComeInRenderer.setLineWidth(2);
        peopleNumberComeInRenderer.setDisplayChartValues(true);


        //Creating XYSeriesRenderer to customize peopleNumberGoOutSeries
        XYSeriesRenderer peopleNumberGoOutRenderer = new XYSeriesRenderer();
        peopleNumberGoOutRenderer.setColor(R.color.red_chart);
        peopleNumberGoOutRenderer.setFillPoints(true);
        peopleNumberGoOutRenderer.setLineWidth(2);
        peopleNumberGoOutRenderer.setDisplayChartValues(true);

        multiRenderer.setXLabels(0);
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setYLabelsVerticalPadding(5);
        multiRenderer.setBarSpacing(4);
        for (int i = 0; i < stationNumber.length; i++) {
            multiRenderer.addXTextLabel(i, mStationName[i]);
        }

        multiRenderer.addSeriesRenderer(maxCapacityRenderer);
        //multiRenderer.addSeriesRenderer(peopleNumberInBusRenderer);
        multiRenderer.addSeriesRenderer(peopleNumberComeInRenderer);
        multiRenderer.addSeriesRenderer(peopleNumberGoOutRenderer);
        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(10);
    }

    public void initBarCombinedChart() {

    }


}
