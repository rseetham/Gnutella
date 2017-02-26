package Server;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Messages.Msg;


public class MessageHashMap {
	/**
	 * stores message clock - current run number of QueryHit
	 */
	HashMap<Msg, peerIPClock> messageMap = new HashMap();
	private static final int MAXSIZE = 10;//TODO set max message storage

	/**
	 * clear half of the oldest entries in the hashmap
	 */
	public void clearMap(int systemClock){
		for(Iterator<Entry<Msg, peerIPClock>> it = messageMap.entrySet().iterator(); it.hasNext(); ) {
			Entry<Msg, peerIPClock> entry = it.next();
			if((systemClock - entry.getValue().messageClock > MAXSIZE)) {
				it.remove();
			}
		}
		//map.entrySet().removeIf(messageClock -> ((systemClock - messageClock) > 10)); //only keep 10 messages
	}

	/**
	 * add new message to the hashmap
	 */
	public void addMap(Msg m, peerIPClock pic){
		messageMap.put(m,pic);
	}

	/**
	 * print contents of the hashmap
	 */
	public void printMap(){
		Set set = messageMap.entrySet();
	//	Iterator it = set.iterator();

		Iterator<Entry<Msg, peerIPClock>> it = messageMap.entrySet().iterator();
		// Display elements
		while(it.hasNext()) {
			Entry<Msg, peerIPClock> entry = it.next();
			System.out.print(entry.getKey() + ": ");
			System.out.println(entry.getValue().messageClock+" ");
			System.out.println(entry.getValue().peerIP);
		}
	}

}
