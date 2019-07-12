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
 public static final String ANSI_RED = "\u001B[31m";
 public static final String ANSI_GREEN = "\u001B[32m";
 public static final String ANSI_CYAN = "\u001B[36m";
 @SuppressWarnings("resource")
 public static void main (String[] args) {
     try {
   System.out.println("Usage: java main [encryption key]");
   String key = args[0];
   char[] sm = key.toCharArray();
   System.out.println("Using Key:"+" "+ANSI_RED+key+ANSI_RESET);
   String response = null;
   System.out.println(ANSI_CYAN+"Reading Database..."+ ANSI_RESET + ANSI_GREEN + "DONE !" + ANSI_RESET);
   ServerSocket serverSocket = new ServerSocket(2468);
   System.out.println(ANSI_CYAN + "Starting Server..."+ ANSI_RESET + ANSI_GREEN + "DONE !" + ANSI_RESET);
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
   System.out.println("Ready?..." + ANSI_GREEN + "DONE !" + ANSI_RESET);
   String userm = null;
   String usernm = null;
   boolean adminlog = false;
   int o = 2;
   String line = "";
   String keyp = null, keyu = null;
   String state = "";
   String user = null, pass = null, token = null, login = null;
   for (int i = 1; i < p.length; i += 2) {
    hm.put(p[i - 1], p[o - 1]);
    o += 2;
   }
   while (true) {
	   String clientSentence = dis.readUTF();
	   clientSentence = AES.decrypt(clientSentence,key);
	   String help = new String(Files.readAllBytes(Paths.get("help.txt")));
	   String dc0 = AES.encrypt(help,key);
	   outToClient.writeUTF("SCPNULCHAR");
	   if (clientSentence.equals("help")) {
		   outToClient.writeUTF(dc0);
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
	}}
    if (clientSentence.contains("pass")) {
     String kk = clientSentence.substring(5);
     if (kk.equals(keyp)) {
	      response = "PASS OK !,Welcome "+keyu;
      String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
      pass = "passtrue";
      state = "true";
	  usernm = keyu;
     } else {
      response = "PASSWORD IS WRONG";
      String dc = AES.encrypt(response,key);
      outToClient.writeUTF(dc);
	}}
    System.out.println("Recieved Traffic Successfully from:" + (s.getInetAddress()) + ":" + (s.getPort()));
    if (state.equals("true") && clientSentence.equals("mail")) {
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
        ewe.append(usernm+" -> "+message+" - "+formatter.format(date));
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
    if (adminhm.containsKey(clientSentence)) {
     userm = clientSentence;
	response = "Admin User OK";
     String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
    } else if (clientSentence.equals(adminhm.get(userm))) {
     adminlog = true;
     response = "Admin Password OK \n Welcome Administrator.";
	String dc = AES.encrypt(response,key);
     outToClient.writeUTF(dc);
    }
    if (adminlog) {
     login = "true";
    }
    if (clientSentence.equals(null) || clientSentence.equals("")) {
		String dc = AES.encrypt("Packet sent,But no content found !",key);
    	outToClient.writeUTF(dc);
    }
    if (clientSentence.contains("createuser") && login == "true") {
     outToClient.writeUTF(AES.encrypt("DONE !",key));
     String in = clientSentence.substring(11);
     BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt", true));
     writer.write( in );
     String[] pp = in .split(":");
     hm.put(pp[0], pp[1]);
     writer.close();
    } else {
    outToClient.writeUTF("SCPNULCHAR");
   }}
}catch (Exception w) {
    w.printStackTrace();
	re();
    }}
public static void re() {
 main(null);
}}
