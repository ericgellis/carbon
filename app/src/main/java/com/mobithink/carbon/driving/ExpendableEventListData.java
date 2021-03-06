package com.mobithink.carbon.driving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mplaton on 10/02/2017.
 */

public class ExpendableEventListData {

    public static HashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();
        List<String> listDataHeader  = new ArrayList<String>();

        listDataHeader.add("Carrefour à feux");
        listDataHeader.add("Carrefour giratoire");
        listDataHeader.add("Aménagement");
        listDataHeader.add("Itinéraire");
        listDataHeader.add("Stationnement");
        listDataHeader.add("Autres");


        List<String> crossroadWithTrafficLights = new ArrayList<String>();
        crossroadWithTrafficLights.add("Passage");
        crossroadWithTrafficLights.add("Conception/Giration");
        crossroadWithTrafficLights.add("Conception/Iot-refuge");
        crossroadWithTrafficLights.add("Conception/TAG");
        crossroadWithTrafficLights.add("Trafic saturé");
        crossroadWithTrafficLights.add("Gestion des cycles/trop courts");
        crossroadWithTrafficLights.add("Gestion des cycles/trop longs");
        crossroadWithTrafficLights.add("Gestion des cycles/piétons");
        crossroadWithTrafficLights.add("Gestion des cycles/TAG");
        crossroadWithTrafficLights.add("Conception/trop d'entrée");

        List<String> roundAboutCrossroad = new ArrayList<String>();
        roundAboutCrossroad.add("Passage");
        roundAboutCrossroad.add("Trafic");

        List<String> amenagement = new ArrayList<String>();
        amenagement.add("Difficulté de giration");
        amenagement.add("Voirie étroite");

        List<String> route = new ArrayList<String>();
        route.add("Tiroir");
        route.add("Boucle");
        route.add("Dénattage");
        route.add("Détours/rebroussement");
        route.add("Générateur non desservi");
        route.add("Intermodalité à améliorer");
        route.add("Marché, manifestation");
        route.add("Plan de circulation retardant le bus");
        route.add("prolongement possible");

        List<String> parking = new ArrayList<String>();
        parking.add("Latéral étroitesse");
        parking.add("Gênant");

        List<String> others = new ArrayList<String>();
        others.add("Trafic");
        others.add("Panne, incident technique bus");

        expandableListDetail.put(listDataHeader.get(0), crossroadWithTrafficLights);
        expandableListDetail.put(listDataHeader.get(1), roundAboutCrossroad);
        expandableListDetail.put(listDataHeader.get(2), amenagement);
        expandableListDetail.put(listDataHeader.get(3), route);
        expandableListDetail.put(listDataHeader.get(4), parking);
        expandableListDetail.put(listDataHeader.get(5), others);

        return expandableListDetail;
    }
}
