package monitor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HeartBeat extends Thread {

	private Socket socket;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private boolean terminar = false;

	public void run() {

		String comando = null;
		super.run();

//		try {
//			dis = new DataInputStream(socket.getInputStream());
//			dos = new DataOutputStream(socket.getOutputStream());
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		while (this.terminar == false) {

			try {
				this.socket = Monitor.getInstance().getSocketPrincipal();
				dis = new DataInputStream(this.socket.getInputStream());
				//dos = new DataOutputStream(this.socket.getOutputStream());
				comando = dis.readUTF();

			} catch (IOException e) {
				
				e.printStackTrace();
			}

			// System.out.println(comando);

			if (comando == "HEARTBEAT") { 
				//ESPERAMOS
			} else { //CAMBIAMOS DE SERVER PRINCIPAL
				Monitor.getInstance().cambiaServerPrincipal();
				//ESPERAMOS PARA DAR TIEMPO A QUE SE CAMBIE
			}

		}
	}
}
