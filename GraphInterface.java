import java.io.IOException;
import java.rmi.RemoteException;

public interface GraphInterface extends java.rmi.Remote {
	public String getMessage (String message) throws RemoteException, IOException ;
    public void joinServer(GraphInterface client) throws RemoteException ;
}
