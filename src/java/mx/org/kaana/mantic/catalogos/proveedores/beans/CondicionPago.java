/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.proveedores.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticProveedorPagoDto;


public class CondicionPago  extends TrManticProveedorPagoDto implements Serializable  {

  private static final long serialVersionUID = 1814811165126557004L;
  
  private ESql accion;
  
  public CondicionPago () {
    this.accion = ESql.UPDATE;
  }

  public ESql getAccion() {
    return accion;
  } 
  
  
}
