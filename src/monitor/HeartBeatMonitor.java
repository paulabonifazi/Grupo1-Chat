package monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HeartBeatMonitor extends Thread {

	private Socket socket;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private boolean terminar = false;

	private int tiempo = 5;
	private int intentos = 0;

	public void run() {

		String comando = null;
		super.run();

		System.out.println("Monitor");
		while (this.terminar == false) {

			try {
				this.socket = Monitor.getInstance().getSocketPrincipal();
				dis = new DataInputStream(this.socket.getInputStream());
				if (dis.available() > 0) {
					comando = dis.readUTF();
					System.out.println("Monitor recibio: "+comando);
				}
				
			} catch (IOException e) {

				e.printStackTrace();
			}

			if (comando.equals("HEARTBEAT")) {
				// ESPERAMOS
				comando = "No llego heartbeat";	
				try {
					Thread.sleep(tiempo * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Monitor: "+comando);
				if (this.intentos == 0) { // CAMBIAMOS DE SERVER PRINCIPAL

					try {
						Monitor.getInstance().cambiaServerPrincipal();
						Thread.sleep(tiempo * 1000);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) { // ESPERAMOS PARA DAR TIEMPO A QUE SE CAMBIE
						e.printStackTrace();
					}

				} else {
					this.intentos--;
					try {
						Thread.sleep(tiempo * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

}