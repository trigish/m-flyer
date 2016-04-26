package client;

import common.*;
import server.*;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.rmi.RemoteException;
import java.util.*;

/**
 * This graphical user interface will be shown on client side only.
 */
public class ClientGUI {
    private JPanel contentPane;
    private JPanel firstRow;
    private JPanel centeredRow;
    private JTextPane outputText;
    private JTextPane inputMsg;
    private JPanel lastRow;
    private JComboBox comboUser;
    private JComboBox comboServer;
    private JButton btnLookup;
    private JButton btnPost;
    private JLabel labelCurrentServer;
    private JButton syncButton;

    private Client controller;

    public ClientGUI(Client pController) {

        controller = pController;

        //load available servers
        try {
            comboServer.setModel(new DefaultComboBoxModel(Server.getAllInstances()));
        }
        catch(Exception e) {
            System.out.println("Error during setting up combobox." + e);
        }

        //load available users
        try {
            comboUser.setModel(new DefaultComboBoxModel(User.getAllInstances().toArray()));
            switchServer(((User) comboUser.getSelectedItem()).getClosestServer());
        }
        catch(Exception e) {
            System.out.println("Error during setting up combobox." + e);
        }

        //create and show main frame
        JFrame frame = new JFrame("ClientGUI");
        frame.setContentPane(this.contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        btnPost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //change gui
                    String msgText = inputMsg.getText();
                    inputMsg.setText("");

                    //send new data
                    controller.postMessage(msgText);
                }
                catch (RemoteException eListener) {
                    System.out.println(eListener.getMessage());
                }
            }
        });
        btnLookup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.lookup();
                }
                catch (RemoteException eListener) {
                    System.out.println(eListener.getMessage());
                }
            }
        });
        comboUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.switchUser((User) comboUser.getSelectedItem());
            }
        });
        syncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.syncCurrentServerWith((Server) comboServer.getSelectedItem());
                }
                catch (RemoteException eListener) {
                    System.out.println(eListener.getMessage());
                }
            }
        });
    }

    /**
     * Append a formatted message to current output.
     * @param pMsg The message that should be appended.
     */
    public void showMessage(Message pMsg) {

        try {
            HTMLDocument doc = (HTMLDocument) outputText.getStyledDocument();
            doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), "<font size=\"18\"><b>" + pMsg.getAuthor() + ":</b> " + pMsg.getText() + "</font><br>");
        }
        catch(Exception e) {
            //TODO
        }
    }

    /**
     * Remove all currently shown messages and show new ones instead.
     * @param pMessages
     */
    public void replaceMessages(List<Message> pMessages) {
        outputText.setText("");
        for(Message msg : pMessages) {
            showMessage(msg);
        }
    }

    /**
     * Shows the name of the newly-selected server.
     * This is not(!) the listener of the user combobox!
     * @param pNewServer
     */
    public void switchServer(RpiServerAccess pNewServer) {
        labelCurrentServer.setText("" + pNewServer);

        //TODO we might also block the server-related item in the combobox, but this isn't possible out of the box..
    }
}
