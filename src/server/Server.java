package server;

import common.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;

public class Server extends UnicastRemoteObject implements RpiServerAccess {

    private int id;
    private String name;
    private String ipAddress;
    private boolean active;
    private LinkedList<Message> localMessages;

    public final static int ID_AMERICA = 0;
    public final static int ID_AUSTRALIA = 1;
    public final static int ID_EUROPE = 2;
    public final static int ID_ASIA = 3;
    public final static int ID_AFRICA = 4;

    public final static int numServers = 5;

    /**
     * This STATIC(!) list contains all existing servers.
     * Will be inited by the first call of getInstanceFromGlobalList(),
     * that's why we also need to call this getter for inner-class usage!
     * If the server wasn't reachable at that time, globalServerList will contain
     * only a dummy server for that entity. Otherwise, the "real" instance
     * (on another machine).
     * Also this is static, there should be a separate data instance for each
     * program execution (on a different machine).
     */
    private static Server[] globalServerList = null;

    /**
     * Create new server object.
     * Notice this method is private!
     * @param pId The id of the new server.
     * @param pName A user-friendly name for this server.
     * @param pIpAddress The ip address of this server.
     */
    private Server(int pId, String pName, String pIpAddress) throws RemoteException {
        super(0); //rmic

        //use given params
        id = pId;
        name = pName;
        ipAddress = pIpAddress;
        active = false;

        localMessages = new LinkedList<Message>();
    }

    /**
     * See description of globalServerList.
     * @param pId The id of the requested server.
     * @return Either a dummy for the requested server or the "real" server (on a different machine).
     */
    public static Server getInstanceFromGlobalList(int pId) throws Exception{

        //validate parameters
        if(pId < 0 || pId >= numServers) {
            throw new java.lang.IllegalArgumentException("Invalid server ID.");
        }

        //init global server list when first accessing it
        if(globalServerList == null) {

            //init all possible servers with dummy data
            globalServerList = new Server[numServers];
            globalServerList[ID_AMERICA] = new Server(ID_AMERICA, "America", "localhost"); //TODO IP
            globalServerList[ID_AUSTRALIA] = new Server(ID_AUSTRALIA, "Australia", "localhost"); //TODO IP
            globalServerList[ID_EUROPE] = new Server(ID_EUROPE, "Europe", "localhost"); //TODO IP
            globalServerList[ID_ASIA] = new Server(ID_ASIA, "Asia", "localhost"); //TODO IP
            globalServerList[ID_AFRICA] = new Server(ID_AFRICA, "Africa", "localhost"); //TODO IP

            //try to replace each dummy server by the real one
            String serverStatus;
            for(int i=0; i < numServers; i++) {
                try {
                    globalServerList[i] = (Server)Naming.lookup(globalServerList[i].getRmiAddress());
                    serverStatus = "active";
                }
                catch(Exception e) {
                    serverStatus = "inactive (" + e.getMessage() + ")";
                    //do nothing here: in the case we can't connect to this server, we'll just keep the dummy element
                }

                System.out.println("Created " + serverStatus + " server " + globalServerList[i].getName());
            }
        }

        return globalServerList[pId];
    }

    public static Server[] getAllInstances() throws Exception {

        //ensure intitialization
        getInstanceFromGlobalList(0);

        return globalServerList;
    }

    /**
     * Start the server with id pServerId.
     * Pay attention to the fact that this method is a static one!
     * @param pServerId The id of the server that should be started.
     * @throws Exception If the server is already running.
     */
    public static void start(int pServerId) throws Exception {

        Server oldServerObject = getInstanceFromGlobalList(pServerId);

        //exit, if server is already running
        //TODO actually there is no problem if the server is already running, but there is a problem if oldServerObject
        //isn't a local object at all (but instead a remote object from another machine). The latter is indicated by
        // the attribute "active".
        if(oldServerObject.active) {
            throw new Exception("Server with id " + pServerId + " could not be started, because it's already running!");
        }

        // bind server to the registry so it will be available in the network
        Naming.rebind(oldServerObject.getRmiAddress(), oldServerObject);
        System.out.println("New server address: " + oldServerObject.getRmiAddress());

        //set to running
        oldServerObject.active = true;

        //inform all known servers (including oldServerObject itself) about its new status
        oldServerObject.broadcast(oldServerObject);

        //log message
        System.out.println("Started server " + oldServerObject.getName() );
    }

    /**
     * This address needs to be used to lookup or bind this object to the Java RMI mechanism.
     * @return
     */
    private String getRmiAddress() {
        return "//" + this.ipAddress + "/server" + this.id;
    }

    /**
     * Send pServerUpdate to all known servers, such that they can update their information about it.
     * @param pServerUpdate The server which should be broadcasted.
     */
    private void broadcast(Server pServerUpdate) {
        for(int i=0;i < numServers; i++) {
            try {
                getInstanceFromGlobalList(i).updateServer(pServerUpdate);
            }
            catch(Exception e) {
                System.out.println("Error during server broadcast.");
            }
        }
    }

    /**
     * Replace the current version of pServerUpdate with the given one.
     * @param pServerUpdate
     */
    private void updateServer(Server pServerUpdate) throws Exception {
        getInstanceFromGlobalList(0); //ensure that data has been initialized
        globalServerList[pServerUpdate.getId()] = pServerUpdate;
    }

    /**
     * This is the main method which should (only) be used on a machine that represents a server.
     * @param args First command line argument must be a valid server id.
     */
    public static void main(String[] args) {

        //TODO: remove following temp code lines
        if(args.length == 0) {
            args = new String[1];
            args[0] = "0";
        }

        if(args.length == 1)
        {
            try {

                //init registry
                try {
                    LocateRegistry.createRegistry(1099);
                    System.out.println("RMI registry inited.");
                } catch (RemoteException e) {
                    //no problem. all init work is already done.
                    System.out.println("RMI registry already started.");
                }

                int serverID = Integer.parseInt(args[0]);
                Server.start(serverID);
            }
            catch (Exception e) {
                System.out.println("Error during server start. " + e.getMessage());
            }
        }
        else
        {
            System.out.println("Error during server start. Invalid number of Arguments.");
        }
    }

    /**
     * Get the unique id of this server.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Get a user-friendly name of this server.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ip address of this server.
     * @return
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Check, if this server is running.
     * @return True, if this server is active/running. Otherweise this object is just a dummy.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Add a newly posted message to the local memory.
     * @param pMsg
     */
    public void addMessage(Message pMsg) {
        localMessages.add(pMsg);

        //TODO extend log
        //TODO init sync??
    }

    /**
     * Get all messages that are currently stored on this server.
     * @return
     */
    public LinkedList<Message> getLocalMessages() {
        return this.localMessages;
    }

    public String toString() {
        return this.name + " (" + this.id + " , " + this.ipAddress + ")";
    }

    /**
     * Sync all local messages (bidirectionally) with pOtherServer.
     * @param pOtherServer
     */
    public void syncWith(Server pOtherServer) {
        //TODO implement
    }
}