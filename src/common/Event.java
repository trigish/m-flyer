package common;

import server.*;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Event implements Serializable {
	private int clock;
	private Message msg;
	private RmiServerAccess server; // == msg.getAuthor().getClosestServer()

	//List<List<String>> ls2d = new ArrayList<List<String>>();
	//tt = new String [3][3];

	public Event (Message pMsg, RmiServerAccess pServer) throws RemoteException {
		msg = pMsg;
		server = pServer;
		clock = pServer.getLocalTime();
	}

	public RmiServerAccess getServer() {
		return server;
	}


	public boolean isLocal (int cmpSiteID) {
		if (cmpSiteID == msg.getAuthor().getClosestServerId()) {
			return true;
		}
		else return false;
	}

	public int getClock() {
		return clock;
	}

	public Message getMsg() {
		return msg;
	}

	/**
	 * Compare two events. Events are equal if their local times equals as well as their server id.
	 * @param other
	 * @return
     */
	public boolean equals(Object other) {
		Event otherEvent = (Event) other;

		boolean res;
		try {
			res =otherEvent.getClock() == this.getClock() && otherEvent.getServer().getId() == this.getServer().getId();
		}
		catch (Exception e) {
			res = false;
		}


		return res;
	}
}