package mx.org.kaana.kajool.procesos.mantenimiento.indicadores.avance.reglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAmbitos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.directivos.beans.Columna;
import mx.org.kaana.kajool.procesos.mantenimiento.indicadores.directivos.beans.Registro;


/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 30, 2014
 *@time 11:10:18 AM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Consultas {

  private List<Registro> toTotales(EAmbitos ambito, List<Entity> registros, String etiquetaIndicador, String estatusAplican, Map params) throws Exception {
    List<Registro> avance    = new ArrayList<Registro>();
    List<Columna> columnas  = null;
    Long total              = 0L;
    Map<Long, String> cortes= new HashMap();
    Double valorIndicador   = 0.0D;
    switch (ambito) {
      case MUNICIPIO:
      case ESTATAL:
      case REGIONAL:
      case LOCALIDAD:
			case OFICINA:
        for (Entity registro: registros) {
          cortes.put(registro.toLong("idKey"), registro.toString("corte"));
        } // for
        break;
      default:
        cortes.put(1L, "NACIONAL");
        break;
    } // swicth
    for (Long corte: cortes.keySet()) {
      columnas              = new ArrayList<Columna>();
      total= 0L;
      valorIndicador= 0.0D;
      for (Entity entity: registros) {
        if (entity.getKey().equals(corte)) {
          columnas.add(new Columna(Cadena.letraCapital(entity.toString("descripcion")), entity.toLong("total"), entity.toDouble("totalPorcentaje")));
        } // if
      } // for
      for (Columna columna: columnas) {
        if (columna.getEtiqueta().toLowerCase().equals(etiquetaIndicador.toLowerCase()))
          valorIndicador= columna.getPorcentaje();
      } // for
      avance.add(new Registro(corte, cortes.get(corte), ambito, total, columnas, valorIndicador));
    } // for
    return avance;
  }

  private List<Entity> obtenerValores(Map params, String idConsulta) throws Exception {
    List<Entity> regresar= null;
    try {
      regresar= DaoFactory.getInstance().toEntitySet("VistaIndicadoresDirectivos", idConsulta, params, -1L);
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

  public List<Registro> toValores(Map params, EAmbitos ambito, String idConsulta, String etiquetaIndicador, String estatusAplican){
    List<Registro> regresar= null;
    try {
      regresar= toTotales(ambito, obtenerValores(params, idConsulta), etiquetaIndicador, estatusAplican, params);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  private void recalcularPorcentajes(Long total, List<Columna> columnas, Long totalHijos){
    for(Columna columna: columnas) {
      columna.setPorcentaje(Numero.getDouble(Numero.redondea((columna.getPorcentaje())/ totalHijos, 2)));
    } // for
  }

  public Long toTotalHijos(Map params, String idConsulta) throws Exception {
    Long regresar= -1L;
    try {
      regresar= DaoFactory.getInstance().toField("VistaIndicadoresDirectivos", idConsulta, params).toLong();
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    return regresar;
  }

}
