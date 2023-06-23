package monitor;

public class InfoServer {
	
	private int puertoPrincipal;
	private String ipPrincipal;
	
	
	public InfoServer(int puertoPrincipal, String ipPrincipal) {
		this.puertoPrincipal = puertoPrincipal;
		this.ipPrincipal = ipPrincipal;
	}


	public int getPuertoPrincipal() {
		return puertoPrincipal;
	}


	public void setPuertoPrincipal(int puertoPrincipal) {
		this.puertoPrincipal = puertoPrincipal;
	}


	public String getIpPrincipal() {
		return ipPrincipal;
	}


	public void setIpPrincipal(String ipPrincipal) {
		this.ipPrincipal = ipPrincipal;
	}
	
	
}
