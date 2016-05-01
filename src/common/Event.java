package common;

import server.*;

public class Event {
	private int clock;
	private Message msg;
	private Server server; // == msg.getAuthor().getClosestServer()

	//List<List<String>> ls2d = new ArrayList<List<String>>();
	//tt = new String [3][3];

	public Event (Message pMsg, Server pServer) {
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
}