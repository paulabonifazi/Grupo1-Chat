package back;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import controladores.ControladorVistaChat;
import controladores.ControladorVistaConecta;
import controladores.ControladorVistaInicial;

public class ReceiveMessage extends Thread {
	ControladorVistaChat contChat = null;
	ControladorVistaConecta contConecta = null;
	ControladorVistaInicial contInicial = null;
//	final DataInputStream dis;
//	final DataOutputStream dos;
//	final Socket s;
	private boolean terminar = false;
	String receptor = "";
	private Conexion conexion;

	public ReceiveMessage() {
		super();

	}

//	public ReceiveMessage(ControladorVistaInicial contInicial2, ControladorVistaChat contChat2,
//			ControladorVistaConecta contConecta2) {
//		this.contChat = contChat2;
//		this.contConecta = contConecta2;
//		this.contInicial = contInicial2;
//	}

	public ReceiveMessage(Conexion conexion) {
		this.conexion = conexion;
	}

	public void recibirMensajes() {
		new Thread(() -> {
			String mensaje;
			char comando;
			super.run();

			while (this.terminar == false) {
				try {
					//System.out.println("LEgoooo");
					mensaje = this.conexion.getDis().readUTF();
//					mensaje = dis.readUTF();
					comando = mensaje.charAt(0);
					mensaje = mensaje.substring(1);

					System.out.println(comando + " " + mensaje);

					if (comando == '0') { // mensaje
						System.out.println("Me llego el mensaje");
						this.contChat.appendTextArea(Cifrado.desencriptar(mensaje));

					} else {
						if (comando == '1') {
							switch (mensaje) {
							case "INICIARCHAT":
								this.contConecta.iniciaChat();
								break;
							case "NOREGISTRADO":
								this.contConecta.ventanaEmergente("El usuario no se encuentra disponible.");
								break;
							case "FINALIZARCHAT":

								this.contConecta.ventanaEmergente("El chat fue finalizado por el otro usuario");
								this.contChat.abandonarChat();
								break;
							case "USERREGISTRADO":
								this.contInicial
										.ventanaEmergente("El usuario ya se encuentra registrado en el sistema.");
								// evitar que se habra la ventana de conecta
								// this.contInicial.setRegistrado(false);
								break;
							case "REGISTRADOCORRECTAMENTE":
								this.contInicial.vistaSiguiente();
								break;
							}

						} else if (comando == '3') {
							if (mensaje.equals("ECHO") == true) {
								System.out.println("Recibimos ECHO");
								this.conexion.setEcho(true);
							}
						} else if (comando == '4')
							System.out.println(mensaje);
					}

				} catch (EOFException e) {
					this.terminarRecibirMensajes();
				} catch (SocketException e1) {
					this.terminarRecibirMensajes();
				} catch (IOException e2) {
					e2.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
//	public void run() {
//
//		String mensaje;
//		char comando;
//		super.run();
//
//		while (this.terminar == false) {
//			try {
//				System.out.println("LEgoooo");
//				mensaje = this.conexion.getDis().readUTF();
////				mensaje = dis.readUTF();
//				comando = mensaje.charAt(0);
//				mensaje = mensaje.substring(1);
//				
//				System.out.println(comando+" " +mensaje);
//
//				if (comando == '0') { // mensaje
//					System.out.println("Me llego el mensaje");
//					this.contChat.appendTextArea(Cifrado.desencriptar(mensaje));
//					
//				} else {
//					if (comando == '1') {
//						switch (mensaje) {
//						case "INICIARCHAT":
//							this.contConecta.iniciaChat();
//							break;
//						case "NOREGISTRADO":
//							this.contConecta.ventanaEmergente("El usuario no se encuentra disponible.");
//							break;
//						case "FINALIZARCHAT":
//
//							this.contConecta.ventanaEmergente("El chat fue finalizado por el otro usuario");
//							this.contChat.abandonarChat();
//							break;
//						case "USERREGISTRADO":
//							this.contInicial.ventanaEmergente("El usuario ya se encuentra registrado en el sistema.");
//							// evitar que se habra la ventana de conecta
//							//this.contInicial.setRegistrado(false);
//							break;
//						case "REGISTRADOCORRECTAMENTE":
//							this.contInicial.vistaSiguiente();
//							break;
//						}
//
//					}else if(comando == '3') {
//						if(mensaje.equals("ECHO") == true) {
//							System.out.println("Recibimos ECHO");
//							this.conexion.setEcho(true);
//						}
//					}else
//						if(comando == '4')
//							System.out.println(mensaje);
//				}
//
//			} catch (EOFException e) {
//				this.terminarRecibirMensajes();
//			} catch (SocketException e1) {
//				this.terminarRecibirMensajes();
//			} catch (IOException e2) {
//				e2.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

	public void terminarRecibirMensajes() {

		this.terminar = true;

		try {
			this.conexion.getDis().close();
			this.conexion.getDos().close();
			this.conexion.getSocket().close();

//			this.dis.close();
//			this.dos.close();
//			this.s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ControladorVistaChat getContChat() {
		return contChat;
	}

	public void setContChat(ControladorVistaChat contChat) {
		this.contChat = contChat;
	}

	public ControladorVistaConecta getContConecta() {
		return contConecta;
	}

	public void setContConecta(ControladorVistaConecta contConecta) {
		this.contConecta = contConecta;
	}

	public ControladorVistaInicial getContInicial() {
		return contInicial;
	}

	public void setContInicial(ControladorVistaInicial contInicial) {
		this.contInicial = contInicial;
	}

}
