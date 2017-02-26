package Messages;

public class Msg {
	
	private int PeerID;
	private int SeqID;
	
	public Msg (int PeerID, int SeqID) {
		this.PeerID = PeerID;
		this.SeqID = SeqID;
	}
	
	public int getPeerID() {
		return PeerID;
	}
	public void setPeerID(int peerID) {
		PeerID = peerID;
	}
	public int getSeqID() {
		return SeqID;
	}
	public void setSeqID(int seqID) {
		SeqID = seqID;
	}
}
