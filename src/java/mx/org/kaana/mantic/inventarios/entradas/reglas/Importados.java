package mx.org.kaana.mantic.inventarios.entradas.reglas;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import org.hibernate.Session;
import mx.org.kaana.kajool.enums.EAccion;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.libs.factura.beans.ComprobanteFiscal;
import mx.org.kaana.mantic.libs.factura.reglas.Reader;
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
  
	public Importados(TcManticNotasEntradasDto orden, Importado xml, Importado pdf) {
		super(orden, Collections.EMPTY_LIST, true, xml, pdf);
	} // Transaccion
  
	public Importados(TcManticNotasEntradasDto orden, Importado xml, Importado pdf, Importado jpg) {
		super(orden, Collections.EMPTY_LIST, true, xml, pdf, jpg);
	} // Transaccion

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= true;
		try {
			this.setMessageError("Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" al importar el archivo."));
			switch(accion) {
				case AGREGAR:
     	    this.toUpdateDeleteXml(sesion);	
          this.checkEstatus(sesion);
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

  private void checkEstatus(Session sesion) throws Exception {
    Map<String, Object> params = null;
    try {      
      params= new HashMap<>();  
      params.put("sortOrder", "");
      params.put("idEmpresa", this.orden.getIdEmpresa());
      params.put("idProveedor", this.orden.getIdProveedor());
      params.put(Constantes.SQL_CONDICION, "tc_mantic_notas_entradas.id_nota_entrada= "+ this.orden.getIdNotaEntrada());
      if(Cadena.isVacio(this.orden.getFactura()) && this.xml!= null && Objects.equals(this.xml.getIdTipoDocumento(), 13L)) {
        File file= new File(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").concat(this.xml.getRuta()).concat(this.xml.getName()));
        Reader reader= new Reader(file.getAbsolutePath());
        ComprobanteFiscal factura = reader.execute();
        this.orden.setFactura(factura.getFolio());
        this.orden.setFechaFactura(Fecha.toDateDefault(factura.getFecha()));
        this.orden.setOriginal(Numero.toRedondearSat(Double.parseDouble(factura.getTotal())));
        DaoFactory.getInstance().update(sesion, this.orden);
      } // if
      Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "VistaEmpresasDto", "documentos", params);
      if(entity!= null && !entity.isEmpty()) {
        Long idCompleto= 1L;
        if(entity.toLong("facturas")> 0 && entity.toLong("comprobantes")> 0 && entity.toLong("vouchers")> 0)
          idCompleto= 3L;
        else
          if(entity.toLong("facturas")> 0 || entity.toLong("comprobantes")> 0 || entity.toLong("vouchers")> 0)
            idCompleto= 2L;
        params.put("idNotaEntrada", this.orden.getIdNotaEntrada());
        TcManticEmpresasDeudasDto item= (TcManticEmpresasDeudasDto)DaoFactory.getInstance().toEntity(sesion, TcManticEmpresasDeudasDto.class, "TcManticEmpresasDeudasDto", "unica", params);
        if(item!= null) {
          item.setIdCompleto(idCompleto);
          DaoFactory.getInstance().update(sesion, item);
        } // if
      } // if
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
  }
  
} 