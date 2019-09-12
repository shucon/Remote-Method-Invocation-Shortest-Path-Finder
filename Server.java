import java.awt.* ;
import java.io.* ;
import java.net.* ;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.* ;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements GraphInterface {
    public static final long serialVersionUID = 1L ;
    public Vector<GraphInterface> clientList;
    public static String ip;
    // public Vector<Graph> Graphs;
    public HashMap<String, Graph> graphMap;
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        // Naming.rebind("RMIServer" ,new Server());
        if (args.length < 1) {
            System.out.println("IP not provided. Running on 127.0.0.1");
            ip = "127.0.0.1";
        } else {
            ip = args[0];
        }
        System.setProperty("java.rmi.server.hostname", ip); 
        // System.setProperty("java.rmi.server.codebase",".");
        Registry reg = LocateRegistry.createRegistry(8889);  
        Server foo = new Server();  
        try{ 
            reg.bind("RMIServer", foo);
        } catch (Exception e) {
            System.out.println("There's some error in starting the server.");
            System.out.println(e);
        }
    }
    public Server() throws RemoteException {
        this.clientList = new Vector<GraphInterface>();
        System.out.println("Server Started");
        this.graphMap = new HashMap<String, Graph>();
    }

    public void joinServer(GraphInterface client) {
        this.clientList.add(client);
        System.out.println("New Client Joined");
    }

    public String getMessage(String message) {
        StringTokenizer tokenedcommand = new StringTokenizer(message);
        String cmd = tokenedcommand.nextToken();
        System.out.println(message);
        if(cmd.equals("add_edge")) {
            String[] splitCmd = message.split(" ", 0);
            if(splitCmd.length != 4) {
                return "Error The command is not in correct format! \nEnter Command in Correct Format\nadd_edge <node1> <node2> <graph name>";
            } else {
                String node1 = tokenedcommand.nextToken();
                int nd1 = 0;
                String node2 = tokenedcommand.nextToken();
                int nd2 = 0;
                String graphName = tokenedcommand.nextToken();
                try {
                    nd1 = Integer.parseInt(node1); 
                    nd2 = Integer.parseInt(node2); 
                } catch (Exception e) {
                    return "Node Number must be an Integer";
                }

                if(!this.graphMap.containsKey(graphName)) {
                    Graph grp = new Graph(graphName);
                    graphMap.put(graphName,grp);
                }

                Graph selectedGraph = graphMap.get(graphName);
                String out = selectedGraph.addEdge(nd1,nd2);
                return out;
            }
        }
        else if(cmd.equals("shortest_distance")){
            String[] splitCmd = message.split(" ", 0);
            if(splitCmd.length != 4){
                return "Error The command is not in correct format! \nEnter Command in Correct Format\nadd_edge <node1> <node2> <graph name>";
            } else {
                String node1 = tokenedcommand.nextToken();
                int nd1 = 0;
                String node2 = tokenedcommand.nextToken();
                int nd2 = 0;
                String graphName = tokenedcommand.nextToken();
                try{
                    nd1 = Integer.parseInt(node1); 
                    nd2 = Integer.parseInt(node2); 
                } catch (Exception e) {
                    return "Node Number should be Integer";
                }
                if(!this.graphMap.containsKey(graphName)){
                    return "Graph doesn't exist";
                }
                int out = graphMap.get(graphName).ShortestPath(nd1,nd2);
                return Integer.toString(out); 

            }       
        }
        else if(cmd.equals("get_graph")){
            String[] splitCmd = message.split(" ", 0);
            if(splitCmd.length != 2){
                return "Error The command is not in correct format! Enter Command in Correct Format\nget_graph <graph name>";
            }
            String graphName = tokenedcommand.nextToken();
            if(!this.graphMap.containsKey(graphName)){
                return "Graph doesn't exist";
            }
            return graphMap.get(graphName).printGraph();
        } else {
            return "Please Enter a Valid Command";
        }
    }
}

class Graph {

    public int noOfVertices;
    public ArrayList<Integer>[] adjacencyList=null;
    String name;
    Graph(String name) {
        this.noOfVertices = 10000;
        this.adjacencyList=(ArrayList<Integer>[])new ArrayList[this.noOfVertices+1];
        this.name = name;
        for(int i = 0;i <= noOfVertices;i++) {
            this.adjacencyList[i] = new ArrayList<Integer>();
        }
    }
    public String addEdge(int u, int v){
        if(this.adjacencyList[u] == null) {
            this.adjacencyList[u] = new ArrayList<Integer>();
        }
        for(int vt: adjacencyList[u]) {
            if(v == vt) {
                return "An edge is already present";
            }
        }
        this.adjacencyList[u].add(v);
        return "Edge is added in Graph " + this.name;
    }

    public String printGraph() {
        String out = "Graph " + this.name + ":\n";
        int startVert = 1;
        ArrayList<Integer> edgeList;
        for(int i = startVert; i < this.noOfVertices+1; i++) {
            edgeList = this.adjacencyList[i];
            if(edgeList != null) {
                for(int v : edgeList) {
                    out+= "u : "+i+" v : "+v + "\n";
                    System.out.println("u : "+i+" v : "+v);
                }
            }
        }
        return out;
    }
    
    public int ShortestPath(int source, int dest) {
        if (source == dest) {
            return 0;
        } else {

            Queue<Integer> queue = new LinkedList<Integer>();
            HashMap<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
            Stack<Integer> pathStack = new Stack<Integer>();
            HashMap<Integer, Integer> distance = new HashMap<Integer, Integer>();

            queue.add(source);
            pathStack.add(source);
            visited.put(source, true);
            distance.put(source,0);

            while(!queue.isEmpty()) {
                int u = queue.poll();
                ArrayList<Integer> adjList = this.adjacencyList[u];

                for (int v : adjList) {
                    if(!visited.containsKey(v)) {
                        queue.add(v);
                        visited.put(v, true);
                        pathStack.add(v);
                        distance.put(v,distance.get(u)+1);
                        if(u == dest)
                            break;
                    }
                }
            }

            if (distance.containsKey(dest)) {
                return distance.get(dest);
            } else {
                return -1;
            }
        }
    }
}
