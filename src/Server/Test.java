package Server;

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
	private static Integer lock;
	
	
	/** constructor
	 * @param me
	 */
	public Test(Peer me, Integer lock){
		Test.me = me;
		seqid = new AtomicInteger(0);
		Test.lock = lock;
		files = filesToLookUp(me.getPeerId()%10);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * sleep the thread for 20seconds to give time for all 10 peers to be set up
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}		
		rand = new Random();
		
		try {
			//sameLookUpTest(files);
			
			diffLookUpTest(files);
			
			// ALLOW THIS CALL FOR OBTAIN TEST!!
			// obtainTest("test.txt");
    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** obtain test that downloads the file given
	 * @param file
	 * @throws Exception
	 */
	static void obtainTest( String fileName) throws Exception {
		me.setFileToGet(fileName);
		System.out.println("Looking up file " + fileName);
		propagateQuery(fileName,me.getTtl());
		System.out.println("Got file " + fileName);
	}
	
	/** query test that queries a random files 200 times
	 * @param files
	 * @throws Exception 
	 */
	static void diffLookUpTest(ArrayList<String> files) throws Exception {

		int l = files.size();
    	String file;
    	long startTime = System.nanoTime();
    	for (int i  = 0; i < 200; i++){
    		file = files.get(rand.nextInt(l));
    		me.setFileToGet(file);
    		System.out.println("Looking up file " + file);
    		propagateQuery(file,me.getTtl());
    		System.out.println("Got file " + file);
    	}
    	long estimatedTime = System.nanoTime() - startTime;	    	
    	System.out.println("Time taken to query up a file 200 times "+estimatedTime/1000000000.0+"s");
    	
	}
	
	/** forward received query to neighbors by iterating through neighbor list
	 * @param fileName
	 * @param ttl
	 * @throws Exception
	 */
	static void propagateQuery(String fileName, int ttl) throws Exception {
		synchronized( me )
    	{
    	Query q = new Query(fileName, ttl, new Msg(me.getPeerId(),seqid.getAndIncrement()),me.getPeerId());
		System.out.println("Message Being Sent To Neighbors");
		for(Neighbor nb: me.getNeighborsList())
			sendQuery(q,nb.ip,nb.port);
    	me.setFileToGet(fileName); 
    	    if( lock.intValue() == 0 )
    	    { 
    	    	me.wait(30000);
    	    }   	     
    	    lock = Integer.valueOf(0);
    	    
    	}
    	
	}
		
	/** send a query to a neighbor
	 * @param q
	 * @param server
	 * @param port
	 * @throws Exception
	 */
	static void sendQuery(Query q, String server, int port) throws Exception {
    	Gson gson = new Gson();
    	Message m = new Message("Query",gson.toJson(q));
    		Socket socket = new Socket( server, port );
        	PrintStream out = new PrintStream( socket.getOutputStream() );
        	out.println(gson.toJson(m));
        	socket.close();
        	out.close();
    }
	
	/**determine the files that this peer can look up
     * @param peerno used to determine the files it can look up
     * @return list of 
     */
    private static ArrayList<String> filesToLookUp(int peerno) {
    	ArrayList<String> files = new ArrayList<String>(90);
    	int p[] = {1024,1025,1026,1027,1028,1029,1030,1031,1032,1033};
    	for (int n : p) {
    		if (n!= me.getPeerId())
    			IntStream.range(0, 10).forEach(i -> files.add("text"+n*10+i+".txt")); 
    	}
		return files;
	}

}
