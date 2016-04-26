package client;

import common.*;
import server.*;

public class Client {

    ClientGUI gui;
    User currentUser;
    Server currentServer;

    public Client() {

        currentUser = User.getAllInstances().getFirst();
        currentServer = Server.getInstanceFromGlobalList(currentUser.getClosestServerId());
        gui = new ClientGUI(this);
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

    public void postMessage(String pText) {

        Message msg = new Message(pText, currentUser);
        gui.showMessage(msg);

        currentServer.addMessage(msg);
    }

    public void lookup() {
        gui.replaceMessages(currentServer.getLocalMessages());
    }

}