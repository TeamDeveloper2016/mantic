package mx.org.kaana.mantic.ventas.garantias.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.mantic.enums.ETiposGarantias;
import mx.org.kaana.mantic.ventas.beans.ArticuloVenta;
import mx.org.kaana.mantic.ventas.beans.TicketVenta;
import mx.org.kaana.mantic.ventas.caja.beans.VentaFinalizada;

public class Garantia extends VentaFinalizada implements Serializable {

	private static final long serialVersionUID = -4603022684128624529L;
	private TicketVenta garantia;	
	private ETiposGarantias tipoGarantia;
	private List<ArticuloVenta> articulosGarantia;
	private Integer idEfectivo;
	
	public Garantia() {
		this(new TicketVenta(), ETiposGarantias.RECIBIDA);
	}
	
	public Garantia(TicketVenta garantia, ETiposGarantias tipoGarantia) {
		this(garantia, tipoGarantia, new ArrayList<ArticuloVenta>());
	}
	
	public Garantia(TicketVenta garantia, ETiposGarantias tipoGarantia, List<ArticuloVenta> articulosGarantia) {
		this(garantia, tipoGarantia, articulosGarantia, Constantes.SI);
	}
	
	public Garantia(TicketVenta garantia, ETiposGarantias tipoGarantia, List<ArticuloVenta> articulosGarantia, Integer idEfectivo) {
		super();
		this.garantia         = garantia;
		this.tipoGarantia     = tipoGarantia;
		this.articulosGarantia= articulosGarantia;
		this.idEfectivo       = idEfectivo;
	}

	public TicketVenta getGarantia() {
		return garantia;
	}

	public void setGarantia(TicketVenta garantia) {
		this.garantia = garantia;
	}	

	public ETiposGarantias getTipoGarantia() {
		return tipoGarantia;
	}

	public void setTipoGarantia(ETiposGarantias tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}

	public List<ArticuloVenta> getArticulosGarantia() {
		return articulosGarantia;
	}

	public void setArticulosGarantia(List<ArticuloVenta> articulosGarantia) {
		this.articulosGarantia = articulosGarantia;
	}

	public Integer getIdEfectivo() {
		return idEfectivo;
	}

	public void setIdEfectivo(Integer idEfectivo) {
		this.idEfectivo = idEfectivo;
	}	
}
