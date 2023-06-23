package monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IniciaMonitor {
	private static int puerto=11132;
	private static ServerSocket serverSocket;
	private static int principal = 0;
	private static int nroSig=0;
	
	public static void main(String args[]) {
		try {
			serverSocket = new ServerSocket(puerto);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			
			try {
				Socket socket = serverSocket.accept();
				
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream (socket.getOutputStream());
				
				if (principal == 0) {
					String num = Integer.toString(4444);
					dos.writeUTF(num);
					principal = 1;
					System.out.println("principal: "+principal);
				}
				else {
					String num = Integer.toString(4445+nroSig);
					dos.writeUTF(num);
					System.out.println("puerto enviado secundario: "+4445+nroSig);
					nroSig++;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
