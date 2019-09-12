import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;
import java.util.Scanner;


public class Client extends UnicastRemoteObject implements GraphInterface{
    private static final long serialVersionUID = 1L;
    public GraphInterface server;
    public static String ip;
    public static int port = 8889;
    private static BufferedReader input_ ;
    public static boolean canplaymore=false;

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, IOException {
        System.out.println("The client has now started running.");
        input_ = new BufferedReader(new InputStreamReader(System.in));
        if (args.length < 1) {
            System.out.println("IP not provided. Running on 127.0.0.1");
            ip = "127.0.0.1";
        } else {
            ip = args[0];
        }
        new Client();
    }

    protected Client() throws MalformedURLException, RemoteException, NotBoundException, IOException  {
        Registry registry = LocateRegistry.getRegistry(ip, port); 
        System.out.println("Starting to connect to server");
        this.server = (GraphInterface) registry.lookup("RMIServer");
        input_ = new BufferedReader(new InputStreamReader(System.in));
        this.server.joinServer(this);
        System.out.println("You are now conected to server");
        while (1 == 1) {
            String command = input_.readLine();
            String ret = this.server.getMessage(command);
            System.out.println(ret);
        }
    }
    public String getMessage (String message) throws RemoteException, IOException {return "";}
    public void joinServer(GraphInterface client) throws RemoteException {}
}
