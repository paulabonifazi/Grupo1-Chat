package monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Monitor extends Thread {

	private static Monitor instance = null;
	private static ServerSocket serverSocketServidores;
	private static ServerSocket serverSocketClientes;
	
	private static int puertoMonitorServidores = 10001;
	private static int puertoMonitorClientes = 11201;
	
	private static int principal = 11001;
	private static int nroSig = 0;

	private ArrayList<Socket> listaSocketsServidores = new ArrayList<Socket>();
	private ArrayList<Socket> listaSocketsCaidos = new ArrayList<Socket>();
	private Socket socketPrincipal = null, socketSecundario = null;
	
	private ArrayList<Socket> listaSocketsClientes = new ArrayList<Socket>();
	private HeartBeatMonitor heartBeat;

	private Monitor() {

		try {
			this.serverSocketServidores = new ServerSocket(this.puertoMonitorServidores);
			//this.serverSocketClientes = new ServerSocket(this.puertoMonitorClientes);
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

		dos.writeUTF(Integer.toString(this.principal + this.nroSig++)); // PUERTO
		dos.writeUTF("localhost"); // IP server principal
		dos.writeUTF(Integer.toString(this.principal)); // PUERTO server principal

		if (this.socketPrincipal == null) {

			this.socketPrincipal = s;
			dos.writeUTF("PRINCIPAL");

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.heartBeat.start();

		} else {
			this.listaSocketsServidores.add(s);
			// this.socketSecundario = s;
			dos.writeUTF("SECUNDARIO");
		}

	}

	public void cambiaServerPrincipal() throws IOException {

		try {
			if (this.listaSocketsServidores.size() > 0) {
				this.listaSocketsCaidos.add(socketPrincipal); // preservamos el socket caido para recuperarlo mas tarde
				this.socketPrincipal = this.listaSocketsServidores.get(0);
				this.listaSocketsServidores.remove(0);
				DataOutputStream dos = new DataOutputStream(this.socketPrincipal.getOutputStream());
				System.out.println("Cambia a server 2" + this.socketPrincipal.getLocalPort());
				dos.writeUTF("PRINCIPAL");
				Thread.sleep(2000);
				this.conecta_a_Principal();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void conecta_a_Principal() throws IOException {
		DataOutputStream dos;

		for (int i = 0; i < this.listaSocketsServidores.size(); i++) {
			dos = new DataOutputStream(this.listaSocketsServidores.get(i).getOutputStream());
			dos.writeUTF("NUEVO_PUERTO");
			dos.writeUTF("localhost"); // IP server principal
			dos.writeUTF(Integer.toString(this.socketPrincipal.getLocalPort())); // PUERTO server principal
		}

//		dos = new DataOutputStream(this.socketSecundario.getOutputStream());
//		dos.writeUTF("NUEVO_PUERTO");
//		dos.writeUTF("localhost"); // IP server principal
//		dos.writeUTF(Integer.toString(this.socketPrincipal.getLocalPort())); // PUERTO server principal
//		
	}

	public void run() {

		String comando = "";
		super.run();
		Socket socketServer;
		Socket socketCliente;

		while (true) {

			try {
				socketServer = this.serverSocketServidores.accept();
				this.agregarSocket(socketServer);
//				socketCliente = this.serverSocketClientes.accept();
//				this.listaSocketsClientes.add(socketCliente);
				
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	public int getPuertoMonitor() {
		return puertoMonitorServidores;
	}

	public Socket getSocketPrincipal() {
		return socketPrincipal;
	}

	public ArrayList<Socket> getListaSockets() {
		return listaSocketsServidores;
	}

	public ServerSocket getServerSocket() {
		return serverSocketServidores;
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
