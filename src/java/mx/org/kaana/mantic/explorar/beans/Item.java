package mx.org.kaana.mantic.explorar.beans;

import java.io.Serializable;
import mx.org.kaana.mantic.db.dto.TcManticPedidosDetallesDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 5/08/2019
 *@time 09:07:47 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Item extends TcManticPedidosDetallesDto implements Serializable {

	private static final long serialVersionUID=1L;
	
	private Long idUsuario;
	private String consecutivo;
	private Double total;
	private Long idPedidoEstatus;
	private Double original;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario=idUsuario;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo=consecutivo;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total=total;
	}

	public Long getIdPedidoEstatus() {
		return idPedidoEstatus;
	}

	public void setIdPedidoEstatus(Long idPedidoEstatus) {
		this.idPedidoEstatus=idPedidoEstatus;
	}

	public Double getOriginal() {
		return original;
	}

	public void setOriginal(Double original) {
		this.original=original;
	}

	@Override
	public String toString() {
		return "Item{"+"idUsuario="+idUsuario+", consecutivo="+consecutivo+", total="+total+", idPedidoEstatus="+idPedidoEstatus+", original="+original+'}';
	}

}
