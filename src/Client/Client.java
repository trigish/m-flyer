package client;

import common.*;
import server.*;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;

/**
 * An object of this class is the main frame of the client-side program.
 */
public class Client {

    ClientGUI gui;
    User currentUser;
    RmiServerAccess currentServer;

    private Client() throws Exception {
        currentUser = User.getAllInstances().getFirst();
        currentServer = currentUser.getClosestServer();
        gui = new ClientGUI(this);

        //load already existing messages
        lookup();
    }

    /**
     * This main method should (only) be called on a machine that is used as a client.
     * @param args
     */
    public static void main(String[] args) {

        //init registry
        try {
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry inited.");
        } catch (RemoteException e) {
            //no problem. all init work is already done.
            System.out.println("RMI registry already started.");
        }

        try {
            Client client = new Client();
        }
        catch(Exception e) {
            System.out.println("Error during Client runtime. " + e);
        }
    }

    /**
     * Post a new message by requesting the server to save it as well as showing it in the gui.
     * @param pText
     */
    public void postMessage(String pText) throws RemoteException {

        Message msg = new Message(pText, currentUser);
        gui.showMessage(msg);

        currentServer.addMessage(msg);
    }

    /**
     * Receive all messages that are locally saved on the current server and show them in the gui.
     */
    public void lookup() throws RemoteException {
        gui.replaceMessages(currentServer.getLocalMessages());
    }

    /**
     * Log off current user and log on new user. This might force a server change, too.
     * @param pNewUser
     */
    public void switchUser(User pNewUser) {

        User oldUser = currentUser;

        //save new objects
        try {
            currentUser = pNewUser;
            currentServer = currentUser.getClosestServer();

            //if this user prefers a different server, refresh the shown messages
            if (oldUser.getClosestServerId() != currentServer.getId()) {
                gui.switchServer(currentServer);
                lookup();
            }

            System.out.println("Switched user from " + oldUser.getName() + " to " + currentUser.getName() + ".");
        }
        catch(Exception e) {
            System.out.println("Error during user switching. " + e.getMessage());
            switchUser(oldUser);
        }
    }

    /**
     * Tell the current serer to sync its messages with pOtherServer.
     * @param pOtherServer
     */
    public void syncCurrentServerWith(RmiServerAccess pOtherServer) throws RemoteException{

        //only do something if pOtherServer is really a different server (this makes gui handling easier)
        if(pOtherServer.getId() != currentServer.getId()) {

            //send actual request
            currentServer.syncWith(pOtherServer);

            //reload merged messages
            lookup();
        }
    }
}