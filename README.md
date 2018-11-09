# ChatApplication-Socket-Programming-Java

Description:
  A chat room system consisting of a server and four client processes.
  Each client process will connect to the server over a socket connection and register a user name at the server.
  The server displays the names of the connected clients.
  Messages typed at one client are sent to the server which will relay it to the other connected clients.
  When a client logs onto the server, every client in the system instantiates a timer to track the interval between
  messages received from that client.
  
  For example: clients A, B, C, and D are connected to a server. 
  If client A send a message “Hello” at time 1:00, then sends message “All you” at time 1:04, 
  and message “listeners” at time 1:05, the output at clients B, C, and D will read something like:
  A (0:00) – Hello
  A (0:04) – All you 
  A (0:01) – listeners
  
  If a client logs off the other clients should be notified. The server displays the messages that come through.
  
 Steps of Execution:
   1.	Run the server code. Server UI will be displayed with pre-fetched information of previous session and 
      server starts running.
   2.	Run the client code.
   3.	When prompted, enter the IP Address: 127.0.0.1(localhost)
   4.	Enter the preferred screen name.( Screen name should be unique )
   5.	Apply the same procedure for running multiple clients.
   6.	The client window now opens. Enter the message in the chat box.
   7.	The message is broadcasted to all the connected clients.
   8.	Client UI will display the message with time interval.
   9.	The Server UI will display the messages as http format.
   10. If you want to quit close the client window and all the connected clients will be notified.
   11. The clients will also be notified when Server is offline.
   12. Implemented multithread Server and Database for the server.

