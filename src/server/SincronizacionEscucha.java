package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SincronizacionEscucha extends Thread {

	//private Socket socket;
	private Sincronizacion sinc;
	private ObjectInputStream input;

	private ArrayList<DataCliente> listaClientes;
	private HashMap<String, DataCliente> clientes;
	private HashMap<String, String> chats;

	public SincronizacionEscucha(Sincronizacion sinc) throws IOException {
		super();
		//this.socket = socket;
		this.sinc = sinc;
		//this.input = new ObjectInputStream(socket.getInputStream());
		this.start();
	}

	@Override
	public void run() {
		super.run();
		while (Server.isTerminar() == false && Server.isPrincipal() == false) {
			try {
				System.out.println("esperando chats");
				input = new ObjectInputStream(sinc.getSocketConPrincipal().getInputStream());
				this.chats = (HashMap<String, String>) this.input.readObject();
				System.out.println("lei chats");
				this.sinc.getServer().getControlador().appendMensajes("Sincronizando server respaaldo");
//				this.server.getControlador().appendMensajes("Sincronizando server respaaldo");
				this.sinc.getServer().getControlador().appendMensajes(chats.toString());
				this.seteaClientes();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void seteaClientes() {
		for (int i = 0; i < this.chats.size(); i++) {
			
			this.sinc.getServer().getLista().get(i)
					.setNicknameReceptor(this.chats.get(this.sinc.getServer().getLista().get(i).getNickname()));
			System.out.println(this.sinc.getServer().getLista().get(i).getNickname() + ", "
					+ this.sinc.getServer().getLista().get(i).getNicknameReceptor());
		}
		
	}

}
