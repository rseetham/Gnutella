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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + PeerID;
		result = prime * result + SeqID;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Msg)) {
			return false;
		}
		Msg other = (Msg) obj;
		if (PeerID != other.PeerID || SeqID != other.SeqID) {
			return false;
		}
		return true;
	}
}
