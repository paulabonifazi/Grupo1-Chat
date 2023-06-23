package monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import back.Conexion;
import server.DataCliente;

public class Monitor extends Thread {

	private static int NUMERO_PUERTO_SIGUIENTE_SERVER = 100;
	private static Monitor instance = null;
	private int puertoMonitor = 200;
	// private InfoServer serverPrincipal = null;
	private Socket socketPrincipal = null;
	// private ArrayList<InfoServer> listaServers = new ArrayList<InfoServer>();
	private ArrayList<Socket> listaSockets = new ArrayList<Socket>();
	private ServerSocket serverSocket;

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

	public void agregarSocket(Socket s) throws IOException {
		if (this.socketPrincipal == null) {
			this.socketPrincipal = s;
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF("PRINCIPAL");
		}
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

}
