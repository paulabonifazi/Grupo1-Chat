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

	private static int puertoMonitorServidores = 11304;
	private static int puertoMonitorClientes = 11222;

	private static int principal = 11001;
	private static int nroSig = 0;

	private ArrayList<Socket> listaSocketsServidores = new ArrayList<Socket>();
	private ArrayList<Socket> listaSocketsCaidos = new ArrayList<Socket>();
	private Socket socketPrincipal = null, socketSecundario = null;

	private ArrayList<Socket> listaSocketsClientes = new ArrayList<Socket>();
	// private HeartBeatMonitor heartBeat;

	private boolean terminar = false;
	private int tiempo = 5;
	private int intentos = 0;
	private int condicion = 0;

	private Monitor() {

		try {
			this.serverSocketServidores = new ServerSocket(this.puertoMonitorServidores);
			this.serverSocketClientes = new ServerSocket(this.puertoMonitorClientes);
			conectarConClientes();
			// this.heartBeat = new HeartBeatMonitor();
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
			recibirHeartBeat();
			// this.heartBeat.start();

		} else {
			this.listaSocketsServidores.add(s);
			// this.socketSecundario = s;
			dos.writeUTF("SECUNDARIO");
		}

	}

	public void cambiaServerPrincipal() throws IOException {

		try {
			if (this.condicion > 0) {
				DataOutputStream dos;
				this.listaSocketsCaidos.add(socketPrincipal); // preservamos el socket caido para recuperarlo mas tarde
				this.socketPrincipal = this.listaSocketsCaidos.get(0);
				this.listaSocketsCaidos.remove(0);
				condicion--;
				dos = new DataOutputStream(this.socketPrincipal.getOutputStream());
				System.out.println("Cambia a server" + this.socketPrincipal.getLocalPort());
				
				dos.writeUTF("PRINCIPAL");
				Thread.sleep(2000);
				this.conecta_a_Principal();
				System.out.println(this.listaSocketsClientes);
				for (int i = 0; i < this.listaSocketsClientes.size(); i++) {
					dos = new DataOutputStream(this.listaSocketsClientes.get(i).getOutputStream());
					dos.writeUTF("CAMBIAR_SERVER");
					System.out.println("Envia CAMBIAR_SERVER");
				}
			} else if (this.listaSocketsServidores.size() > 0) {
				DataOutputStream dos;
				this.listaSocketsCaidos.add(socketPrincipal); // preservamos el socket caido para recuperarlo mas tarde
				condicion++;
				this.socketPrincipal = this.listaSocketsServidores.get(0);
				this.listaSocketsServidores.remove(0);
				dos = new DataOutputStream(this.socketPrincipal.getOutputStream());
				System.out.println("Cambia a server" + this.socketPrincipal.getLocalPort());
				dos.writeUTF("PRINCIPAL");
				Thread.sleep(2000);
				this.conecta_a_Principal();
				System.out.println(this.listaSocketsClientes);
				for (int i = 0; i < this.listaSocketsClientes.size(); i++) {
					dos = new DataOutputStream(this.listaSocketsClientes.get(i).getOutputStream());
					dos.writeUTF("CAMBIAR_SERVER");
					System.out.println("Envia CAMBIAR_SERVER");
				}

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
		for (int i = 0; i < this.listaSocketsCaidos.size(); i++) {
			dos = new DataOutputStream(this.listaSocketsCaidos.get(i).getOutputStream());
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

	public void conectarConClientes() {
		new Thread(() -> {
			try {
				// ServerSocket serverCliente = new ServerSocket(this.puertoMonitorClientes);
				while (!serverSocketClientes.isClosed()) {
					Socket socket = serverSocketClientes.accept();
					System.out.println("Conecta con cliente");
					this.listaSocketsClientes.add(socket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void recibirHeartBeat() {
		new Thread(() -> {
			String comando = null;
			DataInputStream dis = null;

			System.out.println("Monitor");
			while (this.terminar == false) {

				try {
					// this.socket = Monitor.getInstance().getSocketPrincipal();
					dis = new DataInputStream(this.socketPrincipal.getInputStream());
					if (dis.available() > 0) {
						comando = dis.readUTF();
						System.out.println("Monitor recibio: " + comando);
					}
				} catch (IOException e) {

					e.printStackTrace();
				}

				if (comando!=null) {
					
					if (comando.equals("HEARTBEAT")) {
						// ESPERAMOS
						comando = "No llego heartbeat";
						try {
							Thread.sleep(tiempo * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("Monitor: " + comando);
						if (this.intentos == 0) { // CAMBIAMOS DE SERVER PRINCIPAL
							
							try {
								cambiaServerPrincipal();
								Thread.sleep(tiempo * 1000);
							} catch (IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) { // ESPERAMOS PARA DAR TIEMPO A QUE SE CAMBIE
								e.printStackTrace();
							}
							
						} else {
							this.intentos--;
							try {
								Thread.sleep(tiempo * 1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
				}

			}
		}).start();
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

}
