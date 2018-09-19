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
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDetallesDto;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDto;
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
  
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception{
    boolean regresar          = false;
    Map<String, Object> params= null;
    try {
      if(this.lista!= null) {
				params= new HashMap<>();
				params.put("idListaPrecio", this.lista.getIdListaPrecio());
			} // if
      this.messageError= "Ocurrio un error en ".concat(accion.name().toLowerCase()).concat(" articulos a la lista de precios.");
      switch (accion) {
      case AGREGAR: 
        regresar = procesarListaProveedor(sesion);
        break;
      case COMPLEMENTAR: 
        if(procesarListaProveedor(sesion)){
          sesion.flush();
          this.toUpdateXls(sesion);
          regresar = true;
        }
        break;
      case ELIMINAR: 
        this.toDeleteXmlPdf();
        regresar = DaoFactory.getInstance().deleteAll(sesion, TcManticListasPreciosDetallesDto.class, params)>-1L;
        sesion.flush();
        regresar = DaoFactory.getInstance().deleteAll(sesion, TcManticListasPreciosArchivosDto.class, params)>-1L;
        sesion.flush();
        regresar = DaoFactory.getInstance().delete(sesion, this.lista)>-1L;
        sesion.flush();
        break;
      }
      if (!regresar) {
        throw new Exception(messageError);
      }
    }
    catch (Exception e) {
      throw new Exception(messageError.concat("\n\n") + e.getMessage());
    }
    return regresar;
  }
  
  private boolean procesarListaProveedor(Session sesion) throws Exception {
    boolean regresar = false;
    try {
     if(this.lista.getKey() != -1L)
      regresar = DaoFactory.getInstance().update(sesion, lista) > 0L;
    else
      regresar = DaoFactory.getInstance().insert(sesion, lista) > 0L;
    }
    catch (Exception e) {
      throw e;
    }
    return regresar;
  }
  
	protected void toUpdateXls(Session sesion) throws Exception {
		//(idListaPrecio,ruta,tamanio,idUsuario,idTipoArchivo,observaciones,idPrincipal,alias,idListaPrecioArchivo,nombre)
    TcManticListasPreciosArchivosDto tmp= null;
    int i=0;
    int reg = 0;
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
				DaoFactory.getInstance().updateAll(sesion, TcManticListasPreciosArchivosDto.class, tmp.toMap());
        DaoFactory.getInstance().deleteAll(sesion, TcManticListasPreciosDetallesDto.class, tmp.toMap());
        DaoFactory.getInstance().insert(sesion, tmp);
        sesion.flush();
        Monitoreo monitoreo= JsfBase.getAutentifica().getMonitoreo();
        monitoreo.comenzar(0L);
        monitoreo.setTotal(Long.valueOf(this.articulos.size()));
        for(TcManticListasPreciosDetallesDto articulo:this.articulos){
          articulo.setIdListaPrecio(this.lista.getIdListaPrecio());
          DaoFactory.getInstance().insert(sesion, articulo);
          monitoreo.setProgreso((long)(reg* 100/ monitoreo.getTotal()));
          monitoreo.incrementar();
          i++;
          reg++;
          if(i==1000){
            sesion.flush();
            i=0;
          }
        }
        monitoreo.setProgreso(0L);
        monitoreo.setTotal(0L);
        monitoreo.terminarBP();
				//this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios").concat(this.xls.getRuta()), ".".concat(this.xls.getFormat().name()), this.toListFile(sesion, this.xls, 1L));
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
				DaoFactory.getInstance().updateAll(sesion, TcManticListasPreciosArchivosDto.class, tmp.toMap());
				DaoFactory.getInstance().insert(sesion, tmp);
				sesion.flush();
				//this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("listaprecios").concat(this.pdf.getRuta()), ".".concat(this.pdf.getFormat().name()), this.toListFile(sesion, this.pdf, 2L));
			} // if	
  	} // if	
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
  
}