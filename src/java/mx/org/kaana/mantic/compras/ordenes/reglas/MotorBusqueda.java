package mx.org.kaana.mantic.compras.ordenes.reglas;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.compras.ordenes.beans.TreeOrden;
import mx.org.kaana.mantic.db.dto.TcManticCreditosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticNotasEntradasDto;
import mx.org.kaana.mantic.enums.EDocumentosOrden;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class MotorBusqueda implements Serializable {

	private static final Log LOG=LogFactory.getLog(MotorBusqueda.class);
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
								this.toFillDocuments(TcManticCreditosArchivosDto.class, "creditoArchivos", "idCreditoNota", item.getId(), Configuracion.getInstance().getPropiedadSistemaServidor("notascreditos").length()+ "|NOTASDECREDITO|");
								type="credito";
								break;
							case NOTA_ENTRADA:
								item.setTipo(EDocumentosOrden.DEVOLUCION);
								type="devolucion";							
								break;
							case ORDEN_COMPRA:
								item.setTipo(EDocumentosOrden.NOTA_ENTRADA);
								this.toFillDocuments(TcManticNotasArchivosDto.class, "entradaArchivos", "idNotaEntrada", item.getId(), Configuracion.getInstance().getPropiedadSistemaServidor("notasentradas").length()+ "|NOTASDEENTRADA|");
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

	private void toFillDocuments(Class dto, String idKey, String key, Long id, String path) throws Exception {
		Map<String, Object> params=null;
		try {
			params=new HashMap<>();
      params.put(key, id);			
			List<Entity> items= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaEstructuraOrdenesCompraDto", idKey, params, Constantes.SQL_TODOS_REGISTROS);
			if(items!= null && !items.isEmpty())
				for (Entity item : items) {
					// no deberia de devolver registros porque no existe ningun registro
					if(item.toString("alias")!= null) {
						File reference= new File(item.toString("alias"));
						if(reference.exists())
							this.files.add(path+ item.toString("alias"));	
						else {
							// POR ALGUNA RAZON EL ARCHIVO NO SE ENCUENTRA EN LA CARPETA QUE DEBERIA DE ESTAR
							try {
								IBaseDto update= DaoFactory.getInstance().findById(dto, item.toLong("idKey"));
								if(update!= null) {
									Methods.setValue(update, "idPrincipal", new Object[] {2L});
									DaoFactory.getInstance().update(update);
								} // if	
							} // try
							catch (Exception e) {
								LOG.warn("No se puedo actualizar el "+ dto.getClass().getSimpleName());
								Error.mensaje(e);
							} // catch
						} // else	
					} // if
				} // for
		} // try
		catch (Exception e) {
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Methods.clean(this.files);
	}
	
	public static void main(String ... args) throws NoSuchFieldException, Exception {
		IBaseDto update= DaoFactory.getInstance().findById(TcManticNotasEntradasDto.class, 1L);
		Methods.setValue(update, "observaciones", new Object[] {"ESTO ES UN DEMO DE REFLECTION"});
		LOG.info(update);
	}
	
}
