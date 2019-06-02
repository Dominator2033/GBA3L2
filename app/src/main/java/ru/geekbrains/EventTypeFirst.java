package ru.geekbrains;

public class EventTypeFirst extends EventBase{

    public EventTypeFirst(@EventsType int eventsType){
        super(Bus.TYPE_1, eventsType);
    }
}
