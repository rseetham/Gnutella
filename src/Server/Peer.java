package Server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
	 * stores the port the peer's client is hosted on
	 */
	private int port;
	/**
	 * stores the list of files the peer has
	 */
	private ArrayList<String> files;
	/**
	 * stores the list of neighbors the peer has
	 */
	private HashMap<Integer,Neighbor> neighbors;
	
	private MessageHashMap messages;
	
	private String fileToGet = null;
	
	/** Constructor
	 * @param id peer id
	 * @param ip peer ip address
	 * @param clientPort peer's client's port no
	 */
	public Peer (int id, String ip, int port){
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.files = new ArrayList<String>();
		this.neighbors = new HashMap<Integer,Neighbor>();
		messages = new MessageHashMap();
	}
	
	MessageHashMap getMessages(){
		return messages;
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
	 * @return client's ip address
	 */
	String getServeIp() {
		return ip+":"+port;
	}
	
	int getPort() {
		return port;
	}
	
	/**
	 * @param clientPort
	 */
	void setClientPort(int port) {
		this.port = port;
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
	Collection<Neighbor> getNeighborsList(){
		return neighbors.values();
	}
	
	Boolean addNeighbor(int peerId, String ip, int port) {
		neighbors.put(peerId, new Neighbor(ip,port));
		return true;
	}
	
	Neighbor getNeighborIp(int peerId) {
		return neighbors.get(new Integer(peerId));
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
	
	public String getFileToGet() {
		return fileToGet;
	}

	public void setFileToGet(String fileToGet) {
		this.fileToGet = fileToGet;
	}

	public String toString() {
		return "client : " +ip+":"+port + " Id : "+ id + " Files : "
	+ files + "\nNeighbors : "+neighbors;
	}
	
	class Neighbor{
		

		String ip;
		int port;
		
		public int getNbPort(){
			return port;
		}
		
		public String getNbIP(){
			return ip;
		}
		
		Neighbor (String ip, int port) {
			this.ip = ip;
			this.port = port;
		}
		
		public String toString(){
			return " IP : "+ip + ":" + port;
		}
	}

}
