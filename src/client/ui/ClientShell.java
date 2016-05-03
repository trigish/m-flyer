package client.ui;

import client.Client;
import common.Message;
import common.User;
import server.RmiServerAccess;
import server.Server;

import java.rmi.RemoteException;
import java.util.Scanner;

import java.util.List;

/**
 * This graphical user interface will be shown on client side only.
 */
public class ClientShell extends ClientUI {

    public ClientShell(Client pController) {
        super(pController);
    }

    /**
     * Append a formatted message to current output.
     * @param pMsg The message that should be appended.
     */
    public void showMessage(Message pMsg) {
        System.out.println(pMsg.getAuthor() + ": " + pMsg.getText());
    }

    /**
     * Remove all currently shown messages and show new ones instead.
     * @param pMessages
     */
    public void replaceMessages(List<Message> pMessages) {
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
            System.out.println("" + pNewServer.getTextLine() + ", RMI " + (pNewServer instanceof Server ? "off" : "on"));
        }
        catch (Exception e) {
        }
    }

    public void acceptUserInput() {



        while(true) {

            // create a scanner so we can read the command-line input
            Scanner scanner = new Scanner(System.in);

            //  prompt for the user's name
            System.out.print("Enter command: ");

            // get their input as a String
            String userinput = scanner.nextLine();
            String cmd = new String(userinput);
            String param = "";
            if (cmd.indexOf(' ') >= 0) {
                cmd = userinput.substring(0, userinput.indexOf(' '));
                param = userinput.substring(userinput.indexOf(' ') + 1);
            }


            if (cmd.equals("lookup")) {
                try {
                    controller.lookup();
                } catch (RemoteException eListener) {
                    System.out.println(eListener.getMessage());
                }
            }
            else if (cmd.equals("post")) {
                try {
                    controller.postMessage(param);
                } catch (RemoteException eListener) {
                    System.out.println(eListener.getMessage());
                }
            }
            else if (cmd.equals("login")) {
                int userId = Integer.parseInt(param);
                controller.switchUser(User.getAllInstances().get(userId));
            }
            else if (cmd.equals("sync")) {
                try {
                    int serverId = Integer.parseInt(param);
                    controller.syncCurrentServerWith(Server.getInstanceFromGlobalList(serverId));
                } catch (Exception eListener) {
                    System.out.println(eListener.getMessage());
                }
            }
            else {
                System.out.println("Invalid user input. Try it again.");
            }



        }

    }
}
