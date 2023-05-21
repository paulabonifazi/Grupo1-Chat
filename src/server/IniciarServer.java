package server;

import java.io.IOException;

public class IniciarServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Server server = new Server();

		server.start();

		try {

			server.Registrar(); // el hilo se va a quedar aca

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//	}

	}
}