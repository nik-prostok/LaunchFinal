package com.example.nik.launch;

import java.util.ArrayList;

/**
 * Created by Nik on 19.02.2018.
 */

public class Rocket {
    private String rocketName;
    Integer flightNumber;

    private ArrayList<String> fields = new ArrayList<String>();

    public Rocket(String rocketName, String dateTime, String details, String mission_patch, Integer flightNumber,
                  String videoLink, String wiki){
        this.rocketName = rocketName;
        this.fields.add(dateTime);
        this.fields.add(details);
        this.fields.add(videoLink);
        this.fields.add(wiki);
        this.fields.add(mission_patch);
        this.flightNumber = flightNumber;
    }

    public String getRocketName() {
        return rocketName;
    }

    public String getChild(int i){
        return fields.get(i);
    }

    public Integer getCountChild(){
        return fields.size();
    }

    public Integer getFlightNumber() {return  this.flightNumber; }
}
