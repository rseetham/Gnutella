package Messages;

public class Query {
	
	private String fileName = "";
	private int ttl = 0;
	private Msg msg;
	
	/** constructor
	 * @param fileName
	 * @param ttl
	 * @param msg
	 */
	public Query(String fileName, int ttl, Msg msg)
	{
		this.fileName = fileName;
		this.ttl = ttl;
		this.msg = msg;
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
	public int getTTL() {
		return ttl;
	}
	
	/**
	 * @param ttl
	 */
	public void setTTL(int ttl) {
		this.ttl = ttl;
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
	
}

