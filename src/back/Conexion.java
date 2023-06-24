package back;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import server.DataCliente;

public class Conexion {
	
	private static Conexion instance = null;
	private static Socket socketServidor = null;
	private static Socket socketMonitor = null;
	
	private static int nro = 0;
//	private static DataInputStream dis = null;
//	private static DataOutputStream dos = null;
	private Cliente cliente;
	// esto puede ser un arreglo de sockets
	private int i = 1;
	private ArrayList<Socket> sockets = new ArrayList<Socket>();
	private boolean echo = false;
	private boolean cambiaServer = false;
	
//	private Socket s2;
//	private DataInputStream dis2;
//	private DataOutputStream dos2;
    
	Conexion () {
		
	}
	
//	private Conexion(Socket s, DataInputStream dis, DataOutputStream dos) {
//	
//			super();
//			this.s = s;
//			this.dis = dis;
//			this.dos = dos;
//	}
	
//	public static Conexion getInstance() {
//		
//		if(instance == null) {
//			instance = new Conexion();
////			s = s;
////			dis = dis;
////			dos = dos;
//		}
//			return instance;
//	}
	
//public void agregarSocket(String ip, int puerto) throws IOException{
//		
//	Socket s = new Socket(ip,puerto);
//	
//		if(socket == null) {
//			socket = s;
////			Socket s2 = new Socket(ip,puerto+1);	//hay al menos 2 servers
////			this.sockets.add(s2);
//		}
//		
//		this.sockets.add(s);
//		System.out.println("Sockets: " + this.sockets);
//		
//	}
//
//	public void agregarSocket(Socket s) {
//		
//		if(socket == null) {
//			socket = s;
//		}
//		this.sockets.add(socket);
//		System.out.println("Sockets: " + this.sockets);
//	}
	
    
//	public void registrar(String nickname) throws IOException { //registramos al cliente en todos los servers
//		DataOutputStream outPut = null;
//		
//		for(int i = 0; i < sockets.size();i++) {
//			outPut = new DataOutputStream (this.sockets.get(i).getOutputStream());
//			outPut.writeUTF("1" + nickname);
//		}
//	}
	
	public void registrar(String nickname) throws IOException { //registramos al cliente en todos los servers
		DataOutputStream outPut = null;
		outPut = new DataOutputStream (socketServidor.getOutputStream());
		outPut.writeUTF("1" + this.cliente.getNickname());
	}
	
	public synchronized void verificaServer() {
	
//		if(Conexion.getInstance().getCambiaServer() == true) {
		if(this.socketServidor.isClosed() == true) {
			System.out.println(socketServidor.isClosed()+" "+socketServidor.getPort());
			this.cambiaServer(); 
		}
		
	}
	
//	public synchronized void cambiaServer() {
//		//this.cliente.getReceiveMessage().interrupt();
//		
//		//this.cliente.getReceiveMessage().stop();
//		if(sockets.size() > this.i) {		
//			socket = this.sockets.get(i);
//			System.out.println("Socket: " + socket.getPort());	
//			i++;
//			//Conexion.getInstance().setEcho(true);
//			
//			
////			ReceiveMessage recibe = new ReceiveMessage(this.cliente.getContInicial(), this.cliente.getContChat(), this.cliente.getContConecta());
////			recibe.start();
//			//this.cliente.recibirMensajes();
//			
//			
//		}
//	}
	
	public synchronized void cambiaServer() {
		
		try {
			System.out.println("Hace socket con nuevo server");
			socketServidor = new Socket(this.cliente.getiP(),this.cliente.getPuerto()+nro);
			nro++;
			this.registrar(this.cliente.getNickname());
			
			System.out.println("nro: "+nro);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------------ GETTERS ---------------------------------------------------
	public  Socket getSocket() {
		return socketServidor;
	}

	public DataInputStream getDis() {
		DataInputStream inPut = null;
		
		this.verificaServer();
		
		try {
			//System.out.println("Socket: " + socket.getPort());	
			inPut = new DataInputStream(socketServidor.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inPut;
	}

	public DataOutputStream getDos() {
		DataOutputStream outPut = null;
		
		this.verificaServer();
		
		try {
			outPut = new DataOutputStream (socketServidor.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outPut;
	}

	public boolean getEcho() {
		return echo;
	}

	public void setEcho(boolean echo) {
		this.echo = echo;
	}

	public boolean getCambiaServer() {
		return cambiaServer;
	}

	public void setCambiaServer(boolean cambiaServer) {
		this.cambiaServer = cambiaServer;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
}
