package mx.org.kaana.libs.ftp;

public class FtpElementoLista {
  private String origen;
  private String destino;

  public FtpElementoLista(String origen, String destino) {
    setOrigen(origen);
    setDestino(destino);
  }

  public void setOrigen(String origen) {
    this.origen=origen;
  }

  public String getOrigen() {
    return origen;
  }

  public void setDestino(String destino) {
    this.destino=destino;
  }

  public String getDestino() {
    return destino;
  }
}
