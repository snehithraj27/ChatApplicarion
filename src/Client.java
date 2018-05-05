
/* 										
									Client Class
 * */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

// Client class to initialize the GUI
public class Client {
	BufferedReader in;
	PrintWriter out;
	JFrame frame = new JFrame("Client");
	JTextField textField = new JTextField(40);
	JTextArea messageArea = new JTextArea(15, 50);

	Map<String, String> clients = new HashMap<String, String>(); // HashMap to store the Clients registered with the
																	// Server

	public Client() { // Constructor to initialize the Client
		textField.setEditable(false);
		messageArea.setEditable(false);
		frame.getContentPane().add(textField, "North");
		frame.getContentPane().add(new JScrollPane(messageArea), "Center");
		frame.pack();
	}

	// Method for dialog box to enter the IP address of the Server. Enter
	// "127.0.0.1" as the IP address
	private String getServerAddress() {
		return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to the ChatRoomSystem",
				JOptionPane.QUESTION_MESSAGE);
	}

	// Method for dialog box to register the name of the Client
	private String getName() {
		return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}

	// Method to connect to server and start processing messages between client and
	// server
	private void run() throws IOException, ParseException {
		String serverAddress = getServerAddress(); // getting the IP address from getServerAddress method
		Socket socket = new Socket(serverAddress, 9000); // Initialize a new socket connection
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // instance of Buffer Reader to accept
																					// messeges from the server
		out = new PrintWriter(socket.getOutputStream(), true); // instance of PrintWriter to send messages to the server
		DateTimeFormatter dtfd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String tDate = dtfd.format(LocalDateTime.now());

		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // get text from the text area
				out.println(textField.getText() + ",POST /localhost/server http/1.1,Host:" + serverAddress
						+ ",User-Agent:Mozilla/5.0,Content-Type:text,Content-Length:"
						+ Integer.toString(textField.getText().length()) + ",Date:" + tDate);
				textField.setText("");
			}
		});
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String pTime, cTime, hTime, mTime, sTime;
		String sName = null;
		// Process all messages from server, according to the protocol.
		try {
			while (true) {

				String uTime = "0:0:0";
				String line = in.readLine();
				if (line.startsWith("SUBMITNAME")) {
					out.println(getName()); // Send the desired screen name to the server for acceptance
				}
				// block of code is executed if name is accepted by the server
				else if (line.startsWith("NAMEACCEPTED")) {
					textField.setEditable(true);
					sName = line.split(",")[1];
					messageArea.append("You are " + sName + "\nStart sending messages\n");
				} else if (line.startsWith("duplicate")) {
					JOptionPane.showMessageDialog(null, "Name already exist.Please choose another name");
				} else if (line.contains("disconnected")) {
					messageArea.append(line + "\n"); // notifying if any of the clients is disconnected
				} else if (line.contains("connected")) {
					messageArea.append(line + "\n"); // notifying if any of the clients is disconnected
				}
				// block of code is executed if message is received from another client
				else if (line.startsWith("MESSAGE")) {
					String[] msg = line.split(",");
					// block of code to calculate the time interval
					if (clients.containsKey(msg[1])) {
						pTime = clients.get(msg[1]); // getting past time of the sending client
						cTime = dtf.format(LocalDateTime.now());
						DateTime dt1 = new DateTime(format.parse(pTime));
						DateTime dt2 = new DateTime(format.parse(cTime));
						hTime = Integer.toString(Hours.hoursBetween(dt1, dt2).getHours() % 24);
						mTime = Integer.toString(Minutes.minutesBetween(dt1, dt2).getMinutes() % 60);
						sTime = Integer.toString(Seconds.secondsBetween(dt1, dt2).getSeconds() % 60);
						uTime = hTime + ":" + mTime + ":" + sTime;
						clients.put(msg[1], cTime);
						messageArea.append(msg[1] + " (" + uTime + ") - " + msg[2] + "\n");
					}
					// if client sends first message add time to the clients HashMap
					else {
						clients.put(msg[1], dtf.format(LocalDateTime.now()));
						messageArea.append(msg[1] + " (" + uTime + ") - " + msg[2] + "\n");
					}
				}
			}
		} catch (Exception e1) {
			socket.close();
			messageArea.append("Server is offline");
		}
	}

	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		client.run();
	}
}