package client;

import common.*;
import server.*;

/**
 * An object of this class is the main frame of the client-side program.
 */
public class Client {

    ClientGUI gui;
    User currentUser;
    Server currentServer;

    private Client() {
        currentUser = User.getAllInstances().getFirst();
        currentServer = Server.getInstanceFromGlobalList(currentUser.getClosestServerId());
        gui = new ClientGUI(this);
    }

    /**
     * This main method should (only) be called on a machine that is used as a client.
     * @param args
     */
    public static void main(String[] args) {
        Client client = new Client();
    }

    /**
     * Post a new message by requesting the server to save it as well as showing it in the gui.
     * @param pText
     */
    public void postMessage(String pText) {

        Message msg = new Message(pText, currentUser);
        gui.showMessage(msg);

        currentServer.addMessage(msg);
    }

    /**
     * Receive all messages that are locally saved on the current server and show them in the gui.
     */
    public void lookup() {
        gui.replaceMessages(currentServer.getLocalMessages());
    }
}