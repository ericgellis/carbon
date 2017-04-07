package com.mobithink.carbon.utils;

/**
 * Created by mplaton on 06/03/2017.
 */

public class Mathematics {



    public static Double convertRad(double input){
        return (Math.PI * input)/180;
    }

    public static Double calculateGPSDistance(double lat_a_degre, double lon_a_degre, double lat_b_degre, double lon_b_degre){
        Double distance;
        int R = 6378000; //Earth radius in meter

        Double lat_a = convertRad(lat_a_degre);
        Double lon_a = convertRad(lon_a_degre);
        Double lat_b = convertRad(lat_b_degre);
        Double lon_b = convertRad(lon_b_degre);

        distance = R * (Math.PI/2 - Math.asin( Math.sin(lat_b) * Math.sin(lat_a) + Math.cos(lon_b - lon_a) * Math.cos(lat_b) * Math.cos(lat_a)));
        return distance;
    }


}
