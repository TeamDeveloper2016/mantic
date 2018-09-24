package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.TreeOrden;
import mx.org.kaana.mantic.enums.EDocumentosOrden;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class MotorBusqueda implements Serializable {

	private static final long serialVersionUID=5778899664321119975L;
	
	private Long id;
	private TreeOrden tree;
	private List<String> files;

	public MotorBusqueda(Long id) {
		this(id, null);
	}		

	public MotorBusqueda(TreeOrden tree) {
		this(-1L, tree);
	}
	
	public MotorBusqueda(Long id, TreeOrden tree) {
		this.id   = id;
		this.tree = tree;
		this.files= new ArrayList<>();
	}

	public List<String> getFiles() {
		return files;
	}
	
	public TreeOrden toParent() throws Exception {
		TreeOrden regresar       = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idOrdenCompra", this.id);
			regresar= (TreeOrden) DaoFactory.getInstance().toEntity(TreeOrden.class, "VistaEstructuraOrdenesCompraDto", "orden", params);
			regresar.setTipo(EDocumentosOrden.ORDEN_COMPRA);
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // toParent
	
	public List<TreeNode> toChildrens() throws Exception {
		List<TreeNode> regresar  = null;
		List<TreeOrden> nodes    = null;
		Map<String, Object>params= null;
		String type              = null;
		try {
			regresar= new ArrayList<>();
			if(!this.tree.getTipo().equals(EDocumentosOrden.NOTA_CREDITO)) {
				params= new HashMap<>();
				params.put(this.tree.getTipo().getIdParametro(), this.tree.getId());
				nodes= DaoFactory.getInstance().toEntitySet(TreeOrden.class, "VistaEstructuraOrdenesCompraDto", this.tree.getTipo().getIdXml(), params, Constantes.SQL_TODOS_REGISTROS);
				if(!nodes.isEmpty()){				
					for(TreeOrden item: nodes) {
						switch(this.tree.getTipo()) {
							case DEVOLUCION:
								item.setTipo(EDocumentosOrden.NOTA_CREDITO);
								item.setUltimoNivel(true);
								if(!Cadena.isVacio(item.getAlias()))
								  this.files.add(Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").length()+ "|"+ item.getAlias());
								type="credito";
								break;
							case NOTA_ENTRADA:
								item.setTipo(EDocumentosOrden.DEVOLUCION);
								type="devolucion";							
								break;
							case ORDEN_COMPRA:
								item.setTipo(EDocumentosOrden.NOTA_ENTRADA);
								if(!Cadena.isVacio(item.getAlias()))
								  this.files.add(Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").length()+ "|"+ item.getAlias());
								type="entrada";
								break;							
						} // switch
						regresar.add(new DefaultTreeNode(type, item, null));
					} // for
				} // if
			}
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toChildrens

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.files);
	}
	
}
