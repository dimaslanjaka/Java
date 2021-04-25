package app.proxy;

public class Proxy {
	private String proxy;
	private int port;
	private String ip;

	public Proxy(String ipPort) {
		proxy = ipPort;
	}

	public void setProxy(String ipPort) throws Exception {
		if (!ipPort.contains(":")) {
			throw new Exception("Invalid Proxy Format, Missing \":\"");
		}
		proxy = ipPort;
	}

	public void setPort(int port) {
		this.port = port;
		proxy = this.ip + ":" + this.port;
	}

	public void setIp(String ip) {
		this.ip = ip;
		proxy = this.ip + ":" + this.port;
	}

	@Override
	public String toString() {
		return proxy;
	}
}
