package mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans;

import java.io.Serializable;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.db.dto.TcManticTransferenciasDto;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/01/2019
 * @time 10:33:04 AM 
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
*/

public class Traspases extends TcManticTransferenciasDto implements Serializable {

	private static final long serialVersionUID=-4987242434377529059L;
	
	private UISelectEntity ikAlmacen;
	private UISelectEntity ikDestino;
	private UISelectEntity ikSolicito;

	public Traspases() {
		this.ikAlmacen = new UISelectEntity(-1L);
		this.ikDestino = new UISelectEntity(-1L);
		this.ikSolicito= new UISelectEntity(-1L);
	}

	public Traspases(Long idSolicito, Long idTransferenciaEstatus, Long idTransferenciaTipo, Long ejercicio, String consecutivo, Long idUsuario, Long idAlmacen, String observaciones, Long idDestino, Long idEmpresa, Double cantidad, Long orden, Long idArticulo, Long idTransferencia) {
		super(idSolicito, idTransferenciaEstatus, idTransferenciaTipo, ejercicio, consecutivo, idUsuario, idAlmacen, observaciones, idDestino, idEmpresa, cantidad, orden, idArticulo, idTransferencia);
		this.ikAlmacen = new UISelectEntity(-1L);
		this.ikDestino = new UISelectEntity(-1L);
		this.ikSolicito= new UISelectEntity(-1L);
	}

	public UISelectEntity getIkAlmacen() {
		return ikAlmacen;
	}

	public void setIkAlmacen(UISelectEntity ikAlmacen) {
		this.ikAlmacen=ikAlmacen;
		if(ikAlmacen!= null)
			this.setIdAlmacen(ikAlmacen.getKey());
	}

	public UISelectEntity getIkDestino() {
		return ikDestino;
	}

	public void setIkDestino(UISelectEntity ikDestino) {
		this.ikDestino=ikDestino;
		if(ikDestino!= null)
			this.setIdDestino(ikDestino.getKey());
	}

	public UISelectEntity getIkSolicito() {
		return ikSolicito;
	}

	public void setIkSolicito(UISelectEntity ikSolicito) {
		this.ikSolicito= ikSolicito;
		if(ikSolicito!= null)
			this.setIdSolicito(ikSolicito.getKey());
	}

	@Override
	public Class toHbmClass() {
		return TcManticTransferenciasDto.class;
	}
	
}
