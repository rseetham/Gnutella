import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author rekha
 *
 */
public class Peer {
	
	/**
	 * stores the peer id
	 */
	private int id;
	/**
	 * stores the ip address of the peer
	 */
	private String ip;
	/**
	 * stores the port the peer's server is hosted on
	 */
	private int serverPort;
	/**
	 * stores the list of files the peer has
	 */
	private ArrayList<String> files;
	/**
	 * stores the list of neighbors the peer has
	 */
	private ArrayList<Neighbor> neighbors;
	
	/** Constructor
	 * @param id peer id
	 * @param ip peer ip address
	 * @param serverPort peer's server's port no
	 */
	public Peer (int id, String ip, int serverPort){
		this.id = id;
		this.ip = ip;
		this.serverPort = serverPort;
		this.files = new ArrayList<String>();
	}
	
	/**
	 * @return peer id
	 */
	int getPeerId (){
		return id;
	}
	
	/**
	 * @return peer ip
	 */
	String getIp () {
		return ip;
	}
	
	/**
	 * @param peerIp
	 */
	void setIp (String peerIp) {
		this.ip = peerIp;
	}
	
	/**
	 * @return server's ip address
	 */
	String getServerIp() {
		return ip+":"+serverPort;
	}
	
	/**
	 * @param serverPort
	 */
	void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * @return index list of files
	 */
	ArrayList<String> getFilesList(){
		return files;
	}
	
	/**
	 * @return index list of neighbors
	 */
	ArrayList<Neighbor> getNeighborsList(){
		return neighbors;
	}
	
	Boolean addNeighbor(String ip, int port) {
		neighbors.add(new Neighbor(ip,port));
		return true;
	}
	
	/** Adds a file to the peer's index
	 * @param file name
	 */
	void addFile (String file) {
		Iterator<String> it = files.iterator();
		while(it.hasNext()){
			if (it.next().equals(file))
				return;
		}
		files.add(file);
	}
	
	
	/** Removes file from peer's index
	 * @param file name
	 * @return true is success
	 */
	Boolean removeFile (String file) {
		Iterator<String> it = files.iterator();
		while(it.hasNext()){
			if (it.next().equals(file)){
				files.remove(file);
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
				return "Server : " +ip+":"+serverPort + " Id : "+ id + " Files : "+ files;
		
	}
	
	class Neighbor{
		String ip;
		int port;
		
		Neighbor (String ip, int port) {
			this.ip = ip;
			this.port = port;
		}
		
		public String toString(){
			return ip + ":" + port;
		}
	}

}
