package Server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import com.google.gson.Gson;

public class SimpleSocketServer
{
    private static ServerSocket serverSocket;
    private static int port;
    private static Peer me;
    private static boolean doQ;
   

    /**
     * establish socket connection
     */
    public static void runServer()
    {
    	if (doQ) {
    		Test test = new Test(me);
    		new Thread(test).start();
    	}
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
 		System.out.println("SetUpFiles");
     	File[] files = new File(Paths.get("../TestFiles/").toString()).listFiles();
 		for (File file : files) {
 		    if (file.isFile()) {	
 		        try {
 					me.addFile(file.getName());
 				} catch (Exception e) {
 					e.printStackTrace();
 				}
 		    }
 		}
 		System.out.println(me.toString());
     }
 	
 	/** read the peer IDs and their lists of neighbors from json file and populate the data structures to set up the network
 	 * @param net
 	 * @throws Exception
 	 */
 	static void setUpNetwork(String net) throws Exception {
 		System.out.println("setUpNetwork : "+ net);
 		List<String> lines = Files.readAllLines(Paths.get("../"+net));
 		System.out.println(lines);
 		Gson gson = new Gson();
 		Network n;
 		for(String line:lines){
 			n = gson.fromJson(line, Network.class);
 			System.out.println(n);
 			if (n.peerid == me.getPeerId()) {
 				String[] nids = n.Neighbors.split(",");
 				for (String neighbor : nids) {
 					int nbr = Integer.parseInt(neighbor);
 					me.addNeighbor(nbr, "127.0.0.1", nbr);
 				}
 				break;
 			}	
 		}
 		System.out.println(me.toString());
 	}
 	
 	/**
 	 * create test files
 	 */
 	public static void setUpTestFiles() {
 		int n = me.getPeerId();
 		IntStream.range(0, 10).forEach(i -> me.addFile("text"+n*10+i+".txt"));
 	}
    
    public static void main( String[] args ) throws IOException
    {
        if( args.length < 3 )
        {
            System.out.println( "Usage: SimpleSocketServer <port> <network.json> <ttl> <query?> <peerid>" );
            System.exit( 0 );
        }
        port = Integer.parseInt( args[ 0 ] );
        
        String network = args[ 1 ];
        
        int ttl = Integer.parseInt( args[ 2 ] );
        
        doQ =  (args.length < 4) ? false : true;
        
        int peerid = (args.length < 5) ? port : Integer.parseInt(args[ 4 ]);
        System.out.println( "Start server on port: " + port );

        me = new Peer(peerid,"127.0.0.1",port,ttl);
        
        try {
        	//setUpFiles(); 
        	setUpTestFiles();
        	setUpNetwork(network);
        
			serverSocket = new ServerSocket( port );
			runServer();

        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    class Network {

		private int peerid;
    	private String Neighbors;
		
    	public Network(int peerid, String Neighbors) {
			this.peerid = peerid;
			this.Neighbors = Neighbors;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Network [peerId=" + peerid + ", neighbors=" + Neighbors + "]";
		}
    	
    }
}
