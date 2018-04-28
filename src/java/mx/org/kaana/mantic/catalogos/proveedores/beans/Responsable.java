/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.proveedores.beans;



import java.io.Serializable;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.mantic.db.dto.TrManticProveedorPersonaDto;

public class Responsable  extends TrManticProveedorPersonaDto  implements Serializable,IBaseAction {
  
  private static final long serialVersionUID = 7516634619157750816L;
  
   private ESql accion;
   
   public Responsable() {
     this.accion=ESql.UPDATE;
   }

  @Override
  public ESql getAccion() {
     return this.accion;
  }
  
}
