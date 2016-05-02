package server;

import common.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;
import java.lang.*;

public class Server extends UnicastRemoteObject implements RmiServerAccess {

    private int id;
    private String name;
    private String ipAddress;
    private boolean active;
    private LinkedList<Message> localMessages;
    private LinkedList<Event> log;
    private int[][] tt;

    public final static int ID_AMERICA = 0;
    public final static int ID_AUSTRALIA = 1;
    public final static int ID_EUROPE = 2;
    public final static int ID_ASIA = 3;
    public final static int ID_AFRICA = 4;

    public final static int numServers = 3;

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
    private static RmiServerAccess[] globalServerList = null;

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
        tt = new int [numServers][numServers];
        log = new LinkedList<Event>();
    }

    /**
     * See description of globalServerList.
     * @param pId The id of the requested server.
     * @return Either a dummy for the requested server or the "real" server (on a different machine).
     */
    public static RmiServerAccess getInstanceFromGlobalList(int pId) throws Exception{

        //validate parameters
        if(pId < 0 || pId >= numServers) {
            throw new java.lang.IllegalArgumentException("Invalid server ID.");
        }

        //init global server list when first accessing it
        if(globalServerList == null) {

            //init all possible servers with dummy data
            globalServerList = new RmiServerAccess[numServers];
            globalServerList[ID_AMERICA] = new Server(ID_AMERICA, "America", "localhost"); //TODO IP
            globalServerList[ID_AUSTRALIA] = new Server(ID_AUSTRALIA, "Australia", "localhost"); //TODO IP
            globalServerList[ID_EUROPE] = new Server(ID_EUROPE, "Europe", "localhost"); //TODO IP
            //globalServerList[ID_ASIA] = new Server(ID_ASIA, "Asia", "localhost");
            //globalServerList[ID_AFRICA] = new Server(ID_AFRICA, "Africa", "localhost");

            //try to replace each dummy server by the real one
            String serverStatus;
            for(int i=0; i < numServers; i++) {
                try {
                    globalServerList[i] = (RmiServerAccess) Naming.lookup(globalServerList[i].getRmiAddress());
                    serverStatus = "active";
                }
                catch(NotBoundException e) {
                    //do nothing here: in the case we can't connect to this server, we'll just keep the dummy element
                    serverStatus = "inactive";
                }
                catch(Exception e) {
                    //"real error"
                    serverStatus = "inactive (" + e + ")";
                }

                System.out.println("Created " + serverStatus + " server " + globalServerList[i].getName());
            }
        }

        return globalServerList[pId];
    }

    public static RmiServerAccess[] getAllInstances() throws Exception {

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


        try {
            //in order to start a machine, it must be a local instance instead of only a Rpi object => cast
            Server oldServerObject = (Server) getInstanceFromGlobalList(pServerId);

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
        catch(Exception e) {
            System.out.println("Error during server start. Probably this server object is not a local one." + e.getMessage());
        }
    }

    /**
     * This address needs to be used to lookup or bind this object to the Java RMI mechanism.
     * @return
     */
    public String getRmiAddress() { //actually private, but public because of RmiServerAccess interface
        return "//" + this.ipAddress + "/server" + this.id;
    }

    /**
     * Send pServerUpdate to all known servers, such that they can update their information about it.
     * @param pServerUpdate The server which should be broadcasted.
     */
    private void broadcast(Server pServerUpdate) {
        for(int i=0;i < numServers; i++) {
            try {
                RmiServerAccess other = getInstanceFromGlobalList(i);
                other.updateServer(pServerUpdate);
            }
            catch(Exception e) {
                System.out.println("Error during server broadcast." + e);
            }
        }
    }

    /**
     * Replace the current version of pServerUpdate with the given one.
     * @param pServerUpdate
     */
    public void updateServer(RmiServerAccess pServerUpdate) throws Exception { //actually private, but public because of RmiServerAccess interface {
        getInstanceFromGlobalList(0); //ensure that data has been initialized
        globalServerList[pServerUpdate.getId()] = pServerUpdate;
    }

    /**
     * This is the main method which should (only) be used on a machine that represents a server.
     * @param args First command line argument must be a valid server id.
     */
    public static void main(String[] args) {

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
    public String getIpAddress() throws RemoteException {
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
    public void addMessage(Message pMsg) throws RemoteException {

        //store the message locally
        localMessages.add(pMsg);

        //increase local time (needed for new event)
        tt[id][id]++;

        //create new event and add it to the log
        Event localEvent = new Event(pMsg, this);
        log.add(localEvent);
    }

    /**
     * Get all messages that are currently stored on this server.
     * @return
     */
    public LinkedList<Message> getLocalMessages() {
        return this.localMessages;
    }

    public String toString() {

        String str;
        try{
            str = getTextLine();
        }
        catch (Exception e)
        {
            str = super.toString();
        }

        return str;
    }

    /**
     * This method is just a "copy" of the toString method.
     * It's needed because the real toString method is not available in the RmiServerAccess.
     * @return
     */
    public String getTextLine() throws RemoteException {
        return this.getName() + " (" + this.getId() + " , " + this.getIpAddress() + ")";
    }

    /**
     * Sync all local messages (bidirectionally) with pOtherServer.
     * @param pOtherServer
     */
    public void syncWith(RmiServerAccess pOtherServer) throws RemoteException {

        // get potentially unknown events and merge them as well as the related messages with existing local data
        List<Event> potentiallyUnknownEvents = pOtherServer.getUnknownEvents(this);
        for(Event newEvent : potentiallyUnknownEvents) {
            if(!log.contains(newEvent)) { //we might get events we already know about, cause the other site didn't know that we knew. skip that.
                log.add(newEvent);
                localMessages.add(newEvent.getMsg());
            }
        }

        //update time tables
        combineRemoteTT(pOtherServer);

        //start garbage collection
        garbageCollectLog();
    }

    /**
     * Get all events stored in the local log, that might not be stored in the log of pRequestingServer.
     * @param pRequestingServer
     * @return
     */
    public List<Event> getUnknownEvents(RmiServerAccess pRequestingServer) throws RemoteException {

        LinkedList<Event> eventsToSend = new LinkedList<Event>();
        int reqId = pRequestingServer.getId();

        for(Event e : log) {
            if(tt[reqId][e.getServer().getId()] < e.getClock()) {
                eventsToSend.add(e);
            }
        }

        return eventsToSend;
    }

    /**
     * Merge the tt of pRemoteServer into the current local tt.
     * @param pRemoteServer
     * @throws RemoteException
     */
    private void combineRemoteTT (RmiServerAccess pRemoteServer) throws RemoteException {

        int[][] foreignTT = pRemoteServer.getTimeTable();
        int remoteId = pRemoteServer.getId();

        //element-wise maximum of both timetables
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                this.tt[i][j] = java.lang.Math.max(foreignTT[i][j], this.tt[i][j]);
            }
        }

        //choose element-wise maximum of local id-th row and remote remoteId-th row
        for(int j=0; j < numServers;j++) {
            this.tt[id][j] = java.lang.Math.max(this.tt[id][j], foreignTT[remoteId][j]);
        }
    }

    public int[][] getTimeTable() {
        return tt;
    }

    public int getLocalTime() throws RemoteException {
        return tt[id][id];
    }

    /**
     * Start garbage collection on current server and remove all elements from the log that are known by all other servers for sure, too.
     * @throws RemoteException
     */
    private void garbageCollectLog() throws RemoteException {

        //part 1: Get the minimum value of each column of tt = minimum knowledge for each site
        int x; // delete entry after x
        int [] arr = new int[numServers]; //arr[i] min value of i-th column.

        for (int j=0; j<numServers; j++) {
            int min = this.tt[0][j];

            for (int i=1; i<numServers; i++) {
                min = Math.min(min, this.tt[i][j]);
            }
            arr[j] = min;
        }

        //part 2: remove all elements older than minimum
        for (Event i : log) {
            if (i.getClock() <= arr[i.getServer().getId()])
                log.remove(i);
        }
    }
}