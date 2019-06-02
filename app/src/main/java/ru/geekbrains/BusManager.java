package ru.geekbrains;

public class BusManager {
    private Bus model;

    private static BusManager busManager;
    static {
        busManager = new BusManager();
    }

    private BusManager(){
        model = new Bus();
    }

    public static BusManager getInstance() {
        return busManager;
    }

    public Bus getBus() {
        return model;
    }
}
