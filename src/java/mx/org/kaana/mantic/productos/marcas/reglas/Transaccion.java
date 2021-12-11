package mx.org.kaana.mantic.productos.marcas.reglas;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticProductosMarcasArchivosDto;
import mx.org.kaana.mantic.enums.ETipoImagen;
import mx.org.kaana.mantic.productos.marcas.beans.Marca;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

	private Marca marca;	
	private String messageError;

	public Transaccion(Marca marca) {
		this.marca= marca;		
	}

	public String getMessageError() {
		return messageError;
	}

	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el registro de la marca");
			switch(accion){
				case AGREGAR:
          regresar= this.toAgregarMarca(sesion);
					break;
				case MODIFICAR:
          regresar= this.toModificarMarca(sesion);
					break;				
				case ELIMINAR:
          regresar= this.toEliminarMarca(sesion);
					break;
			} // switch
			if(!regresar)
        throw new Exception("");
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError.concat("<br/>")+ e);
		} // catch		
		return regresar;
	}	// ejecutar
	
  private Boolean toAgregarMarca(Session sesion) throws Exception {
    Boolean regresar= Boolean.FALSE;
		try {
      if(this.marca.getImportado()!= null && this.marca.getImportado().getId()< 0L) {
        String name= this.marca.getImportado().getName();
        Long tipo  = ETipoImagen.valueOf(name.substring(name.lastIndexOf(".")+ 1, name.length()).toUpperCase()).getIdTipoImagen();
        TcManticProductosMarcasArchivosDto imagen= new TcManticProductosMarcasArchivosDto(
          -1L, // Long idProductoMarcaArchivo, 
          this.marca.getImportado().getOriginal(), // String archivo, 
          this.marca.getImportado().getFileSize(), // Long tamanio, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          this.marca.getImportado().getRuta().concat(this.marca.getImportado().getName()), // String alias, 
          tipo, // Long idTipoImagen, 
          name, // String nombre
          this.marca.getImportado().getRuta() // String ruta      
        );
        regresar= DaoFactory.getInstance().insert(sesion, imagen)> 0L;
        if(regresar) 
          this.marca.setIdProductoMarcaArchivo(imagen.getIdProductoMarcaArchivo());
      } // if  
      this.marca.setIdUsuario(JsfBase.getIdUsuario());
      regresar= DaoFactory.getInstance().insert(sesion, this.marca)> 0L;
      this.toCheckDeleteFile(sesion, this.marca.getImportado().getName());
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
  private Boolean toModificarMarca(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
    Long idProductoMarcaArchivo= this.marca.getIdProductoMarcaArchivo();
		try {
      if(this.marca.getImportado()!= null && this.marca.getImportado().getId()< 0L) {
        String name= this.marca.getImportado().getName();
        Long tipo  = ETipoImagen.valueOf(name.substring(name.lastIndexOf(".")+ 1, name.length()).toUpperCase()).getIdTipoImagen();
        TcManticProductosMarcasArchivosDto imagen= new TcManticProductosMarcasArchivosDto(
          -1L, // Long idProductoMarcaArchivo, 
          this.marca.getImportado().getOriginal(), // String archivo, 
          this.marca.getImportado().getFileSize(), // Long tamanio, 
          JsfBase.getIdUsuario(), // Long idUsuario, 
          this.marca.getImportado().getRuta().concat(this.marca.getImportado().getName()), // String alias, 
          tipo, // Long idTipoImagen, 
          name, // String nombre
          this.marca.getImportado().getRuta() // String ruta      
        );
        regresar= DaoFactory.getInstance().insert(sesion, imagen)> 0L;
        if(regresar) 
          this.marca.setIdProductoMarcaArchivo(imagen.getIdProductoMarcaArchivo());
        this.toCheckDeleteFile(sesion, this.marca.getImportado().getName());
        if(idProductoMarcaArchivo!= null)
          this.toCheckFile(sesion, idProductoMarcaArchivo);
      } // if  
      regresar= DaoFactory.getInstance().update(sesion, this.marca)> 0L;
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    return regresar;
  }
  
  private Boolean toEliminarMarca(Session sesion) throws Exception {
    Boolean regresar = Boolean.FALSE;
    Map<String, Object> params = null;
    try {      
      params = new HashMap<>();      
      params.put("idProductoMarca", this.marca.getIdProductoMarca());      
      Entity entity= (Entity)DaoFactory.getInstance().toEntity(sesion, "TcManticProductosDto", "existe", params);
      if(entity!= null && entity.toLong("cuantos")<= 0L) {
        regresar= DaoFactory.getInstance().delete(sesion, this.marca)> 0L;
        if(regresar && this.marca.getImportado()!= null && this.marca.getImportado().getId()> 0L) {
          File file= new File(this.marca.getImportado().getRuta());
          if(file.exists())
            file.delete();
          regresar= DaoFactory.getInstance().delete(sesion, TcManticProductosMarcasArchivosDto.class, this.marca.getImportado().getId())> 0L;
        } // if  
      } // if  
      else 
        throw new RuntimeException("La marca no se puede eliminar hay productos asociados");
    } // try
    catch (Exception e) {
      throw e;
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  }
  
  private void toCheckFile(Session sesion, Long idProductoMarcaArchivo) throws Exception {
    try {      
      TcManticProductosMarcasArchivosDto imagen= (TcManticProductosMarcasArchivosDto)DaoFactory.getInstance().findById(sesion, TcManticProductosMarcasArchivosDto.class, idProductoMarcaArchivo);
      if(imagen!= null) {
        File file= new File(imagen.getAlias());
        if(file.exists())
          file.delete();
      } // if  
    } // try
    catch (Exception e) {
      throw e;     
    } // catch	
  }
  
}