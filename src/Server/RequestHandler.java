package Server;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
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
            
            System.out.println(line);
            
            Message msg = gson.fromJson(line, Message.class);
            
            if (msg.getMsgType().equals("Obtain")) {
            	obtain(msg.getMessage());
            }
            else if (msg.getMsgType().equals("QueryHit")) {
            	queryHit(gson.fromJson(msg.getMessage(),QueryHit.class));
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
    
    
    
    private void queryHit(QueryHit qhit) throws IOException {
    	close();
    	
    	
    	if (me.getPeerId() == qhit.getMsg().getPeerID()) {
    		setNewConn(me.getClientPort());
    		propagateQHit(qhit);
    	}
    	
    	peerIPClock replyTo = me.getMessages().getUpstream(qhit.getMsg());  	
    		
    	if (replyTo != null) {
    		setNewConn(replyTo.peerId);
    		propagateQHit(qhit);
    	}
    	
    	close();
	}
    
    private void propagateQHit (QueryHit qhit) {
    	Gson gson = new Gson();
    	String qhitJson = gson.toJson(qhit,QueryHit.class);
    	System.out.println(qhitJson);
		new PrintStream(out).println(gson.toJson(new Message("QueryHit",qhitJson),Message.class));
    }
    
    private void setNewConn(int peerId) throws UnknownHostException, IOException {
    	Neighbor n = me.getNeighborIp(peerId);
		socket = new Socket( n.ip, n.port );
		out = new PrintStream( socket.getOutputStream() );
    }

	private void obtain(String fileName) {
    	
    	
    	try {
    		Path f = Paths.get("./TestFiles/"+fileName);
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
}