package Server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import Messages.Msg;

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
	
	public ConcurrentHashMap<Msg, peerIPClock> messages;
	
	public String fileToGet = null;
	private AtomicInteger systemClock;
	private static final int MAXSIZE = 25;
		
	private int ttl;
	
	/** Constructor
	 * @param id peer id
	 * @param ip peer ip address
	 * @param clientPort peer's client's port no
	 */
	public Peer (int id, String ip, int port, int ttl){
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.ttl = ttl;
		this.files = new ArrayList<String>();
		this.neighbors = new HashMap<Integer,Neighbor>();
		messages = new ConcurrentHashMap<Msg, peerIPClock>();
		this.systemClock = new AtomicInteger(0);
	}
	
	ConcurrentHashMap<Msg, peerIPClock> getMessageMap(){
		return messages;
	}
	
	int getTtl(){
		return ttl;
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
	
	/** add neighbor to peer's neighbor-hashmap
	 * @param peerId
	 * @param ip
	 * @param port
	 * @return
	 */
	Boolean addNeighbor(int peerId, String ip, int port) {
		neighbors.put(peerId, new Neighbor(ip,port));
		return true;
	}
	
	/**
	 * @param peerId
	 * @return
	 */
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
	
	/**
	 * @return
	 */
	public String getFileToGet() {
		return fileToGet;
	}

	/**
	 * @param fileToGet
	 */
	public synchronized void setFileToGet(String fileToGet) {
		this.fileToGet = fileToGet;
	}
	
	/**
	 * add new message to the hashmap
	 */
	public void addMsgToMap(Msg m, int peerId){
		messages.put(m,new peerIPClock(systemClock.getAndIncrement(),peerId));
	}
	
	/** Gets the peerIPClock (peerID of message sender + message timestamp) of a message. 
	 * Clears map entries once storage limit of 50 is reached
	 * @param msg
	 * @return PeerIPClock object
	 */
	public peerIPClock getUpstreamofMsg(Msg msg) {
		peerIPClock res = messages.get(msg);
		if (res == null)
			return null;
		if (systemClock.get() % 50 == 0) 
			clearMap();
		return res;
	}
	
	/**
	 * clear half of the oldest entries in the hashmap
	 */
	public void clearMap(){
		for(Iterator<Entry<Msg, peerIPClock>> it = messages.entrySet().iterator(); it.hasNext(); ) {
			Entry<Msg, peerIPClock> entry = it.next();
			if((systemClock.get() - entry.getValue().messageClock > MAXSIZE)) {
				it.remove();
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "client : " +ip+":"+port + " Id : "+ id + " Files : "
	+ files + "\nNeighbors : "+neighbors + "\n MessageQ" + printMap();
	}
	
	public String printMap() {
		String res = "";
		for (Msg key : messages.keySet()) {
		    res += key + " " + messages.get(key);
		}
		return res;
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
