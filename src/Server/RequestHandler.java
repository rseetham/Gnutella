package Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import Messages.*;
import Server.Peer.Neighbor;
import Server.MessageHashMap;

class RequestHandler implements Runnable
{
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    private Peer me;
    private int remotePort;
    private SocketAddress remoteAddress;
    
    RequestHandler( Socket socket, Peer me )
    {
    	this.me = me;
        this.socket = socket;
    }

    public void run()
    {
        try
        {
            System.out.println( "Received a connection" );
            in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            out = socket.getOutputStream();
            //out = new PrintWriter( socket.getOutputStream() );
            
            Gson gson = new Gson();
            
            // Echo lines back to the client until the client closes the connection or we receive an empty line
            String line = in.readLine();
            
            System.out.println("Accepted ("+me.getPeerId()+ "):" + line);
            
            Message msg = gson.fromJson(line, Message.class);
            
            remotePort = socket.getPort();
            remoteAddress = socket.getRemoteSocketAddress();

            
            if (msg.getMsgType().equals("Obtain")) {
            	handleObtain(msg.getMessage());
            }
            else if (msg.getMsgType().equals("QueryHit")) {
            	handleQueryHit(gson.fromJson(msg.getMessage(),QueryHit.class));
            }
            else if (msg.getMsgType().equals("Query")) {
            	handleQuery(gson.fromJson(msg.getMessage(),Query.class));
            }
            /*while( line != null && line.length() > 0 )
            {
                out.println( "{\"msgType\":\"ack\"}" );
                out.flush();
                line = in.readLine();
            }  */      
           
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    
    
    private void handleQueryHit(QueryHit qhit) throws Exception {
    	close();
    	if (me.getFileToGet() == null)
    		return;
    	
    	if (me.getPeerId() == qhit.getMsg().getPeerID()) {
    		sendObtain(qhit);
        	return;
    	}
    	
    	peerIPClock replyTo = me.getMessages().getUpstream(qhit.getMsg());  	
    		
    	if (replyTo != null) {
    		Neighbor n = me.getNeighborIp(replyTo.peerId);
    		propagateQHit(qhit, n.ip, n.port);
    	}
    	close();
	}
    
    public void sendObtain(QueryHit qhit) throws Exception {
    	String fileName = me.getFileToGet();
    	Message m = new Message("Obtain",fileName);
    	String[] ip = qhit.getPeerIP().split(":");
    	Path f = Paths.get("./TestFiles/"+fileName);
    	//f.createNewFile();
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
    	me.setFileToGet(null);
    }
    
    private void propagateQHit (QueryHit qhit, String ip, int port) throws Exception {
    	System.out.println("QueryHit : ");
    	Gson gson = new Gson();
    	String qhitJson = gson.toJson(qhit,QueryHit.class);
    	System.out.println(qhitJson);
    	socket = new Socket(ip, port); 			
    	out = socket.getOutputStream();
		new PrintStream(out).println(gson.toJson(new Message("QueryHit",qhitJson),Message.class));
    }

	private void handleObtain(String fileName) {
    	
    	System.out.println("Obtain!! "+me.getPeerId());
    	try {
    		Path f = Paths.get("../TestFiles/"+fileName);
			//BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			byte [] barray;  //= new byte [(int)f.length()];
			//bis.read(barray,0,barray.length);
			barray = Files.readAllBytes(f);
			System.out.println("Sending file : "+ fileName);
			out.write(barray, 0, barray.length);
			//bis.close();
			close();
    	
    	} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    

	// Close our connection
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
    
    public void handleQuery(Query query) throws Exception{//int msgID, int TTL, String filename){
    	//	boolean msgFound = false;
    	
    	if(me.getFilesList().contains(query.getFileName()!=null)){//file found in filelist, query hit
    		System.out.println("File Found");
    		QueryHit qh = new QueryHit(query.getFileName(), query.getMsg(),me.getIp());
    		String[] ra = remoteAddress.toString().split(":");
        	close();
        	
    		propagateQHit(qh,ra[0], remotePort);
    	}
    	else{
	    	peerIPClock pic = me.getMessages().getUpstream(query.getMsg());
	    	if(pic!=null){//message exists in messageHashMap, don't propagate
	    		//	msgFound = true;
	    		//do nothing else
	    		System.out.println("Duplicate Message");
	    	}
	    	else {//message is new, propagate to neighbors
	    		propagateQuery(query);
	    		close();
	    		
	    	}
    	}
    	
    }
    
    public void propagateQuery(Query query) {
    	System.out.println("Message Being Sent To Neighbors");
		for(Neighbor nb: me.getNeighborsList()){
	    	Query q = new Query(query.getFileName(), query.getTTL(), new Msg(query.getMsg().getPeerID(),query.getMsg().getSeqID()));
	    	sendQuery(q,nb.ip,nb.port);
		//	query(query);
		}
    }
    
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