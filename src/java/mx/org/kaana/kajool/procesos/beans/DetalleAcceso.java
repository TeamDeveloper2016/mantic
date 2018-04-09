package mx.org.kaana.kajool.procesos.beans;

import java.io.Serializable;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 14/10/2016
 *@time 01:03:47 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DetalleAcceso implements Serializable {

  private static final long serialVersionUID = -6141669220915073052L;
  private Long id;
  private Long total;
	private Long totalUsuarios;
  private String descripcion;

  public DetalleAcceso(Long id, Long total, String descripcion) {
		this(id, total, 0L, descripcion);
  }

  public DetalleAcceso(Long id, Long total, Long totalUsuarios, String descripcion) {
    this.id           = id;
    this.total        = total;
    this.totalUsuarios= totalUsuarios;
    this.descripcion  = descripcion;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id= id;
  }

  public Long getTotal() {
    return total;
  }

  public void setTotal(Long total) {
    this.total= total;
  }

  public Long getTotalUsuarios() {
    return totalUsuarios;
  }

  public void setTotalUsuarios(Long totalUsuarios) {
    this.totalUsuarios= totalUsuarios;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion= descripcion;
  }
}
