package Server;

public class peerIPClock {
//	String peerIP;
	int peerId;
	int messageClock;
	
	/** constructor for object to combine message timestamp and peerID of sender; used for message hashmap
	 * @param messageClock
	 * @param peerId
	 */
	public peerIPClock(int messageClock, int peerId) {
//		this.peerIP = peerIP;
		this.messageClock = messageClock;
		this.peerId = peerId;
	}
	
}
