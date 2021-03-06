package client.ui;

import client.Client;
import common.*;
import server.*;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;

import java.awt.event.KeyAdapter;
import java.rmi.RemoteException;
import java.util.*;

/**
 * This graphical user interface will be shown on client side only.
 */
public class ClientGUI extends ClientUI {
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

    public ClientGUI(Client pController) {
        super(pController);

        //load available servers
        try {
            comboServer.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList list,
                                                              Object value,
                                                              int index,
                                                              boolean isSelected,
                                                              boolean cellHasFocus) {
                    try {
                        RmiServerAccess server = (RmiServerAccess) value;
                        value = server.getTextLine();
                    }
                    catch(Exception e) {
                        System.out.println(e);
                    }

                    return super.getListCellRendererComponent(list, value,
                            index, isSelected, cellHasFocus);
                }
            });
            comboServer.setModel(new DefaultComboBoxModel(Server.getAllInstances()));
            comboServer.updateUI();

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
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);


        //send messages "on Enter"
        int condition = JComponent.WHEN_FOCUSED;
        InputMap iMap = inputMsg.getInputMap(condition);
        ActionMap aMap = inputMsg.getActionMap();
        String enter = "enter";
        iMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), enter);
        aMap.put(enter, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                btnPost.doClick();
            }
        });

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
                    controller.syncCurrentServerWith((RmiServerAccess) comboServer.getSelectedItem());
                }
                catch (RemoteException eListener) {
                    System.out.println(eListener.getMessage());
                }
            }
        });
        inputMsg.addKeyListener(new KeyAdapter() {
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
    public void switchServer(RmiServerAccess pNewServer) {
        try {
            labelCurrentServer.setText("" + pNewServer.getTextLine() + ", RMI " + (pNewServer instanceof Server ? "off" : "on"));
        }
        catch (Exception e){

        }
    }

    public void acceptUserInput() {
        // GUI can accept user input at each time
    }
}
