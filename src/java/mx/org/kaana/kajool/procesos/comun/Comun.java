package mx.org.kaana.kajool.procesos.comun;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.EPerfiles;
import mx.org.kaana.kajool.procesos.acceso.beans.Autentifica;
import mx.org.kaana.libs.pagina.IBaseFilter;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 9/10/2016
 *@time 08:41:12 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public abstract class Comun extends IBaseFilter implements Serializable{

  private static final long serialVersionUID      = -8002497139187570127L;
  protected static final String CONFIG_CAPTURISTA = "idPerfil.capturista";
  protected static final String CONFIG_RESPONSABLE= "idPerfil.responsableEstatal";
  protected static final Long COORDINACION_VACIA  = -1L;
  protected List<UISelectItem> entidades;	
  protected UISelectEntity entidad;				

  public List<UISelectItem> getEntidades() {
    return entidades;
  }

  public Comun(){
    Autentifica auntentifica= null;
    try {
      auntentifica= JsfBase.getAutentifica();
      this.attrs.put("idGrupo", auntentifica.getEmpleado().getIdGrupo());
      this.attrs.put("claveGrupo", auntentifica.getEmpleado().getClaveGrupo());      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
  } // Comun

  protected void loadEntidades(){		
    try {			
			if(JsfBase.getAutentifica().getEmpleado().getIdPerfil().equals(EPerfiles.CAPTURISTA.getIdPerfil()) ||
				 JsfBase.getAutentifica().getEmpleado().getIdPerfil().equals(EPerfiles.RESPONSABLE_ESTATAL.getIdPerfil())) 
			  this.attrs.put(Constantes.SQL_CONDICION, "tc_janal_entidades.id_entidad=".concat(JsfBase.getAutentifica().getEmpleado().getIdEntidad().toString()));			
			else
				this.attrs.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);			
			this.entidades= UISelect.build("VistaCargasTrabajoDto", "cargasTrabajo", this.attrs, "descripcion", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS);				
      this.attrs.put("entidad", UIBackingUtilities.toFirstKeySelectItem(this.entidades));
    } // try
    catch (Exception e) {
      throw e;
    } // catch		
  } // loadEntidades
  
}
