package monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Monitor extends Thread{
	
	private static Monitor instance = null;
	private static int puertoMonitor=11132;
	private static ServerSocket serverSocket;
	private static int principal = 100;
	private static int nroSig=0;
	private ArrayList<Socket> listaSockets = new ArrayList<Socket>();
	private Socket socketPrincipal = null;
	
	private Monitor() {

		try {
			this.serverSocket = new ServerSocket(this.puertoMonitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Monitor getInstance() {

		if (instance == null)
			instance = new Monitor();

		return instance;
	}
	
//	public static void main(String args[]) {
//		try {
//			serverSocket = new ServerSocket(puertoMonitor);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		while (true) {
//			
//			try {
//				Socket socket = serverSocket.accept();
//				
//				DataInputStream dis = new DataInputStream(socket.getInputStream());
//				DataOutputStream dos = new DataOutputStream (socket.getOutputStream());
//				
//				if (principal == 0) {
//					String num = Integer.toString(4444);
//					dos.writeUTF(num);
//					principal = 1;
//					System.out.println("principal: "+principal);
//				}
//				else {
//					String num = Integer.toString(4445+nroSig);
//					dos.writeUTF(num);
//					System.out.println("puerto enviado secundario: "+4445+nroSig);
//					nroSig++;
//				}
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	public void agregarSocket(Socket s) throws IOException {
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		if (this.socketPrincipal == null) {
			this.socketPrincipal = s;
			//DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF("PRINCIPAL");
		}
		dos.writeUTF("PUERTO");
		dos.writeUTF(Integer.toString(this.principal+this.nroSig));
		dos.writeUTF("IP");
		dos.writeUTF("");
		this.listaSockets.add(s);
	}

	public void cambiaServerPrincipal() {
		
		if(this.listaSockets.size() > 0) {
			this.socketPrincipal = this.listaSockets.get(0);
		}
	}

	public void run() {

		String comando = null;
		super.run();
		Socket s;

		while (true) {

			try {
				s = this.serverSocket.accept();
				this.agregarSocket(s);

			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	public int getPuertoMonitor() {
		return puertoMonitor;
	}

	public Socket getSocketPrincipal() {
		return socketPrincipal;
	}

	public ArrayList<Socket> getListaSockets() {
		return listaSockets;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	
//	public void run() {
//		super.run();
//		while (true) {
//			try {
//				Socket socket = serverSocket.accept();
//				
//				DataInputStream dis = new DataInputStream(socket.getInputStream());
//				DataOutputStream dos = new DataOutputStream (socket.getOutputStream());
//				
//				if (this.principal == 0) {
//					dos.write(4444);
//				}
//				else {
//					dos.write(4445+nroSig);
//					nroSig++;
//				}
//				
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
}
