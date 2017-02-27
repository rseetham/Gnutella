package Messages;

public class Message {

	private String msgType;
	
	private String message;
	
	/** constructor
	 * @param msgType
	 * @param message
	 */
	public Message (String msgType, String message) {
		this.msgType = msgType;
		this.message = message;
	}

	/**
	 * @return
	 */
	public String getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
