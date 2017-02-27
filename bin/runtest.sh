for arg in 1024 1025 1026
do
	 java -classpath ../gson-2.6.2.jar:. Server.SimpleSocketServer $arg testnet.json 3 >> logout_$arg &
done