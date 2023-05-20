package prueba;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import back.Cliente;
import server.Server;

public class Prueba {

	public static void main(String[] args) {
	//Server server = new Server();
		
		
		String IP = "localhost";
		int puerto = 1411;
		Socket s;
		Socket s2;
		
		Cliente cliente = new Cliente("Juancho",123,"1321");
		ObjectOutputStream dos = null;
		ObjectOutputStream dos2;
		
		String name = "tuvieja";
		String name2 = "mivieja";
		
		try {
			s = new Socket(IP,puerto);
			
//			DataInputStream dis = new DataInputStream(s.getInputStream());
//	        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			
			
	        //ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
            dos = new ObjectOutputStream(s.getOutputStream());
            
            
            dos.writeObject(name);
	        //dos.writeUTF("tuvieja");
	     
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		 
		 
		 
			try {
				s2 = new Socket(IP,puerto);
				
//				DataInputStream dis = new DataInputStream(s.getInputStream());
//		        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				
				
		        //ObjectInputStream dis = new ObjectInputStream(s.getInputStream());
	            dos2 = new ObjectOutputStream(s2.getOutputStream());
	            
	            
	            dos2.writeObject(name2);
		        //dos.writeUTF("tuvieja");
		     
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				//System.out.println("Exception");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}

//	        try {
//				dos.writeObject(name2);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

	}

}
