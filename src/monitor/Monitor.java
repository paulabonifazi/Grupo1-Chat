package monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Monitor extends Thread {

	private static Monitor instance = null;
	private static ServerSocket serverSocket;
	
	private static int puertoMonitor = 11147;
	private static int principal = 11001;
	private static int nroSig = 0;
	
	private ArrayList<Socket> listaSockets = new ArrayList<Socket>();
	private ArrayList<Socket> listaSocketsCaidos = new ArrayList<Socket>();
	private Socket socketPrincipal = null;
	
	private HeartBeatMonitor heartBeat;
	
	private Monitor() {

		try {
			this.serverSocket = new ServerSocket(this.puertoMonitor);
			this.heartBeat = new HeartBeatMonitor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Monitor getInstance() {

		if (instance == null)
			instance = new Monitor();

		return instance;
	}

	public void agregarSocket(Socket s) throws IOException {

		DataOutputStream dos = new DataOutputStream(s.getOutputStream());

		// dos.writeUTF("PUERTO");
		dos.writeUTF(Integer.toString(this.principal + this.nroSig++)); // PUERTO
		// dos.writeUTF("IP");
		//System.out.println("Puerto local: "+this.principal);
		dos.writeUTF("localhost"); // IP server principal
		//System.out.println("Socket server principal"+s.getLocalPort());
		
		dos.writeUTF(Integer.toString(this.principal)); // PUERTO server principal
		//System.out.println("Principal: "+this.principal);

		if (this.socketPrincipal == null) {
			
			this.heartBeat.start();
			this.socketPrincipal = s;
			dos.writeUTF("PRINCIPAL");
		}else
			dos.writeUTF("SECUNDARIO");
		this.listaSockets.add(s);
		System.out.println(this.listaSockets);
		//System.out.println("Socket server principal"+this.socketPrincipal.getPort());
	}

	public void cambiaServerPrincipal() throws IOException {

		if (this.listaSockets.size() > 0) {
			this.socketPrincipal = this.listaSockets.get(0);
			this.listaSocketsCaidos.add(socketPrincipal);		// preservamos el socket caido para recuperarlo mas tarde
			this.listaSockets.remove(0);
			DataOutputStream dos = new DataOutputStream(this.socketPrincipal.getOutputStream());
			dos.writeUTF("PRINCIPAL");
		}
	}

	public void conecta_a_Principal() throws IOException {
		DataOutputStream dos ;
		
		for(int i=0; this.listaSockets.size()>0 ;i++ ) {
			dos = new DataOutputStream(this.listaSockets.get(i).getOutputStream());
			dos.writeUTF("NUEVO_PUERTO");
			dos.writeUTF("localhost"); // IP server principal
			dos.writeUTF(Integer.toString(this.socketPrincipal.getLocalPort())); // PUERTO server principal
		}	
	}
	
	public void run() {

		String comando = "";
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
}
