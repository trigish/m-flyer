package server;

import common.Event;
import common.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

/**
 * This interface gives limited access to a server through Java RPI.
 */
public interface RpiServerAccess extends Remote {

    //actually private, but public because of RpiServerAccess interface

    public String getRmiAddress() throws RemoteException;
    public String getIpAddress() throws RemoteException;
    public String getName() throws RemoteException;;
    public void updateServer(Server pServerUpdate) throws Exception;
    public void addMessage(Message pMsg) throws RemoteException;
    public LinkedList<Message> getLocalMessages() throws RemoteException;
    public int getId() throws RemoteException;
    public void syncWith(RpiServerAccess pOtherServer) throws RemoteException;
    public String getTextLine() throws RemoteException;
    public int[][] getTimeTable() throws RemoteException;
    public List<Event> getUnknownEvents(RpiServerAccess pRequestingServer) throws RemoteException;
}