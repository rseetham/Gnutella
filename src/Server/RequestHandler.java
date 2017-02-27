package Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import Messages.*;
import Server.Peer.Neighbor;

class RequestHandler implements Runnable
{
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    private Peer me;
    
    
    /** COnstructor
     * @param socket
     * @param me
     */
    RequestHandler( Socket socket, Peer me )
    {
    	this.me = me;
        this.socket = socket;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     * Accept connection, set up input/output streams. Get input and figure out whether the message was a obtain/query/query-hit
     */
    public void run()
    {
        try
        {
            System.out.println( "Received a connection" );
            in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            out = socket.getOutputStream();
            //out = new PrintWriter( socket.getOutputStream() );
            
            Gson gson = new Gson();
            
            String line = in.readLine();
            
            System.out.println("Accepted ("+me.getPeerId()+ "):" + line);
            
            Message msg = gson.fromJson(line, Message.class);
            

            
            if (msg.getMsgType().equals("Obtain")) {
            	handleObtain(msg.getMessage());
            }
            else if (msg.getMsgType().equals("QueryHit")) {
            	handleQueryHit(gson.fromJson(msg.getMessage(),QueryHit.class));
            }
            else if (msg.getMsgType().equals("Query")) {
            	handleQuery(gson.fromJson(msg.getMessage(),Query.class));
            }     
           
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    
    
    /** Respond to query hit if current peer was the one to send query, else forward query hit to neighbors
     * @param qhit
     * @throws Exception
     */
    private void handleQueryHit(QueryHit qhit) throws Exception {
    	close();    	
    	if (me.getPeerId() == qhit.getMsg().getPeerID()) {
    		synchronized(me) {
    		System.out.println("I asked for the file");
    		//sendObtain(qhit);
    		me.notify();
        	return;
    		}
    	}
    	
    	peerIPClock replyTo = me.getMessages().getUpstream(qhit.getMsg());  	
    	System.out.println(replyTo.peerId);
    	if (replyTo != null) {
    		Neighbor n = me.getNeighborIp(replyTo.peerId);
    		System.out.println("Propagate Hit");
    		propagateQHit(qhit, n.ip, n.port);
    	}
    	close();
	}
    
    /** Retrieve the file from the peer that has it
     * @param qhit
     * @throws Exception
     */
    public void sendObtain(QueryHit qhit) throws Exception {
    	
    	
    	String fileName = me.getFileToGet();
    	Message m = new Message("Obtain",fileName);
    	String[] ip = qhit.getPeerIP().split(":");
    	Path f = Paths.get("./TestFiles/"+fileName);
    	BufferedWriter writer = Files.newBufferedWriter(f, Charset.forName("US-ASCII"));
		Gson gson = new Gson();
    	
    	socket = new Socket( ip[0], Integer.parseInt(ip[1]) );
    	out= socket.getOutputStream();
    	new PrintStream(out).println(gson.toJson(m));
    	in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
    	String line = in.readLine();
    	while (line != null && line.length() > 0){
    		writer.write(line, 0, line.length());
    	}
    	
    	writer.close();
    	close();
    	synchronized(me) {
    		me.setFileToGet(null);
    	}
    	
    }
    
    /** If queryhit occurs, broadcast it to neighbors
     * @param qhit
     * @param ip
     * @param port
     * @throws Exception
     */
    private void propagateQHit (QueryHit qhit, String ip, int port) throws Exception {
    	System.out.println("QueryHit : ");
    	Gson gson = new Gson();
    	String qhitJson = gson.toJson(qhit,QueryHit.class);
    	System.out.println(qhitJson);
    	socket = new Socket(ip, port); 			
    	out = socket.getOutputStream();
		new PrintStream(out).println(gson.toJson(new Message("QueryHit",qhitJson),Message.class));
    }

	/** Send the file requested
	 * @param fileName
	 */
	private void handleObtain(String fileName) {
    	
    	System.out.println("Obtain!! "+me.getPeerId());
    	try {
    		Path f = Paths.get("../TestFiles/"+fileName);
			byte [] barray;  
			barray = Files.readAllBytes(f);
			System.out.println("Sending file : "+ fileName);
			out.write(barray, 0, barray.length);
			close();
    	
    	} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    

    /**
     * Close our connection
     */
    public void close() {
    	 try
         {
    		 in.close();
    		 out.close();
    		 socket.close();
         }
    	 catch( Exception e )
         {
             e.printStackTrace();
         }
         System.out.println( "Connection closed" );
    }
    
    /** Deals with 3 cases: if queried file is found in local storage, if query is a duplicate, or if query is new and needs to be recorded and forwarded
     * @param query
     * @throws Exception
     */
    public void handleQuery(Query query) throws Exception{//int msgID, int TTL, String filename){
    	//	boolean msgFound = false;
    	
    	if(me.getFilesList().contains(query.getFileName())){//file found in filelist, query hit
    		System.out.println("File Found");
    		QueryHit qh = new QueryHit(query.getFileName(), query.getMsg(),me.getIp()+":"+me.getPort());
        	close();
    		propagateQHit(qh, "127.0.0.1" ,query.getFrom());
    	}
    	else{
	    	peerIPClock pic = me.getMessages().getUpstream(query.getMsg());
	    	if(pic!=null){//message exists in messageHashMap, don't propagate
	    		System.out.println("Duplicate Message");
	    	}
	    	else {//message is new, propagate to neighbors
	    		int ttl = query.getTtl();
	    		if (ttl > 0) {
	    			query.setTtl(ttl-1);
	    			me.getMessages().addMsg(query.getMsg(), query.getFrom());
	    			propagateQuery(query);		    		
		    		close();
	    		} 		
	    	}
    	}
    	
    }
    
    /** Forwards query to neighbors if file was not found
     * @param query
     * @throws Exception
     */
    public void propagateQuery(Query query) throws Exception {
    	System.out.println("Message Being Sent To Neighbors");
		for(Neighbor nb: me.getNeighborsList()){
			if ( query.getFrom() == nb.getNbPort()) {
				query.setFrom(me.getPeerId());
				//Query q = new Query(query.getFileName(), query.getTtl(), query.getMsg(), me.getPeerId());
				sendQuery(query,nb.ip,nb.port);
			}
		}
    }
    
    /** sets up connection and sends query to given server
     * @param query
     * @param ip
     * @param port
     * @throws Exception
     */
    public void sendQuery(Query query, String ip, int port) throws Exception {
    	System.out.println("Sending Query");
    	Gson gson = new Gson();
    	Message m = new Message("Query",gson.toJson(query));
    	socket = new Socket( ip, port );
    	out = socket.getOutputStream();
    	new PrintStream(out).println(gson.toJson(m));
    	socket.close();
    	out.close();
    }
}