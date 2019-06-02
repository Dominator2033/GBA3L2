package ru.geekbrains;

public class EventTypeSecond extends EventBase{

    public EventTypeSecond(@EventsType int eventsType){
        super(Bus.TYPE_2, eventsType);
    }
}
