package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSocketServer
{
    private static ServerSocket serverSocket;
    private static int port;

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
                new Thread(requestHandler)
                requestHandler.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main( String[] args )
    {
        if( args.length == 0 )
        {
            System.out.println( "Usage: SimpleSocketServer <port>" );
            System.exit( 0 );
        }
        port = Integer.parseInt( args[ 0 ] );
        System.out.println( "Start server on port: " + port );

        try {
			serverSocket = new ServerSocket( port );
			runServer();

        } catch (IOException e) {
			e.printStackTrace();
		}
    }
}
