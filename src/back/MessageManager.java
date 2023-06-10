package back;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import front.IVistaChat;


public class MessageManager {
	
	IVistaChat vista;
	final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    
	public MessageManager(Socket s,DataInputStream dis, DataOutputStream dos,IVistaChat vista) {
		super();
		this.vista = vista;
		this.dis = dis;
		this.dos = dos;
		this.s = s; 
	}
     
//private void chequeaServer(Socket s) {
//		this.s = s;
//		this.dis = this.s.getInputStream();
//		this.dos = this.s.getOutputStream();
//		
//	}
//	private void chequeaServer() {
//		
//		this.dis = this.s.getInputStream();
//		this.dos = this.s.getOutputStream();
//		
//	}
	
	public void enviaMensaje(String mensaje) {

    	if(this.s.isClosed() != true) {
    		try {
    			
    			this.dos.writeUTF("0"+Cifrado.encriptar(mensaje));

    		} catch (IOException e) { 
    			e.printStackTrace();
    		} catch (Exception e) {
				
				e.printStackTrace();
			}
    	}
    	
    }
    
    public void enviaComando(String mensaje) {
    	
    	
    	if(this.s.isClosed() != true) {
    		try {
    			this.dos.writeUTF("1"+mensaje);
    		} catch (IOException e) { 
    			e.printStackTrace();
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
    public void enviaNickName(String mensaje) {
   
    	if(this.s.isClosed() != true) {
    		try {
    			this.dos.writeUTF("2"+mensaje);
    		} catch (IOException e) { 
    			e.printStackTrace();
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    
    Socket getSocket() {
    	return this.s;
    }
}
