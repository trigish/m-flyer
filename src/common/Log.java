package common;

import server.*;
import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;

//TODO either replace log by simple list or move functions from server to here
public class Log {
    List<Event> events = new ArrayList<Event>();
    private Server server;

    public Log(Server pServer) {
        server = pServer;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void handleEvent(Event event) {
        events.add(event);
    }
}