package mx.org.kaana.mantic.archivos.beans;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.mantic.db.dto.TcManticDocumentosDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 20/06/2022
 *@time 03:13:46 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Referencia extends TcManticDocumentosDto implements Serializable {

  private static final long serialVersionUID = 3027054165774572203L;
  
  private Long id;

  public Referencia() {
    this(new Random().nextLong());
  }

  public Referencia(Long id) {
    this(id, "", "", "", "", "", "", "", "", "");
  }
  
  public Referencia(String folio, String rfc, String emisor, String uuid, String fecha, String tipo, String total, String receptor, String archivo) {
    this(new Random().nextLong(), folio, rfc, emisor, uuid, fecha, tipo, total, receptor, archivo);
  }
  
  public Referencia(Long id, String folio, String rfc, String emisor, String uuid, String fecha, String tipo, String total, String receptor, String archivo) {
    this.id = id;
    this.setFolio(folio);
    this.setRfc(rfc);
    this.setEmisor(emisor);
    this.setUuid(uuid);
    this.setFecha(fecha);
    this.setTipo(tipo);
    this.setTotal(total);
    this.setReceptor(receptor);
    this.setArchivo(archivo);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 61 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Referencia other = (Referencia) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Referencia{" + "id=" + id + ", "+ super.toString() + '}';
  }

  @Override
  public Class toHbmClass() {
    return TcManticDocumentosDto.class;
  }
  
}
