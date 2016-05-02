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
public interface RmiServerAccess extends Remote {

    //actually private, but public because of RmiServerAccess interface

    public String getRmiAddress() throws RemoteException;
    public String getIpAddress() throws RemoteException;
    public String getName() throws RemoteException;;
    public void updateServer(RmiServerAccess pServerUpdate) throws Exception;
    public void addMessage(Message pMsg) throws RemoteException;
    public LinkedList<Message> getLocalMessages() throws RemoteException;
    public int getId() throws RemoteException;
    public void syncWith(RmiServerAccess pOtherServer) throws RemoteException;
    public String getTextLine() throws RemoteException;
    public int[][] getTimeTable() throws RemoteException;
    public List<Event> getUnknownEvents(RmiServerAccess pRequestingServer) throws RemoteException;
    public int getLocalTime() throws RemoteException;
    public boolean isActive() throws RemoteException;
}