package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class RequestHandler implements Runnable
{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
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
            out = new PrintWriter( socket.getOutputStream() );


            // Echo lines back to the client until the client closes the connection or we receive an empty line
            String line = in.readLine();
            while( line != null && line.length() > 0 )
            {
                out.println( "Echo: " + line );
                out.flush();
                line = in.readLine();
            }        
           
        }
        catch( Exception e )
        {
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