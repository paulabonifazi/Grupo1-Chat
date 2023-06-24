package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import front.IVistaChat;

public class ConectionCliente extends Thread {

	DataCliente dataCliente;
	private HashMap<String, DataCliente> clientes;
	private HashMap<String, String> chats;
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket socket;
	private ControladorVistaServer cont;
	private Sincronizacion sincronizacion;
	
	private boolean terminar = false;

	public ConectionCliente(DataInputStream dis, DataOutputStream dos, Socket s) {
		super();
		this.dis = dis;
		this.dos = dos;
		this.socket = s;
		
	}

	public ConectionCliente(Socket s, DataCliente conexion, HashMap<String, DataCliente> clientes) throws IOException {
		super();
		this.dis = new DataInputStream(s.getInputStream());
		this.dos = new DataOutputStream(s.getOutputStream());
		this.socket = s;
		this.dataCliente = conexion;
		this.clientes = clientes;
	}

	public ConectionCliente(Socket s, DataCliente conexion, HashMap<String, DataCliente> clientes, DataInputStream dis,
			DataOutputStream dos,HashMap<String, String> chats,Sincronizacion sincronizacion) throws IOException {
		super();
		this.dis = dis;
		this.dos = dos;
		this.socket = s;
		this.dataCliente = conexion;
		this.clientes = clientes;
		this.chats = chats;
		this.sincronizacion = sincronizacion;
	}

	public void run() {

		// String recibido;
		super.run();
		String comando;
		String mensaje;
		char bandera;

		while (this.terminar == false) {

			try {
				if (dis.available() > 0) {

					mensaje = dis.readUTF();
					bandera = mensaje.charAt(0);
					mensaje = mensaje.substring(1);
					
					

					if (bandera == '0') {
						if (this.dataCliente.getNicknameReceptor() != null) {
							this.cont.appendMensajes(dataCliente.getNickname()+" para "+dataCliente.getNicknameReceptor()+ ":" +mensaje);
							this.clientes.get(this.dataCliente.getNicknameReceptor()).getDos().writeUTF("0" + mensaje);
							this.clientes.get(this.dataCliente.getNicknameReceptor()).getDos().flush();
						} 
					} else {
						if (bandera == '1') { // comando para el servidor

							if (mensaje.equals("FINALIZARCHAT") == true) {
								this.cont.appendMensajes(dataCliente.getNickname()+" finalizó el chat con "+dataCliente.getNicknameReceptor());

								this.clientes.get(this.dataCliente.getNicknameReceptor()).getDos()
										.writeUTF("1" + mensaje);
								this.clientes.get(this.dataCliente.getNicknameReceptor()).setNicknameReceptor(null);
								this.dataCliente.setNicknameReceptor(null);

							}
						}else {
							if(bandera == '2') {
								if ((mensaje.equals(this.dataCliente.getNickname())) == false
										&& this.clientes.containsKey(mensaje) && this.clientes.get(mensaje).getNicknameReceptor()==null) {
								
									this.dataCliente.setNicknameReceptor(mensaje);
									this.clientes.get(mensaje).setNicknameReceptor(this.dataCliente.getNickname());
									
									//seteamos los chats
									this.chats.put(this.dataCliente.getNickname(), mensaje);
									this.chats.put(mensaje, this.dataCliente.getNickname());
									System.out.println("hasta aca llego");
									this.sincronizacion.sincronizaServers();
									//SincronizacionOut.sincronizarServer();
									
									
									comando = "1INICIARCHAT";
									this.dataCliente.getDos().writeUTF(comando);
									this.dataCliente.getDos().flush();

									this.clientes.get(mensaje).getDos().writeUTF(comando);
									this.clientes.get(mensaje).getDos().flush();
									this.cont.appendMensajes(dataCliente.getNickname()+" inició un chat con "+dataCliente.getNicknameReceptor());
									//this.sincronizacion.sincronizaServers();

										
								} else {
									// no esta registrado el receptor
									comando = "1NOREGISTRADO";
									this.dataCliente.getDos().writeUTF(comando);

								}
							}
//							else if(bandera == '3') {
//								if(mensaje.equals("PING")== true && Server.isTerminar() == false)
//									this.dataCliente.getDos().writeUTF("3ECHO");
//							} 
						}
					}

				}

			} catch (EOFException e) {
				// e.printStackTrace();
				this.terminarRecibirMensajes();
			} catch (SocketException e1) {
				this.terminarRecibirMensajes();
				// e1.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	public void terminarRecibirMensajes() {
		
		this.terminar = true;

		try {
			this.dis.close();
			this.dos.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ControladorVistaServer getCont() {
		return cont;
	}

	public void setCont(ControladorVistaServer cont) {
		this.cont = cont;
	}
	
}
