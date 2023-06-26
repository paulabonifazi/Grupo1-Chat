package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import server.DataCliente;

public class Server extends Thread {

	private ServerSocket serverSocketCliente;
	public int puerto;
	private HashMap<String, DataCliente> clientes = new HashMap<>();
	private HashMap<String, String> chats = new HashMap<>();
	private ArrayList<DataCliente> listaClientes = new ArrayList<DataCliente>();
	private ControladorVistaServer controlador;
	private static boolean terminar = false;
	private static boolean principal = false;
	private Sincronizacion sincronizacion;
	private Socket s = null;

	public Server(String text, ControladorVistaServer cont) {
		this.puerto = Integer.parseInt(text);

		try {
			this.serverSocketCliente = new ServerSocket(puerto);
			this.controlador = cont;
			this.controlador.setServer(this);
			this.sincronizacion = new Sincronizacion (this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		String nickname;
		String nicknameReceptor;
		DataCliente dataCliente;
		Object object;
		char bandera;
		

		try {
			while (this.terminar == false) {
				s = serverSocketCliente.accept();
				//i++;
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());

				nickname = dis.readUTF();
				bandera = nickname.charAt(0);
				nickname = nickname.substring(1);

				if (this.clientes.containsKey(nickname) == false) {
					//System.out.println("Se hace nuevo data cliente: "+nickname);
					dataCliente = new DataCliente(s, nickname, dis, dos);
					this.clientes.put(nickname, dataCliente);
					this.listaClientes.add(dataCliente);
					
					ConectionCliente conection = new ConectionCliente(s, dataCliente.getNickname(), this.clientes, dis, dos, this.chats,this.sincronizacion);
					conection.setCont(controlador);
					conection.start();
					dos.writeUTF("1REGISTRADOCORRECTAMENTE");
					controlador.appendListaConectados(dataCliente.toString());

				} else {

					dos.writeUTF("1USERREGISTRADO");
				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
	
	public ArrayList<DataCliente> getLista() {
		return listaClientes;
	}

	public HashMap<String, DataCliente> getClientes() {
		return clientes;
	}

	public void setLista(ArrayList<DataCliente> lista) {
		this.listaClientes = lista;
	}

	public void closeServer() throws IOException { // podriamos cerrar el socket de conexion con otros servidores tmb
		this.terminar = true;
		this.principal = false;
//		this.s.close();
	}

	public HashMap<String, String> getChats() {
		return chats;
	}

	public ControladorVistaServer getControlador() {
		return controlador;
	}

	public void setControlador(ControladorVistaServer controlador) {
		this.controlador = controlador;
	}

	public ArrayList<DataCliente> getListaClientes() {
		return listaClientes;
	}

	public void setServerSocketCliente(ServerSocket serverSocketCliente) {
		this.serverSocketCliente = serverSocketCliente;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public void setClientes(HashMap<String, DataCliente> clientes) {
		this.clientes = clientes;
	}

	public void setChats(HashMap<String, String> chats) {
		this.chats = chats;
	}

	public void setListaClientes(ArrayList<DataCliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public static void setTerminar(boolean terminar) {
		Server.terminar = terminar;
	}

	public static void setPrincipal(boolean principal) {
		Server.principal = principal;
	}

	public void setSincronizacion(Sincronizacion sincronizacion) {
		this.sincronizacion = sincronizacion;
	}

	public static boolean isPrincipal() {
		return principal;
	}

	public static boolean isTerminar() {
		return terminar;
	}

	public Sincronizacion getSincronizacion() {
		return sincronizacion;
	}

	
	
}