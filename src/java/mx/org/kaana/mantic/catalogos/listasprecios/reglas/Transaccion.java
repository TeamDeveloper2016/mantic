package mx.org.kaana.mantic.catalogos.listasprecios.reglas;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.catalogos.backing.Monitoreo;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDto;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {
  
  private static final Log LOG = LogFactory.getLog(Transaccion.class);
  private IBaseDto dto;
  private TcManticListasPreciosDto lista;	
	private Importado xls;
	private Importado pdf;
	private String messageError;
	private List<TcManticListasPreciosDetallesDto> articulos;
  
  public Transaccion(IBaseDto dto) {
    this.dto = dto;
    this.lista = (TcManticListasPreciosDto) dto;
  }
  
  public Transaccion(TcManticListasPreciosDto lista, List<TcManticListasPreciosDetallesDto> articulos, Importado xls, Importado pdf) {
		this.lista    = lista;		
		this.articulos= articulos;
		this.xls      = xls;
		this.pdf      = pdf;
	} // Transaccion

	protected void setMessageError(String messageError) {
		this.messageError=messageError;
	}

	public String getMessageError() {
		return messageError;
	}
  
	@Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception{
    boolean regresar= false;
    try {
      this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" articulos a la lista de precios.");
      switch (accion) {
				case COMPLEMENTAR: 
					if(this.lista.isValid())
						regresar= DaoFactory.getInstance().update(sesion, this.lista)> 0L;
					else
						regresar= DaoFactory.getInstance().insert(sesion, this.lista)> 0L;
					if(regresar) {
						sesion.flush();
						this.toUpdateXls(sesion);
						regresar= true;
					} // if
					break;
				case ELIMINAR: 
					this.toDeleteXmlPdf();
					regresar = DaoFactory.getInstance().deleteAll(sesion, TcManticListasPreciosDetallesDto.class, this.lista.toMap())>-1L;
					sesion.flush();
					regresar = DaoFactory.getInstance().deleteAll(sesion, TcManticListasPreciosArchivosDto.class, this.lista.toMap())>-1L;
					sesion.flush();
					regresar = DaoFactory.getInstance().delete(sesion, this.lista)>-1L;
					sesion.flush();
					break;
      } // swtich 
      if (!regresar) {
        throw new Exception(messageError);
      } // if
    } // tyr
		catch (Exception e) {
      throw new Exception(messageError.concat("<br/>") + e.getMessage());
    } // catch
    return regresar;
  }
  
	protected void toUpdateXls(Session sesion) throws Exception {
    TcManticListasPreciosArchivosDto tmp= null;
		Map<String, Object> params=null;
    int count = 0;
		try {
			params=new HashMap<>();
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put("idProveedor", this.lista.getIdProveedor());
			params.put("idListaPrecio", this.lista.getIdListaPrecio());
			if(this.lista.getIdListaPrecio()!= -1L) {
				if(this.xls!= null) {
					tmp= new TcManticListasPreciosArchivosDto(
						this.lista.getIdListaPrecio(),
						this.xls.getRuta(),
						this.xls.getFileSize(),
						JsfBase.getIdUsuario(),
						8L,
						this.xls.getObservaciones(),
						1L,
						Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios").concat(this.xls.getRuta()).concat(this.xls.getName()),
						-1L,
						this.xls.getName()
					);
					params.put("idTipoArchivo", 8);
					DaoFactory.getInstance().updateAll(sesion, TcManticListasPreciosArchivosDto.class, params);
					DaoFactory.getInstance().deleteAll(sesion, TcManticListasPreciosDetallesDto.class, params);
					File file= new File(tmp.getAlias());
					if(file.exists()) 
						DaoFactory.getInstance().insert(sesion, tmp);
					else
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
					sesion.flush();
					Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
					monitoreo.comenzar(0L);
					monitoreo.setTotal(Long.valueOf(this.articulos.size()));
					for(TcManticListasPreciosDetallesDto articulo:this.articulos){
						articulo.setIdListaPrecio(this.lista.getIdListaPrecio());
						DaoFactory.getInstance().insert(sesion, articulo);
						monitoreo.incrementar();
						count++;
						if(count% 1000== 0)
							sesion.flush();
					} // for
					monitoreo.terminar();
					monitoreo.setProgreso(0L);
					this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios").concat(this.xls.getRuta()), ".".concat(this.xls.getFormat().name()), this.toListFile(sesion, this.xls, 8L));
				} // if	
				if(this.pdf!= null) {
					tmp= new TcManticListasPreciosArchivosDto(
						this.lista.getIdListaPrecio(),
						this.pdf.getRuta(),
						this.pdf.getFileSize(),
						JsfBase.getIdUsuario(),
						2L,
						this.pdf.getObservaciones(),
						1L,
						Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios").concat(this.pdf.getRuta()).concat(this.pdf.getName()),
						-1L,
						this.pdf.getName()
					);
					params.put("idTipoArchivo", 2);
					DaoFactory.getInstance().updateAll(sesion, TcManticListasPreciosArchivosDto.class, params);
					DaoFactory.getInstance().insert(sesion, tmp);
					sesion.flush();
					this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
				} // if	
			} // if	
		} // try
		finally {
			Methods.clean(params);
		} // finally
	}

	public void toDeleteXmlPdf() throws Exception {
		List<TcManticListasPreciosArchivosDto> list= (List<TcManticListasPreciosArchivosDto>)DaoFactory.getInstance().findViewCriteria(TcManticListasPreciosArchivosDto.class, this.lista.toMap(), "all");
		if(list!= null)
			for (TcManticListasPreciosArchivosDto item: list) {
				LOG.info("Lista archivo: "+ this.lista.getIdListaPrecio().toString()+ " delete file: "+ item.getAlias());
				File file= new File(item.getAlias());
				file.delete();
			} // for
	}	
  
	private void toDeleteAll(String path, String type, List<Nombres> listado) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(path), type.toLowerCase());
    if(fileSearch.getResult().size()> 0)
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
				if(listado.indexOf(new Nombres(name))< 0) {
          LOG.warn("Lista de precios: "+ this.lista.getNombre()+ " delete file: ".concat(matched));
				  File file= new File(matched);
				  file.delete();
				} // if
      } // for
	}
	
	private List<Nombres> toListFile(Session sesion, Importado tmp, Long idTipoArchivo) throws Exception {
		List<Nombres> regresar= null;
		Map<String, Object> params=null;
		try {
			params  = new HashMap<>();
			params.put("idTipoArchivo", idTipoArchivo);
			params.put("ruta", tmp.getRuta());
			regresar= (List<Nombres>)DaoFactory.getInstance().toEntitySet(sesion, Nombres.class, "TcManticListasPreciosArchivosDto", "listado", params);
			regresar.add(new Nombres(tmp.getName()));
		} // try 
		catch (Exception e) {
			mx.org.kaana.libs.formato.Error.mensaje(e);
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} 
	
}