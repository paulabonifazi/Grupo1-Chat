package server;

import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class SincronizacionOut {

//	private static ArrayList<DataCliente> listaClientes;
//	private static HashMap<String, DataCliente> clientes;
//	private static HashMap<String, String> chats;

	private static Socket socketAnterior;
	private static ServerSocket serverSocket;
	private static String ip = "localhost";

	private static int puertoActual = 201;
	private static int puertoAnterior = 200; // chequear los numero de los puertos

	private static ObjectOutputStream outPut;
	private static ObjectInputStream input;
	private static Server server;
	//private static SincronizacionIn sincronizacionIn = null;

	public SincronizacionOut(Server server) {
		super();
		this.server = server;
//		this.chats = server.getChats();
//		this.clientes = server.getClientes();
//		this.listaClientes = server.getListaClientes();
	}

	public static void sincronizarServer() {

		try {
			if (puertoAnterior != -1) { // si hay server anterior nos conectamos
				socketAnterior = new Socket(ip, puertoAnterior);
				outPut = new ObjectOutputStream(socketAnterior.getOutputStream());

				//outPut.writeObject(server.getChats());
//				outPut.writeObject(server.getClientes());
//				outPut.writeObject(server.getListaClientes());

				//server.getControlador().appendMensajes("Sincronizando server respaldo");
//				dis = new DataInputStream(socket.getInputStream());
//				dos = new DataOutputStream(socket.getOutputStream());
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void creaServerSocket(int puerto) throws IOException {
		serverSocket = new ServerSocket(puerto);
	}

	private static void conectaServerAnterior() throws IOException {

		if (puertoAnterior != -1)
			socketAnterior = new Socket(ip, puertoAnterior);

	}
//
//	public static void setServer(Server server) {
//		SincronizacionOut.server = server;
//		sincronizacionIn = new SincronizacionIn(server);
//		sincronizacionIn.setServerSocket(serverSocket);
//	}

//	public static SincronizacionIn getSincronizacionIn() {
//		return sincronizacionIn;
//	}
//
//	public static Socket getSocket() {
//		return socketAnterior;
//	}
//
//	public void start() {
//		sincronizacionIn = new SincronizacionIn(server);
//		sincronizacionIn.setServerSocket(serverSocket);
//		sincronizacionIn.start();
//	}

//	private void seteaClientes() {
//
//		for (int i = 0; i < this.chats.size(); i++) {
//			this.server.getLista().get(i)
//					.setNicknameReceptor(this.chats.get(this.server.getLista().get(i).getNickname()));
//			System.out.println(this.server.getLista().get(i).getNickname() + ", "
//					+ this.server.getLista().get(i).getNicknameReceptor());
//		}
//	}

}
