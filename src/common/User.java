package common;

import java.util.*;

public class User {

    private int id;
    private String name;
    private int closestServerId;

    private static LinkedList<User> allInstances = null;

    public User(int pId, String pName, int pClosestServerId) {
        id = pId;
        name = pName;
        closestServerId = pClosestServerId;
    }

    public static LinkedList<User> getAllInstances() {

        //create user instances only once
        if(allInstances == null) {
            allInstances = new LinkedList<User>();
            allInstances.add(new User(0, "A", 0));
            allInstances.add(new User(1, "B", 1));
            allInstances.add(new User(2, "C", 2));
            allInstances.add(new User(3, "D", 3));
            allInstances.add(new User(4, "E", 4));
            allInstances.add(new User(5, "F", 0));
        }

        return allInstances;
    }

    public String toString() {
        return this.name + " (" + this.id + ")";
    }

}