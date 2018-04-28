package mx.org.kaana.mantic.catalogos.personas.reglas;

import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.procesos.usuarios.reglas.RandomCuenta;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import org.hibernate.Session;

public class Transaccion  extends IBaseTnx{

	private TcManticPersonasDto persona;	
	private String messageError;
	private String cuenta;

	public Transaccion(TcManticPersonasDto persona) {
		this.persona= persona;		
	} // Transaccion

	public String getCuenta() {
		return cuenta;
	}		
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar   = false;
		Encriptar encriptar= null;
		try {
			this.messageError= "Ocurrio un error al ".concat(accion.name().toLowerCase()).concat(" el registro de la persona");
			switch(accion){
				case AGREGAR:
					encriptar= new Encriptar();
					toCuenta();
					this.persona.setContrasenia(encriptar.encriptar(this.persona.getRfc()));
					this.persona.setCuenta(this.cuenta);
					regresar= DaoFactory.getInstance().insert(sesion, this.persona)>= 1L;
					break;
				case MODIFICAR:
					this.cuenta= this.persona.getCuenta();
					regresar= DaoFactory.getInstance().update(sesion, this.persona)>= 1L;
					break;				
				case ELIMINAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.persona)>= 1L;
					break;
			} // switch
			if(!regresar)
        throw new Exception(this.messageError);
		} // try
		catch (Exception e) {			
			throw new Exception(this.messageError);
		} // catch		
		return regresar;
	}	// ejecutar
	
	private void toCuenta(){
		RandomCuenta random= null;
		try {
			random= new RandomCuenta(this.persona.getNombres(), this.persona.getPaterno(), this.persona.getMaterno(), -1L);
      this.cuenta= random.getCuentaGenerada();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
	} // toCuenta
}