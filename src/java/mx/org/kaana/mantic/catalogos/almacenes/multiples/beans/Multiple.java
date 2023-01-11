package mx.org.kaana.mantic.catalogos.almacenes.multiples.beans;

import java.io.Serializable;
import java.util.Calendar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasMultiplesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 10/01/2023
 *@time 13:39:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Multiple extends TcManticTransferenciasMultiplesDto implements Serializable {

	private static final long serialVersionUID=3088884892456452488L;
	
	private UISelectEntity ikEmpresa;
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikDestino;
	private UISelectEntity ikSolicito;

	public Multiple() {
		this(-1L);
	}

	public Multiple(Long key) {
		this(-1L, 1L, new Long(Calendar.getInstance().get(Calendar.YEAR)), new Long(Calendar.getInstance().get(Calendar.YEAR))+ "00000", 1L, -1L, "", JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), 1L, -1L);
	}
	
	public Multiple(Long idSolicito, Long idTransferenciaMultipleEstatus, Long ejercicio, String consecutivo, Long idUsuario, Long idAlmacen, String observaciones, Long idEmpresa, Long orden, Long idTransferenciaMultiple) {
		super(consecutivo, idSolicito, idTransferenciaMultipleEstatus, idUsuario, idAlmacen, observaciones, idEmpresa, orden, idTransferenciaMultiple, ejercicio);
		this.ikEmpresa = new UISelectEntity(idEmpresa);
		this.ikAlmacen = new UISelectEntity(idAlmacen);
		this.ikSolicito= new UISelectEntity(idSolicito== null? -1L: idSolicito);
		this.ikDestino = new UISelectEntity(-1L);
	}
	
	public UISelectEntity getIkEmpresa() {
		return ikEmpresa;
	}

	public void setIkEmpresa(UISelectEntity ikEmpresa) {
		this.ikEmpresa=ikEmpresa;
		if(this.ikEmpresa!= null)
		  this.setIdEmpresa(this.ikEmpresa.getKey());
	}

	public UISelectEntity getIkAlmacen() {
		return ikAlmacen;
	}

	public void setIkAlmacen(UISelectEntity ikAlmacen) {
		this.ikAlmacen=ikAlmacen;
		if(this.ikAlmacen!= null)
		  this.setIdAlmacen(this.ikAlmacen.getKey());
	}

	public UISelectEntity getIkSolicito() {
		return ikSolicito;
	}

	public void setIkSolicito(UISelectEntity ikSolicito) {
		this.ikSolicito=ikSolicito;
		if(this.ikSolicito!= null)
		  this.setIdSolicito(this.ikSolicito.getKey());
	}

  public UISelectEntity getIkDestino() {
    return ikDestino;
  }

  public void setIkDestino(UISelectEntity ikDestino) {
    this.ikDestino = ikDestino;
  }

  @Override
	public Class toHbmClass() {
		return TcManticTransferenciasMultiplesDto.class;
	}
	
}
