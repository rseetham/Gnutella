package Server;

public class peerIPClock {
	String peerIP;
	int peerId;
	int messageClock;
	
	public peerIPClock(String peerIP, int messageClock, int peerId) {
		this.peerIP = peerIP;
		this.messageClock = messageClock;
		this.peerId = peerId;
	}
	
}
