package Server;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.google.gson.Gson;

import Messages.Message;
import Messages.Msg;
import Messages.Query;
import Server.Peer.Neighbor;

public class Test implements Runnable{
	
	private static Peer me;
	static Random rand;
	private static AtomicInteger seqid;
	ArrayList<String> files;
	
	
	/** constructor
	 * @param me
	 */
	public Test(Peer me){
		this.me = me;
		seqid = new AtomicInteger(0);
		files = filesToLookUp(me.getPeerId()%10);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * sleep the thread for 20seconds to give time for all 10 peers to be set up
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("Hello!!!");
		
		rand = new Random();
		
		try {
			//sameLookUpTest(files);
			diffLookUpTest(files);
    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** query a file held by same peer
	 * @param files
	 * @throws Exception
	 */
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
	
	/** query a file held by another peer
	 * @param files
	 * @throws Exception 
	 */
	static void diffLookUpTest(ArrayList<String> files) throws Exception {

		int l = files.size();
    	String file;
    	long startTime = System.nanoTime();
    	for (int i  = 0; i < 1000; i++){
    		file = files.get(rand.nextInt(l));
    		propagateQuery(file,me.getTtl());
    	}
    	long estimatedTime = System.nanoTime() - startTime;	    	
    	System.out.println("Time taken to query up a file 200 times "+estimatedTime/1000000000.0+"s");
	}
	
	/** forward received query to neighbors by iterating through neighbor list
	 * @param fileName
	 * @param ttl
	 * @throws Exception
	 */
	synchronized static void propagateQuery(String fileName, int ttl) throws Exception {
    	Query q = new Query(fileName, ttl, new Msg(me.getPeerId(),seqid.getAndIncrement()),me.getPeerId());
		System.out.println("Message Being Sent To Neighbors");
		for(Neighbor nb: me.getNeighborsList())
			sendQuery(q,nb.ip,nb.port);
	}
	
	
	/** send a query to a neighbor
	 * @param q
	 * @param server
	 * @param port
	 * @throws Exception
	 */
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
    	ArrayList<Integer> peers = new ArrayList<Integer>();
    	int p[] = {1024,1025,1026,1027,1028,1029,1030,1031,1032,1033};
    	for (int n : p) {
    		IntStream.range(0, 10).forEach(i -> files.add("text"+n*10+i+".txt")); 
    	}
		return files;
	}

}
