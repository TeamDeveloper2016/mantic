package mx.org.kaana.mantic.facturas.beans;

import java.io.Serializable;
import java.util.List;

public class SimpleCfdi implements Serializable {
	
	private static final long serialVersionUID = 2004671922070223893L;
	private ClienteFactura cliente;
	private List<ArticuloFactura> articulos;

	public SimpleCfdi(ClienteFactura cliente, List<ArticuloFactura> articulos) {
		this.cliente  = cliente;
		this.articulos= articulos;
	}

	public ClienteFactura getCliente() {
		return cliente;
	}

	public void setCliente(ClienteFactura cliente) {
		this.cliente = cliente;
	}

	public List<ArticuloFactura> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<ArticuloFactura> articulos) {
		this.articulos = articulos;
	}	
}
