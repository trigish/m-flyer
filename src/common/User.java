package common;

import java.util.*;
import server.*;

/**
 * An object of this class represents a person which is using the blog.
 */
public class User {

    private int id;
    private String name;

    /**
     * This user will always connect to the server with id closestServerId.
     */
    private int closestServerId;

    private static LinkedList<User> allInstances = null;

    /**
     * All instances of this class are known before runtime. So this constructor is private.
     * Use getAllInstances() as a public interface.
     * @param pId
     * @param pName
     * @param pClosestServerId
     */
    private User(int pId, String pName, int pClosestServerId) {
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getClosestServerId() {
        return closestServerId;
    }

    public Server getClosestServer() {
        return Server.getInstanceFromGlobalList(this.getClosestServerId());
    }
}