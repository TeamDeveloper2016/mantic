package mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticEmpresasPagosDto;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx{

	private String messageError;
	private TcManticEmpresasPagosDto pago;
	private TcManticEmpresasDeudasDto deuda;
	private Importado xml;
	private Importado pdf;
	private Long idPago;
	
	public Transaccion(TcManticEmpresasPagosDto pago) {
		this.pago= pago;
	} // Transaccion	

	public Transaccion(TcManticEmpresasDeudasDto deuda, Importado xml, Importado pdf, Long idPago) {
		this.deuda = deuda;
		this.pdf   = pdf;
		this.xml   = xml;
		this.idPago= idPago;
	} // Transaccion
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar = false;
    try {			
      switch (accion) {
        case AGREGAR:
          regresar = procesarPago(sesion);
          break;       
				case REGISTRAR:
					regresar= true;
					toUpdateDeleteXml(sesion);
					break;
      } // switch
      if (!regresar) 
        throw new Exception("");      
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("\n\n")+ e.getMessage());
    } // catch		
    return regresar;
	} // ejecutar
	
	private boolean procesarPago(Session sesion) throws Exception{
		boolean regresar               = false;
		TcManticEmpresasDeudasDto deuda= null;
		Double saldo                   = 0D;
		try {
			if(DaoFactory.getInstance().insert(sesion, this.pago)>= 1L){
				deuda= (TcManticEmpresasDeudasDto) DaoFactory.getInstance().findById(sesion, TcManticEmpresasDeudasDto.class, this.pago.getIdEmpresaDeuda());
				saldo= deuda.getSaldo() - this.pago.getPago();
				deuda.setSaldo(saldo);
				deuda.setIdEmpresaEstatus(saldo.equals(0L) ? 3L : 2L);
				regresar= DaoFactory.getInstance().update(sesion, deuda)>= 1L;
			} // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally{
			this.messageError= "Error al registrar el pago";
		} // finally
		return regresar;
	} // procesarPago
	
	protected void toUpdateDeleteXml(Session sesion) throws Exception {
		TcManticEmpresasArchivosDto tmp= null;
		if(this.deuda.getIdEmpresaDeuda()!= -1L) {
			if(this.xml!= null) {
				tmp= new TcManticEmpresasArchivosDto(
					this.xml.getRuta(),
					-1L,
					this.xml.getName(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					this.xml.getFileSize(),
					JsfBase.getIdUsuario(),	
					1L,
					1L,
					this.xml.getObservaciones(),
					this.idPago,																					
					Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.xml.getRuta()).concat(this.xml.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1)					
				);
				TcManticEmpresasArchivosDto exists= (TcManticEmpresasArchivosDto)DaoFactory.getInstance().toEntity(TcManticEmpresasArchivosDto.class, "TcManticEmpresasArchivosDto", "identically", tmp.toMap());
				if(exists== null) {
					DaoFactory.getInstance().updateAll(sesion, TcManticEmpresasArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				sesion.flush();
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.xml.getRuta()), ".".concat(this.xml.getFormat().name()), this.toListFile(sesion, this.xml, 1L));
			} // if	
			if(this.pdf!= null) {
				tmp= new TcManticEmpresasArchivosDto(
					this.xml.getRuta(),
					-1L,
					this.xml.getName(),
					new Long(Calendar.getInstance().get(Calendar.YEAR)),
					this.xml.getFileSize(),
					JsfBase.getIdUsuario(),	
					2L,
					1L,
					this.xml.getObservaciones(),
					this.idPago,																					
					Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.xml.getRuta()).concat(this.xml.getName()),
					new Long(Calendar.getInstance().get(Calendar.MONTH)+ 1)					
				);
				TcManticEmpresasArchivosDto exists= (TcManticEmpresasArchivosDto)DaoFactory.getInstance().toEntity(TcManticEmpresasArchivosDto.class, "TcManticEmpresasArchivosDto", "identically", tmp.toMap());
				if(exists== null) {
					DaoFactory.getInstance().updateAll(sesion, TcManticEmpresasArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				sesion.flush();
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("pagos").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
  	} // if	
	}

	public void toDeleteXmlPdf() throws Exception {
		List<TcManticEmpresasArchivosDto> list= (List<TcManticEmpresasArchivosDto>)DaoFactory.getInstance().findViewCriteria(TcManticEmpresasArchivosDto.class, this.deuda.toMap(), "all");
		if(list!= null)
			for (TcManticEmpresasArchivosDto item: list) {
				File file= new File(item.getAlias());
				file.delete();
			} // for
	}	// if	
	
	private List<Nombres> toListFile(Session sesion, Importado tmp, Long idTipoArchivo) throws Exception {
		List<Nombres> regresar= null;
		Map<String, Object> params=null;
		try {
			params  = new HashMap<>();
			params.put("idTipoArchivo", idTipoArchivo);
			params.put("ruta", tmp.getRuta());
			regresar= (List<Nombres>)DaoFactory.getInstance().toEntitySet(sesion, Nombres.class, "TcManticEmpresasArchivosDto", "listado", params);
			regresar.add(new Nombres(tmp.getName()));
		} // try  // try 
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toListFile
	
	private void toDeleteAll(String path, String type, List<Nombres> listado) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(path), type.toLowerCase());
    if(fileSearch.getResult().size()> 0){
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
				if(listado.indexOf(new Nombres(name))< 0) {
				  File file= new File(matched);
				  file.delete();
				} // if
      } // for
		} // if
	} // toDeleteAll
}
