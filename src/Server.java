import java.lang.IllegalArgumentException;

public class Server {

    public final int ID_AMERICA = 0;
    public final int ID_AUSTRALIA = 1;
    public final int ID_EUROPE = 2;
    public final int ID_ASIA = 3;
    public final int ID_AFRICA = 4;

    public final int numServers = 5;


    private int id;
    private String name;
    private String ipAddress;
    private boolean active;
    private Server[] allServers;

    public void initDummyServers() {
        allServers = new Server[numServers];

        allServers[ID_AMERICA] = new Server(ID_AMERICA, "America", "TODO IP");
        allServers[ID_AUSTRALIA] = new Server(ID_AUSTRALIA, "Australia", "TODO IP");
        allServers[ID_EUROPE] = new Server(ID_EUROPE, "Europe", "TODO IP");
        allServers[ID_ASIA] = new Server(ID_ASIA, "Asia", "TODO IP");
        allServers[ID_AFRICA] = new Server(ID_AFRICA, "Africa", "TODO IP");
    }


    private Server(int pId, String pName, String pIpAddress) {

        //use given params
        id = pId;
        name = pName;
        ipAddress = pIpAddress;
        active = false;

        System.out.println("Created inactive Server " + name);
    }

    public Server getInstanceById(int pId) {

        if(pId < 0 || pId >= numServers) {
            throw new java.lang.IllegalArgumentException("Invalid Server ID.");
        }

        return allServers[pId];
    }

    public static void start(int pServerId) {

        server = Server.getInstanceById(pServerId);
        server.active = true;
        server.broadcast(server);

        System.out.println("Started Server " + server.getName() );
    }

    public void broadcast(Server pServerUpdate) {
        for(int i=0;i < numServers; i++) {
            allServers[i].updateServer(pServerUpdate);
        }
    }

    private void updateServer(Server pServerUpdate) {
        allServers[pServerUpdate.getId()] = pServerUpdate;
    }

    public static void main(String[] args) {

        if(args.length == 1)
        {
            initDummyServers();

            int serverID = Integer.parseInt(args[0]);
            start(serverID);
        }
        else
        {
            system.out.println("Error during server start. Invalid number of Arguments.");
        }


    }

}