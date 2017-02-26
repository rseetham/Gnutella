package Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.*;

import com.google.gson.Gson;

import Messages.*;

public class SimpleSocketClientExample
{
	static Socket socket;
	static PrintStream out;
	static BufferedReader in;
	static int peerId;
	static String server;
	static int port;
	static int seqid = 0;
	
    public static void main( String[] args )
    {
        if( args.length < 3 )
        {
            System.out.println( "Usage: SimpleSocketClientExample <peerID> <server> <TTL> <port> " );
            System.exit( 0 );
        }
        peerId = Integer.parseInt(args[0]);
        server = args[ 1 ];
        port = (args.length < 3) ? peerId : Integer.parseInt(args[ 2 ]);

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
            
            String qhit = new Gson().toJson(new QueryHit("test.txt",new Msg(peerId,100),"127.0.0.1:1060"));
            out.println(qhit);
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
    
    void sendQuery(String fileName, int ttl) throws Exception {
    	Gson gson = new Gson();
    	Query q = new Query(fileName, ttl, new Msg(peerId,seqid));
    	Message m = new Message("Query",gson.toJson(q));
    	socket = new Socket( server, port );
    	out = new PrintStream( socket.getOutputStream() );
    	out.println(gson.toJson(m));
    	socket.close();
    	out.close();
    }
    
    void rcvQHitSendObtain(QueryHit qhit, String fileName) throws NumberFormatException, UnknownHostException, IOException {
    	Gson gson = new Gson();
    	Message m = new Message("Obtain",fileName);
    	String[] ip = qhit.getPeerIP().split(":");
    	Path f = Paths.get("./TestFiles/"+fileName);
    	//f.createNewFile();
    	BufferedWriter writer = Files.newBufferedWriter(f, Charset.forName("US-ASCII"));
    	socket = new Socket( ip[0], Integer.parseInt(ip[1]) );
    	out = new PrintStream( socket.getOutputStream() );
    	out.println(gson.toJson(m));
    	in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
    	// TODO write to file!!!
    	String line = in.readLine();
    	while (line != null && line.length() > 0){
    		writer.write(line, 0, line.length());
    	}
    	
    	writer.close();
    	socket.close();
    	in.close();
    }
    
    void close() throws Exception{
    	socket.close();
    	in.close();
    	out.close();
    }
}
