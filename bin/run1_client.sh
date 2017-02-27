java -classpath ../gson-2.6.2.jar:. Server.SimpleSocketServer 1024 $1 10 1 >> loout_1024&
for arg in 1025 1026 1027 1028 1029 1030 1031 1032 1033
do
	 java -classpath ../gson-2.6.2.jar:. Server.SimpleSocketServer $arg $1 10 >> logout_$arg&
done
wait
echo "Done"