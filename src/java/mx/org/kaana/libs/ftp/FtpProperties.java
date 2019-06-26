package mx.org.kaana.libs.ftp;

import java.io.Serializable;

public class FtpProperties implements Serializable {

	private static final long serialVersionUID = 5186379685793495463L;
  private String host;
  private String user;
  private String password;
  private int port;

  public FtpProperties() {
		this(null, null, null, 21);
	}
	
  public FtpProperties(String host, String user, String password, int port) {
    this.host    = host;
    this.user    = user;
    this.password= password;
    this.port    = port;
  }

  public void setHost(String host) {
    this.host=host;
  }

  public String getHost() {
    return host;
  }

  public void setUser(String user) {
    this.user=user;
  }

  public String getUser() {
    return user;
  }

  public void setPassword(String password) {    
    this.password= password;
  }

  public String getPassword() {
    return password;
  }

  public void setPort(int port) {
    this.port=port;
  }

  public int getPort() {
    return port;
  }
}
