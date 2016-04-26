package client;

import common.*;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;

/**
 * Created by Tim on 25.04.2016.
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

    private Client controller;

    public ClientGUI(Client pController) {

        controller = pController;

        //load available users
        comboUser.setModel(new DefaultComboBoxModel(User.getAllInstances().toArray()));

        //TODO load available servers

        //create and show main frame
        JFrame frame = new JFrame("ClientGUI");
        frame.setContentPane(this.contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



        btnPost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //change gui
                String msgText = inputMsg.getText();
                inputMsg.setText("");

                //send new data
                controller.postMessage(msgText);
            }
        });
        btnLookup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.lookup();
            }
        });
    }

    public void showMessage(Message pMsg) {
        //append new message to current output
        try {
            HTMLDocument doc = (HTMLDocument) outputText.getStyledDocument();
            doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), "<font size=\"18\"><b>" + pMsg.getAuthor() + ":</b> " + pMsg.getText() + "</font><br>");
        }
        catch(Exception e) {
            //TODO
        }
    }

    public void replaceMessages(List<Message> pMsgs) {
        outputText.setText("");
        for(Message msg : pMsgs) {
            showMessage(msg);
        }
    }
}
