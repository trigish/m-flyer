package client.ui;

import client.Client;
import common.Message;
import common.User;
import server.RmiServerAccess;
import server.Server;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This graphical user interface will be shown on client side only.
 */
abstract public class ClientUI {

    protected Client controller;

    public ClientUI(Client pController) {

        controller = pController;
    }

    /**
     * Append a formatted message to current output.
     * @param pMsg The message that should be appended.
     */
    abstract public void showMessage(Message pMsg);

    /**
     * Remove all currently shown messages and show new ones instead.
     * @param pMessages
     */
    abstract public void replaceMessages(List<Message> pMessages);

    /**
     * Shows the name of the newly-selected server.
     * This is not(!) the listener of the user combobox!
     * @param pNewServer
     */
    abstract public void switchServer(RmiServerAccess pNewServer);

    abstract public void acceptUserInput();
}
