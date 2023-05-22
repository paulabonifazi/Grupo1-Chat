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

public class Conection extends Thread {
	
	DataCliente dataCliente;
	private HashMap<String,DataCliente> clientes;
	
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket socket;
	
    private boolean terminar = false;
    
	public Conection(DataInputStream dis, DataOutputStream dos, Socket s) {
		super();
		this.dis = dis;
		this.dos = dos;
		this.socket = s;
	}

	public Conection(Socket s,DataCliente conexion,HashMap<String,DataCliente> clientes) throws IOException {
		super();
		this.dis = new DataInputStream(s.getInputStream());
		this.dos = new DataOutputStream(s.getOutputStream());
		this.socket = s;
		this.dataCliente = conexion;
		this.clientes = clientes;
	} 
	
	public Conection(Socket s,DataCliente conexion,HashMap<String,DataCliente> clientes,DataInputStream dis,DataOutputStream dos) throws IOException {
		super();
		this.dis = dis;
		this.dos = dos;
		this.socket = s;
		this.dataCliente = conexion;
		this.clientes = clientes;
	}
	
	public void run() {

	//	String recibido;
		super.run();
		String comando; 
		String mensaje;
		char bandera;
		
		//while(this.terminar == false && this.s.isClosed() != true) {
		while(this.terminar == false) {
			
			//System.out.println("Hilo");
			try {
				if(dis.available()> 0) {
					//recibido = (String) dis.readObject();
					 mensaje = dis.readUTF();
			         bandera = mensaje.charAt(0);
			         mensaje = mensaje.substring(1);
			         
			         System.out.println(bandera);
			         System.out.println(mensaje);
			         
			         
			         if(bandera == '0') { 
	                        if(this.dataCliente.getNicknameReceptor() != null) {
	                            this.clientes.get(this.dataCliente.getNicknameReceptor()).getDos().writeUTF("0"+mensaje);
	                            this.clientes.get(this.dataCliente.getNicknameReceptor()).getDos().flush();
	                        }else {
	                            if((mensaje.equals(this.dataCliente.getNickname()))==false &&  this.clientes.containsKey(mensaje)){
	                                System.out.println(this.clientes);
	                                this.dataCliente.setNicknameReceptor(mensaje);
	                                this.clientes.get(mensaje).setNicknameReceptor(this.dataCliente.getNickname());
	                                // INICIAR LOS CHATS EN LOS CLIENTES
	                                comando = "1INICIARCHAT";
	                                this.dataCliente.getDos().writeUTF(comando);
	                                this.dataCliente.getDos().flush();

	                                this.clientes.get(mensaje).getDos().writeUTF(comando);
	                                this.clientes.get(mensaje).getDos().flush();

	                            }else { 
	                                // no esta registrado ese nickname de receptor
	                                comando = "1NOREGISTRADO";
	                                this.dataCliente.getDos().writeUTF(comando);
	                                this.dataCliente.getDos().flush();
	                            }
	                        }
	                    } else {
	                        if (bandera == '1') {    // comando para el servidor

	                            if(mensaje == "FINALIZARCHAT") {
	                                this.clientes.get(mensaje).getDos().writeUTF("1"+mensaje);
	                                this.clientes.get(mensaje).getDos().flush();
	                            }
	                        }
	                    }		         
			       
//			        if(bandera == '0') { 
//			        	this.clientes.get(this.dataCliente.getNicknameReceptor()).getDos().writeUTF("0"+mensaje);
//			        }
//			        else {
//			        	if(bandera == '2') {
//			        		if((mensaje.equals(this.dataCliente.getNickname()))==false &&  this.clientes.containsKey(mensaje)) {
//			        			this.dataCliente.setNicknameReceptor(mensaje);
//								this.clientes.get(mensaje).setNicknameReceptor(this.dataCliente.getNickname());
//								
//								// INICIAR LOS CHATS EN LOS CLIENTES
//								comando = "1INICIARCHAT";
//								this.dataCliente.getDos().writeUTF(comando);
//								this.dataCliente.getDos().flush();
//								this.clientes.get(mensaje).setNicknameReceptor(this.dataCliente.getNickname());
//								this.clientes.get(mensaje).getDos().writeUTF(comando);
//								this.clientes.get(mensaje).getDos().flush();
//			        		}
//			        	}
//			        	else { 
//							// no esta registrado ese nickname de receptor
//							comando = "1NOREGISTRADO";
//							this.dataCliente.getDos().writeUTF(comando);
//							this.dataCliente.getDos().flush();
//						}
//			        }
					
//					} else {
//						if (bandera == '1') {	// comando para el servidor
//							
//							if(mensaje == "FINALIZARCHAT") {
//								this.clientes.get(mensaje).getDos().writeUTF("1"+mensaje);
//								this.clientes.get(mensaje).getDos().flush();
//							}
//						}
//					}
			        
			        
			        //------------------
				}
				
			} 
			catch (EOFException e) {
				//e.printStackTrace();
				this.terminarRecibirMensajes();
			}
			catch (SocketException e1) {
				this.terminarRecibirMensajes();
				//e1.printStackTrace();
			}
			catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void terminarRecibirMensajes() {
		//String mensaje = "El otro usuario se desconecto.\n";
		this.terminar = true;
		
		//this.vista.getTextArea().setText(this.vista.getTextArea().getText()+"\n"+mensaje);
		try {
			this.dis.close();
			this.dos.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}