package back;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import front.IVistaChat;


public class SendMessage {
	
	IVistaChat vista;
	private Conexion conexion;

//	final DataInputStream dis;
//    final DataOutputStream dos;
//    final Socket s;
    
    
//	public SendMessage(Socket s,DataInputStream dis, DataOutputStream dos,IVistaChat vista) {
//		super();
//		this.vista = vista;
//		this.dis = dis;
//		this.dos = dos;
//		this.s = s; 
//	}
     
    public SendMessage(Conexion conexion) {
    	this.conexion = conexion;
    }

	public void enviaMensaje(String mensaje) {

    	//if(this.s.isClosed() != true) {
    	//if(Conexion.getInstance().getSocket().isClosed() != true) {
    		try {
    			conexion.getDos().writeUTF("0"+Cifrado.encriptar(mensaje));
    			//this.dos.writeUTF("0"+Cifrado.encriptar(mensaje));

    		} catch (IOException e) { 
    			e.printStackTrace();
    		} catch (Exception e) {
				
				e.printStackTrace();
			}
    	//}
    	
    }
    
    public void enviaComando(String mensaje) {
    	
    	
    	//if(this.s.isClosed() != true) {
    	//if(Conexion.getInstance().getSocket().isClosed() != true) {
    		try {
    			conexion.getDos().writeUTF("1"+mensaje);
    			//this.dos.writeUTF("1"+mensaje);
    		} catch (IOException e) { 
    			e.printStackTrace();
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	//}
    }
    
    public void enviaNickName(String mensaje) {
   
    	//if(this.s.isClosed() != true) {
    	//if(Conexion.getInstance().getSocket().isClosed() != true) {
    		try {
    			conexion.getDos().writeUTF("2"+mensaje);
    			//this.dos.writeUTF("2"+mensaje);
    		} catch (IOException e) { 
    			e.printStackTrace();
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	//}
    }
    
    
//    Socket getSocket() {
//    	return this.s;
//    }
}
