package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SimpleSocketClientExample
{
    public static void main( String[] args )
    {
        if( args.length < 2 )
        {
            System.out.println( "Usage: SimpleSocketClientExample <server> <path>" );
            System.exit( 0 );
        }
        String server = args[ 0 ];
        int port = Integer.parseInt(args[ 1 ]);

        System.out.println( "Loading contents of URL: " + server );

        try
        {
            // Connect to the server
            Socket socket = new Socket( server, port );

            // Create input and output streams to read from and write to the server
            PrintStream out = new PrintStream( socket.getOutputStream() );
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            
            String file = "test.txt";
            String obtain = "{\"msgType\":\"Obtain\",\"message\":\"" + file + "\"}";
            
            out.println(obtain);

            // Read data from the server until we finish reading the document
            String line = in.readLine();
            while( line != null )
            {
                System.out.println( line );
                line = in.readLine();
            }
            
            System.out.println("closing client");
            // Close our streams
            in.close();
            out.close();
            socket.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
