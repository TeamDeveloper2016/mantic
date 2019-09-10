/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.categorias.reglas;

import java.io.Serializable;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.mantic.db.dto.TcManticCategoriasDto;
import mx.org.kaana.libs.cfg.Detalle;

/**
 *
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class MotorBusqueda implements Serializable{
	
	private Long idCategoria;

	public MotorBusqueda(Long idCategoria) {
		this.idCategoria = idCategoria;
	}
	
	public TcManticCategoriasDto toCategoria() throws Exception{
		TcManticCategoriasDto regresar= null;
		try {
			regresar= (TcManticCategoriasDto) DaoFactory.getInstance().findById(TcManticCategoriasDto.class, this.idCategoria);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toCategoria
	
	public Long toParent() throws Exception{
		Long regresar            = -1L;
		TcManticCategoriasDto dto= null;			
		Categoria categoria      = null;
		try {
			categoria= new Categoria();
			dto= toCategoria();
			if(dto.getNivel() > 1L)
				regresar= categoria.getFather(dto.getClave()).getIdCategoria();			
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	} // toParent
	
	public String toNextClave() throws Exception{
		String regresar          = null;
		TcManticCategoriasDto dto= null;
		Categoria categoria      = null;
		try {
			categoria= new Categoria();
			if(this.idCategoria.equals(-1L)){
				regresar= categoria.toNextKey("", 1);
			} // if			
			else{
				dto= toCategoria();
				regresar= categoria.toNextKey(dto.getClave(), dto.getNivel().intValue() + 1);			
			} // else
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toNextClave
	
	private String doCompletarClave(int nivel, String clave){
		StringBuilder regresar= null;
		Categoria categoria   = null;
		try {
			categoria= new Categoria();
			regresar= new StringBuilder();
			for(Detalle detalle: categoria.getNiveles()){
				if(detalle.getNivel()> nivel)
				  regresar= regresar.append("00");
				else if(detalle.getNivel()== nivel)
					regresar= regresar.append(clave);
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar.toString();
	} // doCompletarClave
	
	public boolean isChild() throws Exception{
		boolean regresar         = false;
		Categoria categoria      = null;
		TcManticCategoriasDto dto= null;
		try {
			dto= toCategoria();
			categoria= new Categoria();
			regresar= categoria.isChild(dto.getClave(), dto.getNivel().intValue());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // getChilds
}
