package mx.org.kaana.mantic.inventarios.entradas.reglas;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.ESql.DELETE;
import static mx.org.kaana.kajool.enums.ESql.INSERT;
import static mx.org.kaana.kajool.enums.ESql.SELECT;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticEgresosBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticEgresosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasArchivosDto;
import mx.org.kaana.mantic.egresos.beans.IEgresos;
import mx.org.kaana.mantic.inventarios.entradas.beans.NotaEntrada;
import org.apache.log4j.Logger;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Importados extends Transaccion implements Serializable {

  private static final Logger LOG = Logger.getLogger(Importados.class);
	private static final long serialVersionUID=-6063204157451117549L;
  
  private List<IEgresos> articulos;
  private Entity documento;
  
  public Importados(Entity documento) throws Exception {
    super((NotaEntrada)DaoFactory.getInstance().toEntity(NotaEntrada.class, "TcManticNotasEntradasDto", "igual", Variables.toMap("idNotaEntrada~"+ documento.toLong("idNotaEntrada"))));
    this.documento= documento;  
  }
  
  public Importados(List<IEgresos> articulos) throws Exception {
    super(new NotaEntrada());
    this.articulos= articulos;
  }
          
	public Importados(NotaEntrada orden, Importado xml, Importado pdf) {
		super(orden, Collections.EMPTY_LIST, true, xml, pdf);
	} // Transaccion
  
	public Importados(NotaEntrada orden, Importado xml, Importado pdf, Importado jpg) {
		super(orden, Collections.EMPTY_LIST, true, xml, pdf, jpg);
	} // Transaccion

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			this.setMessageError("Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" al importar el archivo."));
			switch(accion) {
				case AGREGAR:
     	    this.toUpdateDeleteXml(sesion);	
          regresar= this.toCheckEstatus(sesion);
          this.toCheckDocumentoEgreso(sesion, this.orden.getIdNotaEntrada());
					break;
				case PROCESAR:
          regresar= this.toUpdatePartidas(sesion);
					break;
				case MOVIMIENTOS:
          this.toDeleteDocumento(sesion);
          regresar= this.toCheckEstatus(sesion);
          this.toCheckDocumentoEgreso(sesion, this.documento.toLong("idNotaEntrada"));
					break;
			} // switch
		} // try
		catch (Exception e) {
			regresar= false;
      Error.mensaje(e);			
			throw new Exception(this.getMessageError().concat("\n\n")+ e.getMessage());
		} // catch		
		LOG.info("Se importaron de forma correcta los archivos !");
		return regresar;
	}	// ejecutar

  private Boolean toCheckEstatus(Session sesion) throws Exception {
    Boolean regresar= false;
    Map<String, Object> params = null;
    try {      
      params= new HashMap<>();  
      params.put("sortOrder", "");
      params.put("idEmpresa", this.orden.getIdEmpresa());
      params.put("idProveedor", this.orden.getIdProveedor());
      params.put(Constantes.SQL_CONDICION, "tc_mantic_notas_entradas.id_nota_entrada= "+ this.orden.getIdNotaEntrada());
      // FALTA VERIFICAR QUE EN LOS DOCUMENTOS DE LA FACTURA REALMENTE SE TENGAN UN XML Y EL PDF DE LA FACTURA
      boolean factura= this.checkTwoDocumentos(sesion, this.orden.getIdNotaEntrada());
      Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "VistaEmpresasDto", "documentos", params);
      if(entity!= null && !entity.isEmpty()) {
        Long idCompleto= 1L; // INICIAL
        if(factura && entity.toLong("facturas")> 0 && entity.toLong("comprobantes")> 0 && entity.toLong("vouchers")> 0)
          idCompleto= 3L; // COMPLETO
        else
          if(entity.toLong("facturas")> 0 || entity.toLong("comprobantes")> 0 || entity.toLong("vouchers")> 0)
            idCompleto= 2L; // INCOMPLETO
        params.put("idNotaEntrada", this.orden.getIdNotaEntrada());
        TcManticEmpresasDeudasDto item= (TcManticEmpresasDeudasDto)DaoFactory.getInstance().toEntity(sesion, TcManticEmpresasDeudasDto.class, "TcManticEmpresasDeudasDto", "unica", params);
        if(item!= null) {
          item.setIdCompleto(idCompleto);
          DaoFactory.getInstance().update(sesion, item);
        } // if
      } // if
      sesion.flush();
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private boolean toDeleteDocumento(Session sesion) throws Exception {
    boolean regresar= Boolean.FALSE;
    try {      
      TcManticNotasArchivosDto item= (TcManticNotasArchivosDto)DaoFactory.getInstance().findById(sesion, TcManticNotasArchivosDto.class, this.documento.getKey());
      if(item!= null) {
        item.setIdEliminado(Objects.equals(item.getIdEliminado(), 1L)? 2L: 1L);
        regresar= DaoFactory.getInstance().update(sesion, item)> 0L;
        this.documento.getValue("idEliminado").setData(item.getIdEliminado());
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
  private boolean checkTwoDocumentos(Session sesion, Long idNotaEntrada) throws Exception {
    boolean regresar= Boolean.FALSE;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idNotaEntrada", idNotaEntrada);      
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet(sesion, "TcManticNotasArchivosDto", "all", params);
      int count= 0;
      for (Entity item : items) {
        if(Objects.equals(item.toLong("idTipoDocumento"), 13L)) {
          if(Objects.equals(item.toLong("idTipoArchivo"), 1L) && Objects.equals(item.toLong("idEliminado"), 2L)) // XML
            count++;
          if(Objects.equals(item.toLong("idTipoArchivo"), 2L) && Objects.equals(item.toLong("idEliminado"), 2L)) // PDF
            count++;
        } // if  
      } // for
      regresar= count> 1;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private Boolean toUpdatePartidas(Session sesion) throws Exception {
    Long idPivoteNota         = -1L;
    Long idPivoteEgreso       = -1L;
    Boolean regresar          = false;
    Map<String, Object> params= null;
    try {      
      params= new HashMap<>();  
      for (IEgresos item: this.articulos) {
        switch(item.getAccion()) {
          case SELECT:
            break;
          case INSERT:
            DaoFactory.getInstance().insert(sesion, (IBaseDto)item);
            break;
          case DELETE:
            DaoFactory.getInstance().delete(sesion, (IBaseDto)item);
            break;
        } // switch
      } // for
      sesion.flush();
      // VERIFICAR EL ESTATUS POR CUENTA X PAGAR
      for (IEgresos item: this.articulos) {
        switch(item.getAccion()) {
          case SELECT:
            break;
          case INSERT:
          case DELETE:
            if(Objects.equals(idPivoteNota, -1L) || !Objects.equals(idPivoteNota, item.getIdNotaEntrada())) {
              this.orden= (NotaEntrada)DaoFactory.getInstance().findById(NotaEntrada.class, item.getIdNotaEntrada());
              this.toCheckEstatus(sesion);
              idPivoteNota= item.getIdNotaEntrada();
            } // if  
            if(Objects.equals(idPivoteEgreso, -1L) || !Objects.equals(idPivoteEgreso, item.getIdEgreso())) { 
              this.toCheckEstatusGlobal(sesion, item.getIdEgreso());
              idPivoteEgreso= item.getIdEgreso();
            } // if  
            break;
        } // switch
      } // if
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private Boolean toCheckEstatusGlobal(Session sesion, Long idEgreso) throws Exception {
    Boolean regresar          = false;
    Map<String, Object> params= null;
    try {      
      params= new HashMap<>();  
      params.put("idEgreso", idEgreso);      
      Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "VistaEgresosDto", "documentos", params);
      Long idEgresoEstatus= 1L;
      if(entity!= null && !entity.isEmpty() && Objects.equals(entity.toLong("total"), entity.toLong("completos")))
        idEgresoEstatus= 3L;
      else
        if(entity!= null && !entity.isEmpty() && entity.toLong("completos")> 0)
          idEgresoEstatus= 2L;
      TcManticEgresosDto item= (TcManticEgresosDto)DaoFactory.getInstance().toEntity(sesion, TcManticEgresosDto.class, "TcManticEgresosDto", "detalle", params);
      if(item!= null) {
        TcManticEgresosBitacoraDto bitacora= new TcManticEgresosBitacoraDto("CAMBIO AUTOMATICO", idEgresoEstatus, item.getIdEgreso(), JsfBase.getIdUsuario(), -1L);
        DaoFactory.getInstance().insert(sesion, bitacora);
        item.setIdEgresoEstatus(idEgresoEstatus);
        DaoFactory.getInstance().update(sesion, item);
      } // if
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private Boolean toCheckDocumentoEgreso(Session sesion, Long idNotaEntrada) throws Exception {
    Boolean regresar= false;
    Map<String, Object> params = null;
    try {      
      params= new HashMap<>();  
      params.put("idNotaEntrada", idNotaEntrada);
      List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet(sesion, "TcManticEgresosNotasDto", "notas", params);
      if(items!= null && !items.isEmpty()) {
        for (Entity item: items) {
          this.toCheckEstatusGlobal(sesion, item.toLong("idEgreso"));
        } // for
      } // if  
      regresar= true;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
} 