package Messages;

public class QueryHit {
	
	String fileName;
	Msg msg;
	String peerIP;
	
	
	/** constructor
	 * @param fileName
	 * @param msg
	 * @param peerIP
	 */
	public QueryHit(String fileName, Msg msg, String peerIP) {
		this.fileName = fileName;
		this.msg = msg;
		this.peerIP = peerIP;
	}
	
	/**
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @return
	 */
	public Msg getMsg() {
		return msg;
	}
	
	/**
	 * @param msg
	 */
	public void setMsg(Msg msg) {
		this.msg = msg;
	}
	
	/**
	 * @return
	 */
	public String getPeerIP() {
		return peerIP;
	}
	
	/**
	 * @param peerIP
	 */
	public void setPeerIP(String peerIP) {
		this.peerIP = peerIP;
	}

	
}
