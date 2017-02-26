package Messages;

public class QueryHit {
	
	String fileName;
	Msg msg;
	String peerIP;
	
	
	public QueryHit(String fileName, Msg msg, String peerIP) {
		this.fileName = fileName;
		this.msg = msg;
		this.peerIP = peerIP;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Msg getMsg() {
		return msg;
	}
	public void setMsg(Msg msg) {
		this.msg = msg;
	}
	public String getPeerIP() {
		return peerIP;
	}
	public void setPeerIP(String peerIP) {
		this.peerIP = peerIP;
	}

	
}
