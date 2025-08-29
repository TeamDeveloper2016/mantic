package mx.org.kaana.mantic.solicitudes.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;

@Named(value = "manticSolicitudesFiltro")
@ViewScoped
public class Filtro extends mx.org.kaana.mantic.catalogos.almacenes.transferencias.backing.Filtro implements Serializable {

  private static final long serialVersionUID = 8793661741599428879L;
  
  @Override
	protected void toLoadTransferenciasTipos() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
			params.put(Constantes.SQL_CONDICION, "id_transferencia_tipo= 4");
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("tipos", (List<UISelectEntity>) UIEntity.build("TcManticTransferenciasTiposDto", "row", params, columns));
			this.attrs.put("idTransferenciaTipo", new UISelectEntity("-1"));
    } // try
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
  
}
