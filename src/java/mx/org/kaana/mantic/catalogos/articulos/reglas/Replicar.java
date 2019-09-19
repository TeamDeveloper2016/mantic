package mx.org.kaana.mantic.catalogos.articulos.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.beans.CodigoArticulo;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticImagenesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public final class Replicar extends IBaseTnx implements Serializable {
	
	private static final Log LOG=LogFactory.getLog(Replicar.class);
	private static final long serialVersionUID=8680186650062307089L;

	private final Entity articulo;
	private final List<CodigoArticulo> articulos;
	private String messageError;

	public Replicar(Entity articulo, List<CodigoArticulo> articulos) {
		this.articulo = articulo;
		this.articulos= articulos;
	} // Replicar

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
		boolean regresar= false;
		this.messageError= "Ocurrio un error al replicar las imagenes, verifiquelo por favor !";
		Map<String, Object> params= new HashMap<>();
		try {
      for (CodigoArticulo item: this.articulos) {
				TcManticArticulosDto element= (TcManticArticulosDto)DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, item.getIdArticulo());
				if(element!= null) {
					Long idImagen= element.getIdImagen();
					element.setIdImagen(this.articulo.toLong("idImagen"));
					regresar= DaoFactory.getInstance().update(sesion, element).intValue()> 0;
					if(idImagen!= null) {
						params.put("idImagen", idImagen);
						params.put("idArticulo", element.getIdArticulo());
						Value count= (Value)DaoFactory.getInstance().toField(sesion, "TcManticArticulosDto", "existe", params, "total");
						if(count== null || count.toLong()<= 0L){
							sesion.flush();
							DaoFactory.getInstance().delete(sesion, TcManticImagenesDto.class, idImagen);
						} // if
					} // if
				} // if
				else
					LOG.warn("EL ARTICULO ["+ item.getIdArticulo()+ "] NO EXISTE EN EL CATALOGO, FAVOR DE VERIFICAR !");
			} // for
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch	
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // ejecutar
	
}
