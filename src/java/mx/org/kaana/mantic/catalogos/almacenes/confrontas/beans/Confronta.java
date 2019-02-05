package mx.org.kaana.mantic.catalogos.almacenes.confrontas.beans;

import java.io.Serializable;
import java.util.Calendar;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticConfrontasDto;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/01/2019
 *@time 10:29:26 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Confronta extends TcManticConfrontasDto implements Serializable {

	private static final long serialVersionUID=3088884892456452488L;
	
	private UISelectEntity ikEmpresa;
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikSolicito;
	private UISelectEntity ikDestino;
	private TcManticTransferenciasDto transferencia;

	public Confronta() {
		this(-1L);
	}

	public Confronta(Long key) {
	  this(key, -1L);
	}
	
	public Confronta(Long key, Long idTransferencia) {
		this(new Long(Calendar.getInstance().get(Calendar.YEAR))+ "00000", -1L, JsfBase.getIdUsuario(), "", 1L, idTransferencia, new Long(Calendar.getInstance().get(Calendar.YEAR)));
	}

	public Confronta(String consecutivo, Long idConfronta, Long idUsuario, String observaciones, Long orden, Long idTransferencia, Long ejercicio) {
		super(consecutivo, idConfronta, idUsuario, observaciones, orden, idTransferencia, ejercicio);
		this.init();
	}
	
	public void init() {
		try {
			this.transferencia= (TcManticTransferenciasDto)DaoFactory.getInstance().findById(TcManticTransferenciasDto.class, this.getIdTransferencia());
			if(this.transferencia!= null) {
				this.ikEmpresa = new UISelectEntity(this.transferencia.getIdEmpresa());
				this.ikAlmacen = new UISelectEntity(this.transferencia.getIdAlmacen());
				this.ikDestino = new UISelectEntity(this.transferencia.getIdDestino());
				this.ikSolicito= new UISelectEntity(this.transferencia.getIdSolicito()== null? -1L: this.transferencia.getIdSolicito());
			} // if	
		} // try
		catch (Exception e) {
			Error.mensaje(e);
      JsfBase.addMessageError(e);
		} // catch
	}
	
	public UISelectEntity getIkEmpresa() {
		return ikEmpresa;
	}

	public void setIkEmpresa(UISelectEntity ikEmpresa) {
		this.ikEmpresa=ikEmpresa;
		if(this.ikEmpresa!= null)
		  this.transferencia.setIdEmpresa(this.ikEmpresa.getKey());
	}

	public UISelectEntity getIkAlmacen() {
		return ikAlmacen;
	}

	public void setIkAlmacen(UISelectEntity ikAlmacen) {
		this.ikAlmacen=ikAlmacen;
		if(this.ikAlmacen!= null)
		  this.transferencia.setIdAlmacen(this.ikAlmacen.getKey());
	}

	public UISelectEntity getIkSolicito() {
		return ikSolicito;
	}

	public void setIkSolicito(UISelectEntity ikSolicito) {
		this.ikSolicito=ikSolicito;
		if(this.ikSolicito!= null)
		  this.transferencia.setIdSolicito(this.ikSolicito.getKey());
	}

	public UISelectEntity getIkDestino() {
		return ikDestino;
	}

	public void setIkDestino(UISelectEntity ikDestino) {
		this.ikDestino=ikDestino;
		if(this.ikDestino!= null)
		  this.transferencia.setIdDestino(this.ikDestino.getKey());
	}

	public TcManticTransferenciasDto getTransferencia() {
		return transferencia;
	}

	@Override
	public Class toHbmClass() {
		return TcManticConfrontasDto.class;
	}
	
}
