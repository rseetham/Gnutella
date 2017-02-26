package Server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

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
                RequestHandler requestHandler = new RequestHandler(socket,me);
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
 	
 	static void setUpNetwork(String net) throws IOException {
 		List<String> lines = Files.readAllLines(Paths.get("../"+net), Charset.forName("UTF-8"));
 		Gson gson = new Gson();
 		Network n;
 		for(String line:lines){
 			n = gson.fromJson(line, Network.class);
 			if (n.getPeerId() == me.getPeerId()) {
 				for (int neighbor : n.getNeighbors())
 					me.addNeighbor(neighbor, "127.0.0.1", neighbor);
 				break;
 			}
 			
 		}
 	}
    
    public static void main( String[] args )
    {
        if( args.length < 2 )
        {
            System.out.println( "Usage: SimpleSocketServer <port> <network.json> <peerid>" );
            System.exit( 0 );
        }
        port = Integer.parseInt( args[ 0 ] );
        
        String network = args[ 1 ];
        
        int peerid = (args.length < 3) ? port : Integer.parseInt(args[ 2 ]);
        System.out.println( "Start server on port: " + port );

        me = new Peer(peerid,"127.0.0.1",port);
        
        setUpFiles();
        setUpNetwork(network);
        
        
        try {
        	
			serverSocket = new ServerSocket( port );
			runServer();

        } catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    class Network {
    	private int peerId;
    	private int [] neighbors;
		
    	public Network(int peerId, int[] neighbors) {
			this.peerId = peerId;
			this.neighbors = neighbors;
		}

		/**
		 * @return the peerId
		 */
		public int getPeerId() {
			return peerId;
		}

		/**
		 * @param peerId the peerId to set
		 */
		public void setPeerId(int peerId) {
			this.peerId = peerId;
		}

		/**
		 * @return the neighbors
		 */
		public int[] getNeighbors() {
			return neighbors;
		}

		/**
		 * @param neighbors the neighbors to set
		 */
		public void setNeighbors(int[] neighbors) {
			this.neighbors = neighbors;
		}
    	
    	
    }
}
