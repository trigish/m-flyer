package client;

import javax.swing.*;

/**
 * Created by Tim on 25.04.2016.
 */
public class ClientGUI {
    private JPanel contentPane;
    private JPanel firstRow;
    private JPanel centeredRow;
    private JTextArea outputText;
    private JTextPane inputMsg;
    private JPanel lastRow;
    private JComboBox comboUser;
    private JComboBox comboServer;
    private JButton btnRefreshBlog;
    private JButton btnSendMessage;

    public ClientGUI() {
        JFrame frame = new JFrame("ClientGUI");
        frame.setContentPane(this.contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
