package mx.org.kaana.mantic.ventas.caja.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *@company KAANA
 *@project KAJOOL 
 *@date 20/09/2020
 *@time 02:31:23 PM 
 *@author Team Developer 2016 <team.developer@gmail.com>
 */

public final class Abono implements Serializable {

  private static final long serialVersionUID = 5772381754144953456L;

  private Double monto;
  private Timestamp registro;
  private String medio;

  public Abono(Double monto, Timestamp registro, String medio) {
    this.monto = monto;
    this.registro = registro;
    this.medio = medio;
  }

  public Double getMonto() {
    return monto;
  }

  public Timestamp getRegistro() {
    return registro;
  }

  public String getMedio() {
    return medio;
  }

  @Override
  public String toString() {
    return "Abono{" + "monto=" + monto + ", registro=" + registro + ", medio=" + medio + '}';
  }
  
}
