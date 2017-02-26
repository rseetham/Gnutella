package Messages;

public class Query {
	
	private String fileName = "";
	private int ttl = 0;
	private Msg msg;
	
	public Query(String fileName, int ttl, Msg msg)
	{
		this.fileName = fileName;
		this.ttl = ttl;
		this.msg = msg;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getTTL() {
		return ttl;
	}
	public void setTTL(int ttl) {
		this.ttl = ttl;
	}
	public Msg getMsg() {
		return msg;
	}
	public void setMsg(Msg msg) {
		this.msg = msg;
	}
	
}

