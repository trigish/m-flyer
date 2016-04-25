package server

public class Server {

    public final int ID_AMERICA = 0;
    public final int ID_AUSTRALIA = 1;
    public final int ID_EUROPE = 2;
    public final int ID_ASIA = 3;
    public final int ID_AFRICA = 4;

    public final numServers = 5;


    private int id;
    private String name;
    private String ipAddress;
    private Server[] allServers[];

    public void initAllServers() {
        allServers = new Server[self::numServers];

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
    }

    public getInstanceById(int pId) {

        if(pId < 0 || pId >= numServers) {
            throw new InvalidArgumentException("Invalid Server ID.");
        }

        return allServers[pId];
    }

    public static void main(String[] args) {
        initAllServers();
    }

}