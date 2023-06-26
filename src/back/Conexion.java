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

	private static Socket socketServidor = null;
	private static Socket socketMonitor = null;

	private static int puertoMonitorClientes = 11222;

	private static int nro = 0;
	private Cliente cliente;
	private int i = 1;
	private ArrayList<Socket> sockets = new ArrayList<Socket>();
	private ArrayList<Socket> socketsCaidos = new ArrayList<Socket>();
	private boolean echo = false;
	private boolean cambiaServer = false;
	//private int condicion = 0;
	
	public Conexion() {

	}


	public void registrar(String nickname) throws IOException { // registramos al cliente en todos los servers
		DataOutputStream outPut = null;
		outPut = new DataOutputStream(socketServidor.getOutputStream());
		outPut.writeUTF("1" + this.cliente.getNickname());
		//this.cliente.recibirMensajes();
	}

	public synchronized void verificaServer() {

		if (this.socketServidor.isClosed() == true) {
			System.out.println(socketServidor.isClosed() + " " + socketServidor.getPort());
			this.cambiaServer();
		}

	}

	public void escuchaMonitor() { // el monitor indica que hay un cambio de servidor
		new Thread(() -> {
			try {
				DataInputStream dis = new DataInputStream(this.socketMonitor.getInputStream());
				int puerto;
				String ip;
				String comando;

				System.out.println("escuchaMonitor");
				while (true) {
					if (dis.available() > 0) {
						comando = dis.readUTF();
						System.out.println(comando);
						if (comando.equals("CAMBIAR_SERVER")) {
							this.cambiaServer();
						}
//						puerto = Integer.valueOf(dis.readUTF());
//						ip = dis.readUTF();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void conectaMonitor() throws IOException {
		System.out.println("Conecta monitor");
		this.socketMonitor = new Socket("localhost", this.puertoMonitorClientes);
	}

	public synchronized void cambiaServer() {
		
		if (this.socketsCaidos.size()==0) {
			try {
				System.out.println("Hace socket con nuevo server");
				if (socketServidor != null)
					this.socketsCaidos.add(socketServidor);
				
				socketServidor = new Socket(this.cliente.getiP(), this.cliente.getPuerto() + nro);
				nro++;
				this.registrar(this.cliente.getNickname());
				if(this.cliente.getReceiveMessage() != null)
					this.cliente.getReceiveMessage().recibirMensajes();
				//this.cliente.getReceiveMessage().s
				System.out.println("nro: " + nro);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			socketServidor = this.socketsCaidos.get(0);
			this.socketsCaidos.remove(0);
			try {
				socketServidor.close();
				socketServidor = new Socket ("localhost", 1212);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}

	// ------------------------------------------------------ GETTERS
	// ---------------------------------------------------
	public Socket getSocket() {
		return socketServidor;
	}

	public DataInputStream getDis() {
		DataInputStream inPut = null;

		// this.verificaServer();

		try {
			// System.out.println("Socket: " + socket.getPort());
			inPut = new DataInputStream(socketServidor.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inPut;
	}

	public DataOutputStream getDos() {
		DataOutputStream outPut = null;
 
		// this.verificaServer();

		try {
			outPut = new DataOutputStream(socketServidor.getOutputStream());
		} catch (IOException e) {
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
