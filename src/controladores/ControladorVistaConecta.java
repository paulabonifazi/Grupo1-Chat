package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import back.Cliente;
import exception.CreaChatException;
import exception.UserNotAvailableException;
import front.IVistaChat;
import front.IVistaConecta;
import front.vistaChat;

public class ControladorVistaConecta implements ActionListener {

	private IVistaConecta vistaConecta = null;
	private Cliente cliente = null;
	private ControladorVistaChat cont;

	public ControladorVistaConecta(IVistaConecta vista) {
		this.vistaConecta = vista;
		this.vistaConecta.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();

		if (comando.equalsIgnoreCase("CONECTAR")) {
			boolean condition = !this.vistaConecta.getNicknameReceptor().equals("user");

			try {
				if (condition == false)
					JOptionPane.showMessageDialog(null, "Ingrese un usuario válido");
				else
					this.cliente.conectarReceptor(this.vistaConecta.getNicknameReceptor());

			} catch (UserNotAvailableException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			} catch (NumberFormatException e1) {
				System.out.println("e1");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CreaChatException e1) {
				iniciaChat();
			}
		}

	}

	public void iniciaChat() {
		IVistaChat vistaChat = new vistaChat();
		cont = new ControladorVistaChat(vistaChat);
		cont.setVistaChat(vistaChat);
		cont.setCliente(this.cliente);

		this.cliente.setContChat(cont);
		vistaChat.setCont(cont);

		this.vistaConecta.mostrarVentana(false);
		vistaChat.mostrarVentana(true);
	}

	public void setVistaConecta(IVistaConecta vistaConecta) {
		this.vistaConecta = vistaConecta;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void ventanaEmergente(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}

	public ControladorVistaChat getCont() {
		return cont;
	}

	public void setCont(ControladorVistaChat cont) {
		this.cont = cont;
	}

	public IVistaConecta getVistaConecta() {
		return vistaConecta;
	}

}