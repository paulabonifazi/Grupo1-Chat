package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Sincronizacion extends Thread {

	private Server server;

	private ServerSocket ss;
	private Socket socketMonitor;
	private Socket socketConPrincipal;
	private ArrayList<Socket> listaSocketsServers = new ArrayList<Socket>();

	private int puertoLocal;
	private int puertoPrincipal;
	private int puertoMonitor = 11147;

	private String ipMonitor = "localhost";
	private String ipServerPrincipal;
	private String rol; // PRINCIPAL o SECUNDARIO

	private ConectionMonitor conectionMonitor = null;

	public Sincronizacion(Server server) {
		this.server = server;
		this.conectaMonitor();
	}

	public void conectaMonitor(){
		try {
			socketMonitor = new Socket(ipMonitor, puertoMonitor);

			System.out.println("Llegue 1");
			this.conectionMonitor = new ConectionMonitor(socketMonitor,this); // escucha al MONITOR
			
			System.out.println("Llegue 2");

			DataInputStream dis = new DataInputStream(socketMonitor.getInputStream());
			
			this.puertoLocal = Integer.valueOf(dis.readUTF());
			System.out.println("Llegue 3");
			this.ipServerPrincipal = dis.readUTF();
			System.out.println("Llegue 4");
			this.puertoPrincipal = Integer.valueOf(dis.readUTF());
			this.rol = dis.readUTF();
			System.out.println("Rol recibido: "+this.rol);

			System.out.println("puerto recibido: " + puertoLocal);


			if (this.rol.equals("SECUNDARIO")) {
				socketConPrincipal = new Socket(ipServerPrincipal, this.puertoPrincipal);
				server.getControlador().appendMensajes("Sincronizando server respaldo");
				System.out.println("llegue perra");
				SincronizacionEscucha sinc = new SincronizacionEscucha(this);
			}
			else {				
				this.ss = new ServerSocket(puertoLocal);
				this.start();
			}
			this.conectionMonitor.start();

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	
	public void conectarConPrincipal() throws NumberFormatException, IOException {
		
		DataInputStream dis = new DataInputStream(socketMonitor.getInputStream());
		
		this.ipServerPrincipal = dis.readUTF();
		this.puertoPrincipal = Integer.valueOf(dis.readUTF());		
		socketConPrincipal = new Socket(ipServerPrincipal, this.puertoPrincipal);
		server.getControlador().appendMensajes("Conecta con server principal");
	}
	
	public void run() {
		Socket socket = null;

		try {
			while ( Server.isTerminar() == false) {
				socket = ss.accept();
				this.listaSocketsServers.add(socket);
				System.out.println("Se creo el socket con el principal");
				server.getControlador().appendMensajes("Sincronizando server respaldoo");

//				ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());		// como creamos todos los servidores al principio siempre estan vacios los chats
//				dos.writeObject(this.server.getChats());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void sincronizaServers() throws IOException {

		if (Server.isTerminar() == false)
			for (int i = 0; i < this.listaSocketsServers.size(); i++) {
				ObjectOutputStream dos = new ObjectOutputStream(this.listaSocketsServers.get(i).getOutputStream());
				dos.writeObject(this.server.getChats());
				System.out.println("Se mandaron los chats");
			}
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public ServerSocket getSs() {
		return ss;
	}

	public void setSs(ServerSocket ss) {
		this.ss = ss;
	}

	public Socket getSocketConPrincipal() {
		return socketConPrincipal;
	}

	public void setSocketConPrincipal(Socket socketConPrincipal) {
		this.socketConPrincipal = socketConPrincipal;
	}

	
}
