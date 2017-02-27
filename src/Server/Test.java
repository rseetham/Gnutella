package Server;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

import Messages.Message;
import Messages.Msg;
import Messages.Query;
import Server.Peer.Neighbor;

public class Test implements Runnable{
	
	private static Peer me;
	static Random rand;
	private static AtomicInteger seqid;
	
	
	public Test(Peer me){
		System.out.println("Hello!!! from test constructor");
		this.me = me;
		seqid = new AtomicInteger(0);
	}
	
	@Override
	public void run() {
		
		System.out.println("Hello!!!");
		ArrayList<String> files = filesToLookUp(me.getPeerId()%10);
		
		rand = new Random();
		
		try {
			sameLookUpTest(files);
			//diffLookUpTest(files);
    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void sameLookUpTest(ArrayList<String> files) throws Exception {
		int l = files.size();
    	String file = files.get(rand.nextInt(l));
    	long startTime = System.nanoTime();
    	for (int i  = 0; i < 1000; i++){
    		propagateQuery(file,me.getTtl());
    	}
    	long estimatedTime = System.nanoTime() - startTime;	    	
    	System.out.println("Time taken to query up a file 200 times "+estimatedTime/1000000000.0+"s");
	}
	
	static void diffLookUpTest(ArrayList<String> files) {
		int l = files.size();
    	long startTime = System.nanoTime();
    	String file;
    	for (int i  = 0; i < 1000; i++){
    		file = files.get(rand.nextInt(l));
    		
    	}
    	long estimatedTime = System.nanoTime() - startTime;	    	
    	System.out.println("Time taken to look up 1000 files "+estimatedTime/1000000000.0+"s");
	}
	
	synchronized static void propagateQuery(String fileName, int ttl) throws Exception {
    	Query q = new Query(fileName, ttl, new Msg(me.getPeerId(),seqid.getAndIncrement()));
		System.out.println("Message Being Sent To Neighbors");
		for(Neighbor nb: me.getNeighborsList())
			sendQuery(q,nb.ip,nb.port);
	}
	
	
	synchronized static void sendQuery(Query q, String server, int port) throws Exception {
    	Gson gson = new Gson();
    	Message m = new Message("Query",gson.toJson(q));
    	/*Socket socket;
        BufferedReader in;
        OutputStream out;*/
    	synchronized(me) {
    		Socket socket = new Socket( server, port );
        	PrintStream out = new PrintStream( socket.getOutputStream() );
        	out.println(gson.toJson(m));
        	socket.close();
        	out.close();
        	me.setFileToGet(q.getFileName());
        	me.wait();
    	}
    	
    }
	
	/**determine the files that this peer can look up
     * @param peerno used to determine the files it can look up
     * @return list of 
     */
    private static ArrayList<String> filesToLookUp(int peerno) {
    	ArrayList<String> files = new ArrayList<String>(23);
    	files.add("text_3.txt");
    	files.add("text_4.txt");
    	files.add("text_5.txt");
    	files.add("text_6.txt");
    	ArrayList<Integer> peers = new ArrayList<Integer>();
    	peers.add(new Integer(1));
    	peers.add(new Integer(2));
    	peers.add(new Integer(3));
    	peers.remove(new Integer(peerno));
    	peers.forEach(no -> {
    		for (int i = 0; i <= 9; i ++)
    			files.add("text_"+no+""+i+".txt");
    	});
		return files;
	}

}
