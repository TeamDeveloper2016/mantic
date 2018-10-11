package mx.org.kaana.mantic.catalogos.empresas.cuentas.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.empresas.cuentas.beans.TreeCuenta;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class MotorBusqueda implements Serializable {

	private static final long serialVersionUID=5778899664321119975L;
	
	private Long id;
	private TreeCuenta tree;
	private List<String> files;

	public MotorBusqueda(Long id) {
		this(id, null);
	}		

	public MotorBusqueda(TreeCuenta tree) {
		this(-1L, tree);
	}
	
	public MotorBusqueda(Long id, TreeCuenta tree) {
		this.id   = id;
		this.tree = tree;
		this.files= new ArrayList<>();
	}

	public List<String> getFiles() {
		return files;
	}
	
	public TreeCuenta toParent() throws Exception {
		TreeCuenta regresar       = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idEmpresaDeuda", this.id);
			params.put("sortOrder", "");			
			regresar= (TreeCuenta) DaoFactory.getInstance().toEntity(TreeCuenta.class, "VistaEmpresasDto", "cuentas", params);
		} // try // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // toParent
	
	public List<TreeNode> toChildrens() throws Exception {
		List<TreeNode> regresar= null;
		List<TreeCuenta> nodes     = null;
		Map<String, Object>params = null;
		try {
			regresar= new ArrayList<>();
				params= new HashMap<>();
				params.put("idEmpresaDeuda", this.id);
				nodes= DaoFactory.getInstance().toEntitySet(TreeCuenta.class, "VistaEmpresasDto", "pagosDeuda", params, Constantes.SQL_TODOS_REGISTROS);
				if(!nodes.isEmpty()){				
					for(TreeCuenta item: nodes) {						
						item.setUltimoNivel(true);								
						regresar.add(new DefaultTreeNode("pago", item, null));
					} // for
				} // if
		} // try // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toChildrens

	private void toFillDocuments(String idKey, String key, Long id, String path) throws Exception {
		Map<String, Object> params= null;
		try {
			params=new HashMap<>();
      params.put(key, id);			
			List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaEstructuraOrdenesCompraDto", idKey, params, Constantes.SQL_TODOS_REGISTROS);
			if(items!= null && !items.isEmpty()){
				for (Entity item : items) {
				  this.files.add(path+ item.toString("alias"));	
				} // for
			} // if
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	} // toFillDocuments
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.files);
	}	
}