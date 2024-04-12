package mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.enums.ESql;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 11/04/2024
 *@time 04:49:30 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Umbral implements IBaseDto, Serializable {

  private static final long serialVersionUID = 7966297167249143264L;
  
  private Long idAlmacen;
  private Long idArticulo;
  private String codigo;
  private String nombre;
  private Double minimo;
  private Double maximo;
  private ESql action;     
  private Long idVerificado;

  public Umbral() {
    this.action= ESql.SELECT;
  }

  public Long getIdArticulo() {
    return idArticulo;
  }

  public void setIdArticulo(Long idArticulo) {
    this.idArticulo = idArticulo;
  }

  public Long getIdAlmacen() {
    return idAlmacen;
  }

  public void setIdAlmacen(Long idAlmacen) {
    this.idAlmacen = idAlmacen;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Double getMinimo() {
    return minimo;
  }

  public void setMinimo(Double minimo) {
    this.minimo = minimo;
  }

  public Double getMaximo() {
    return maximo;
  }

  public void setMaximo(Double maximo) {
    this.maximo = maximo;
  }

  public ESql getAction() {
    return action;
  }

  public void setAction(ESql action) {
    this.action = action;
  }

  public Long getIdVerificado() {
    return idVerificado;
  }

  public void setIdVerificado(Long idVerificado) {
    this.idVerificado = idVerificado;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.idArticulo);
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
    final Umbral other = (Umbral) obj;
    if (!Objects.equals(this.idArticulo, other.idArticulo)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Umbral{" + "idArticulo=" + idArticulo + ", idAlmacen=" + idAlmacen + ", codigo=" + codigo + ", nombre=" + nombre + ", minimo=" + minimo + ", maximo=" + maximo + ", action=" + action + ", idVerificado=" + idVerificado + '}';
  }

  @Override
  public Long getKey() {
    return this.idArticulo;
  }

  @Override
  public void setKey(Long key) {
    this.idArticulo= key;
  }

  @Override
  public Map<String, Object> toMap() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public boolean isValid() {
    return Objects.equals(this.idArticulo, -1L);
  }

  @Override
  public Object toValue(String name) {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public String toAllKeys() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public String toKeys() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public Class toHbmClass() {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
}
