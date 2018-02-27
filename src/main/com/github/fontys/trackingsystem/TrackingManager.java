package com.github.fontys.trackingsystem;

import com.github.fontys.trackingsystem.tracking.Hardware;

import java.util.ArrayList;
import java.util.List;

public class TrackingManager {
    private List<Hardware> hardwareList;

    public TrackingManager() {
        hardwareList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Hardware h = new Hardware(i, String.format("Type %s", i));
            System.out.println(h.getType());
        }
    }
}
