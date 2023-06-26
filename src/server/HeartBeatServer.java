package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class HeartBeatServer extends Thread{

	private Socket socketMonitor; // lo tenemos que traer desde SINCRONIZACION
	//private Boolean terminar = false;
	private DataOutputStream dos;
	private final String HEARTBEAT = "HEARTBEAT";
	private int tiempo = 4;
	
	public HeartBeatServer(Socket socketMonitor) {
		this.socketMonitor = socketMonitor;
		//this.dos  = new DataOutputStream(this.socketMonitor.getOutputStream());
	}

	public void iniciaThread() {
		new Thread(() -> {
			try {
				dos = new DataOutputStream(socketMonitor.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			while (Server.isTerminar() == false) {
				try {
					dos.writeUTF(HEARTBEAT);
					Thread.sleep(tiempo * 1000);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        }).start();
	}
	public void run() {
		
	}
}
