package Server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;

import com.google.gson.Gson;

import Messages.*;

class RequestHandler implements Runnable
{
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    
    RequestHandler( Socket socket )
    {
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
    
    
    
    private void queryHit(QueryHit fromJson) {
    	System.out.println("QueryHit");
    	close();
	}

	private void obtain(String fileName) {
    	
    	
    	try {
    		File f = new File(Paths.get("/Users/apple/Documents/cs550/pa2/Gnutella/TestFiles/"+fileName).toString());
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			byte [] barray  = new byte [(int)f.length()];
			bis.read(barray,0,barray.length);
			System.out.println("Sending file : "+ fileName);
			out.write(barray, 0, barray.length);
			bis.close();
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