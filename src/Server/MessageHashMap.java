package Server;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import Messages.Message;
import Messages.Msg;


public class MessageHashMap {
	
	//stores message clock - current run number of QueryHit
	ConcurrentHashMap<Msg, peerIPClock> messageMap;
	private AtomicInteger systemClock;
	private static final int MAXSIZE = 25;//TODO set max message storage
		
	public MessageHashMap(){
		this.systemClock = new AtomicInteger(0);
		this.messageMap = new ConcurrentHashMap<Msg, peerIPClock>();
	}
	
	/**
	 * clear half of the oldest entries in the hashmap
	 */
	public void clearMap(){
		for(Iterator<Entry<Msg, peerIPClock>> it = messageMap.entrySet().iterator(); it.hasNext(); ) {
			Entry<Msg, peerIPClock> entry = it.next();
			if((systemClock.get() - entry.getValue().messageClock > MAXSIZE)) {
				it.remove();
			}
		}
		//map.entrySet().removeIf(messageClock -> ((systemClock - messageClock) > 10)); //only keep 10 messages
	}

	/**
	 * add new message to the hashmap
	 */
	public void addMsg(Msg m, int peerId){
		messageMap.put(m,new peerIPClock(peerId, systemClock.getAndIncrement()));
	}
	
	public peerIPClock getUpstream(Msg msg) {
		peerIPClock res = messageMap.get(msg);
		if (res == null)
			return null;
		messageMap.remove(msg);
		if (systemClock.get() % 50 == 0) 
			clearMap();
		return res;
	}
	
	/**
	 * get all messages in the hashmap
	 */
//	public Msg 
	

	/**
	 * print contents of the hashmap
	 */
	public String toString(){
		//Set set = messageMap.entrySet();
	//	Iterator it = set.iterator();
		String res = "";
		Iterator<Entry<Msg, peerIPClock>> it = messageMap.entrySet().iterator();
		// Display elements
		while(it.hasNext()) {
			Entry<Msg, peerIPClock> entry = it.next();
			res+= entry.getKey() + ": "+entry.getValue().messageClock+" \n";
			res += entry.getValue().peerId;
		}
		return res;
	}

}