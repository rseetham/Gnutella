package Messages;

public class Message {

	private String msgType;
	
	private String message;
	
	public Message (String msgType, String message) {
		this.msgType = msgType;
		this.message = message;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
