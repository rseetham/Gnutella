package Messages;

public class Query {
	
	private String fileName = "";
	private int ttl = 0;
	private int from;
	private Msg msg;
	
	/** constructor
	 * @param fileName
	 * @param ttl
	 * @param msg
	 * @param from
	 */
	public Query(String fileName, int ttl, Msg msg,int from)
	{
		this.fileName = fileName;
		this.ttl = ttl;
		this.msg = msg;
		this.from = from;
	}
	
	/**
	 * @return the ttl
	 */
	public int getTtl() {
		return ttl;
	}

	/**
	 * @param ttl the ttl to set
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * @return the from
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(int from) {
		this.from = from;
	}

	public String getFileName() {
		return fileName;
	}
	
	/**
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
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

