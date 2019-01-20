package mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans;

import java.io.Serializable;
import java.util.Calendar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/01/2019
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Transferencia extends TcManticTransferenciasDto implements Serializable {

	private static final long serialVersionUID=3088884892456452488L;
	
	private UISelectEntity ikEmpresa;
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikSolicito;
	private UISelectEntity ikDestino;

	public Transferencia() {
		this(-1L);
	}

	public Transferencia(Long key) {
	  this(key, 1L);
	}
	
	public Transferencia(Long key, Long idTransferenciaTipo) {
		this(-1L, 1L, idTransferenciaTipo, new Long(Calendar.getInstance().get(Calendar.YEAR)), new Long(Calendar.getInstance().get(Calendar.YEAR))+ "00000", 1L, -1L, "", -1L, JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), 1L, -1L);
	}

	public Transferencia(Long idSolicito, Long idTransferenciaEstatus, Long idTransferenciaTipo, Long ejercicio, String consecutivo, Long idUsuario, Long idAlmacen, String observaciones, Long idDestino, Long idEmpresa, Long orden, Long idTransferencia) {
		super(idSolicito, idTransferenciaEstatus, idTransferenciaTipo, ejercicio, consecutivo, idUsuario, idAlmacen, observaciones, idDestino, idEmpresa, orden, idTransferencia);
		this.ikEmpresa = new UISelectEntity(idEmpresa);
		this.ikAlmacen = new UISelectEntity(idAlmacen);
		this.ikDestino = new UISelectEntity(idDestino);
		this.ikSolicito= new UISelectEntity(idSolicito== null? -1L: idSolicito);
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
		this.ikDestino=ikDestino;
		if(this.ikDestino!= null)
		  this.setIdDestino(this.ikDestino.getKey());
	}

	@Override
	public Class toHbmClass() {
		return TcManticTransferenciasDto.class;
	}
	
}
