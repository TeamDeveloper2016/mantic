package mx.org.kaana.mantic.productos.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.db.dto.TcManticProductosDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/12/2021
 *@time 10:12:30 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class General extends TcManticProductosDto implements Serializable {

  private static final long serialVersionUID = -90581327958862136L;

  private String categoria;
  private String marca;
  private String archivo;

  public General() {
    this("", "", "");
  }

  public General(String categoria, String marca, String archivo) {
    this.categoria = categoria;
    this.marca = marca;
    this.archivo = archivo;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getArchivo() {
    return archivo;
  }

  public void setArchivo(String archivo) {
    this.archivo = archivo;
  }
  
  @Override
  public Class toHbmClass() {
    return TcManticProductosDto.class;
  }

}
