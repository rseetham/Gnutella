java -classpath ../gson-2.6.2.jar:. Server.SimpleSocketServer 1024 $1 3 1 &
for arg in 1025 1026
do
	 java -classpath ../gson-2.6.2.jar:. Server.SimpleSocketServer $arg $1 3 1 &
done