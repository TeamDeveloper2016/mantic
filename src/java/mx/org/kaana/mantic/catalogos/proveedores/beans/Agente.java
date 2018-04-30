/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.proveedores.beans;

import java.io.Serializable;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticProveedoresAgentesDto;

public class Agente extends TrManticProveedoresAgentesDto implements Serializable, IBaseAction {

  private static final long serialVersionUID = 7516634619157750816L;

  private ESql accion;

  public Agente() {
    this(ESql.UPDATE);
  }

  public Agente(ESql accion) {
    this.accion = accion;
  }

  @Override
  public ESql getAccion() {
    return this.accion;
  }

}
