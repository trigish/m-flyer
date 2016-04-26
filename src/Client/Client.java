package client;

import common.*;

public class Client {

    ClientGUI gui;
    User currentUser;

    public Client() {

        currentUser = User.getAllInstances().getFirst();
        gui = new ClientGUI(this);
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

    public void postMessage(String pText) {

        Message msg = new Message(pText, currentUser);
        gui.showMessage(msg);

        //TODO implement
    }

    public void lookup() {
        //TODO implement
        //gui.replaceMessages();
    }

}