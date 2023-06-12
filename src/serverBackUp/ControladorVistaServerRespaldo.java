package serverBackUp;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import front.IVistaInicial;

public class ControladorVistaServerRespaldo implements ActionListener{
	
	private static IVistaServer vistaServer;
	private static Server server;
	
	public ControladorVistaServerRespaldo(IVistaServer vista) {
		this.vistaServer = vista;
		this.vistaServer.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();

		if (comando.equalsIgnoreCase("CLOSE_SERVER")) {
			try {
				this.server.closeServer();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			this.vistaServer.getTextArea().append("Este servidor fue desconectado");
			//aca podriamos cerrarla
		}
		else if (comando.equalsIgnoreCase("CREAR_SERVER")) {
			this.server = new Server(this.vistaServer.getTextFieldPuerto().getText(), this);
			this.server.start();
//			try {
//				this.server.Registrar();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}
		
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
	
	public static void setListaConectados() {
		vistaServer.getTextArea().setText(server.getLista().toString());  
	}
	
	public void appendListaConectados (String dataCliente) {
		this.vistaServer.getTextArea().append("\n" + dataCliente);
	}
	
	public void appendMensajes (String mensaje) {
		this.vistaServer.getTextAreaMensajes().append("\n" + mensaje);
	}
	
	public int getNro() {
		return VistaServer.nro;
	}
}