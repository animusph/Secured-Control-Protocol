import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
public class client {
 
    public static void main(String[] args) {
 
        try {
		String key = args[1];
		System.out.println("Using Key:"+key);
		char[] sm = key.toCharArray();
        	String host = args[0];
            Socket s = new Socket(host, 2468);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String serverMsg, clientMsg;
             do{
		System.out.println("You ->");
                clientMsg = br.readLine();
		String tbs = AES.encrypt(clientMsg,key);
                dos.writeUTF(tbs);
                dos.flush();
                serverMsg = dis.readUTF();
		while (serverMsg.contains("SCPNULCHAR") || serverMsg.equals("SCPNULCHAR")) {
			serverMsg = dis.readUTF();
		}
		serverMsg = AES.decrypt(serverMsg,key);
		System.out.println(serverMsg);
            }
            while(true);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
}
}
}