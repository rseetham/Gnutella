package Messages;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Server.Peer;

public class MessageHashMap {
	/**
	 * stores message clock - current run number of QueryHit
	 */
	int messageClock;
	
	HashMap<Msg, Peer> messageMap = new HashMap();
	
	/**
	 * clear half the current entries in the hashmap
	 */
	public void clearMap(int systemClock){
		for(Iterator<Entry<Msg, Peer>> it = messageMap.entrySet().iterator(); it.hasNext(); ) {
		      Entry<Msg, Peer> entry = it.next();
		      if(entry.getKey().equals("test")) {
		        it.remove();
		      }
		    }
		//map.entrySet().removeIf(messageClock -> ((systemClock - messageClock) > 10)); //only keep 10 messages
	}
	
	/**
	 * add new message to the hashmap
	 */
	public void addMap(Msg m, Peer p){
		messageMap.put(m,p);
	}
	
	/**
	 * print contents of the hashmap
	 */
	public void printMap(){
	      Set set = messageMap.entrySet();
	      Iterator i = set.iterator();
	      
	      // Display elements
	      while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
	         System.out.print(me.getKey() + ": ");
	         System.out.println(me.getValue());
	      }
	}
	
}
