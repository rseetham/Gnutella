package Server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleSocketServer
{
    private static ServerSocket serverSocket;
    private static int port;
    private static Peer me;
   

    public static void runServer()
    {
        while( true )
        {
            try
            {
                System.out.println( "Listening for a connection" );
                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket );
                new Thread(requestHandler).start();
                //requestHandler.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
 	 * Registers all the files in the TestFiles dir of the client
 	 */
 	static void setUpFiles(){
     	File[] files = new File(Paths.get("./TestFiles/").toString()).listFiles();
 		for (File file : files) {
 		    if (file.isFile()) {
 		        try {
 					me.addFile(file.getName());
 				} catch (Exception e) {
 					e.printStackTrace();
 				}
 		    }
 		}
     }
    
    public static void main( String[] args )
    {
        if( args.length == 0 )
        {
            System.out.println( "Usage: SimpleSocketServer <port> <peerid>" );
            System.exit( 0 );
        }
        port = Integer.parseInt( args[ 0 ] );
        
        int peerid = Integer.parseInt(args[ 1 ]);
        System.out.println( "Start server on port: " + port );

        me = new Peer(peerid,"127.0.0.1",port);
        
        setUpFiles();
        
        
        
        try {
        	
			serverSocket = new ServerSocket( port );
			runServer();

        } catch (IOException e) {
			e.printStackTrace();
		}
    }
}
