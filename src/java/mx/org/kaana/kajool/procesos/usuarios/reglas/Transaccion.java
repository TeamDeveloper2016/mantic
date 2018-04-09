package mx.org.kaana.kajool.procesos.usuarios.reglas;

import java.util.Map;
import mx.org.kaana.libs.formato.BouncyEncryption;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import static mx.org.kaana.kajool.enums.EAccion.ACTIVAR;
import static mx.org.kaana.kajool.enums.EAccion.AGREGAR;
import static mx.org.kaana.kajool.enums.EAccion.ELIMINAR;
import static mx.org.kaana.kajool.enums.EAccion.MODIFICAR;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;
import org.hibernate.Session;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 1/09/2015
 * @time 10:43:46 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class Transaccion extends IBaseTnx {
	
	private Map<String, Object> attrs;
	
	public Transaccion(Map<String, Object> attrs) {
		this.attrs= attrs;
	}							

	@Override
	protected boolean ejecutar(Session session, EAccion accion) throws Exception {
		boolean regresar            = false;
		TcJanalUsuariosDto usuario  = null;		
		TcJanalEmpleadosDto empleado= null;
		try {			
			switch (accion) {
				case AGREGAR:					
					if(((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getIdEmpleado()== -1L) {
					  //Almacenar nueva persona
						((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).setContrasenia(BouncyEncryption.encrypt(((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getCurp().substring(0, 10)));
						((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).setEstilo("sentinel");
						((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).setIdEmpleado(DaoFactory.getInstance().insert(session, ((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto"))));
						((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).setNombres(((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getNombres().toUpperCase());
						((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).setPrimerApellido(((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getPrimerApellido().toUpperCase());
						((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).setSegundoApellido(((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getSegundoApellido().toUpperCase());
					} // if
					//Verificar si usuario ya existe
					this.attrs.put("idEmpleado", ((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getIdEmpleado());
					this.attrs.put("idPerfil", ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).getIdPerfil());
					this.attrs.put("idEntidad", ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).getIdEntidad());
					usuario=(TcJanalUsuariosDto) DaoFactory.getInstance().findIdentically(session, TcJanalUsuariosDto.class, ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).toMap());
					if (usuario!= null && !usuario.getKey().equals(-1L)) { // usuario ya existe
						((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).setActivo(usuario.getActivo());
						this.attrs.put("tcJanalUsuarioDto", usuario);
						regresar=false;
					} // if
					else {						
						//Almacenar nuevo usuario						
						((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).setActivo(1L);												
						((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).setIdUsuarioModifica(JsfBase.getAutentifica().getEmpleado().getIdUsuario());						
						((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).setIdEmpleado(((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getIdEmpleado());
						regresar= DaoFactory.getInstance().insert(session, ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")))>=1L;						
					} // else
					break;
				case MODIFICAR:
					//Verificar si usuario ya existe
					this.attrs.put("idEmpleado", ((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")).getIdEmpleado());
					this.attrs.put("idPerfil", ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")).getIdPerfil());					
					regresar= DaoFactory.getInstance().update(session, ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")))>=1L && DaoFactory.getInstance().update(session, ((TcJanalEmpleadosDto)this.attrs.get("tcJanalEmpleadoDto")))>=1L;					
					break;
				case ELIMINAR:					
					Entity dto=(Entity) DaoFactory.getInstance().toEntity(session, "TrJanalMovimientosDto", "usuario", this.attrs);
					if (dto==null||dto.isEmpty()) {								
						regresar= DaoFactory.getInstance().delete(session, ((TcJanalUsuariosDto)this.attrs.get("tcJanalUsuarioDto")))>=1L;
					} // if
					else {
						JsfBase.addMessage("Administración de grupos de trabajo", "No se puede eliminar el registro ya que cuenta con carga de trabajo asignada", ETipoMensaje.INFORMACION);
					} // else
					break;
				case ACTIVAR:										
					regresar=DaoFactory.getInstance().update(session, TcJanalUsuariosDto.class, (Long)this.attrs.get("idUsuario"), this.attrs)>=1L;
					break;
				case RESTAURAR:
					empleado= (TcJanalEmpleadosDto) DaoFactory.getInstance().findById(session, TcJanalEmpleadosDto.class, (Long)this.attrs.get("idEmpleado"));
          empleado.setContrasenia(BouncyEncryption.encrypt(empleado.getCurp().substring(0, 10)));
					regresar= DaoFactory.getInstance().update(session, empleado)>= 1;
					break;
				case REGISTRAR:
					empleado= (TcJanalEmpleadosDto) DaoFactory.getInstance().findById(session, TcJanalEmpleadosDto.class, (Long)this.attrs.get("idEmpleado"));
          empleado.setContrasenia(BouncyEncryption.encrypt((String)this.attrs.get("nueva")));
					regresar= DaoFactory.getInstance().update(session, empleado)>= 1;
					break;
				case COMPLEMENTAR:
					empleado= (TcJanalEmpleadosDto) DaoFactory.getInstance().findById(session, TcJanalEmpleadosDto.class, (Long)this.attrs.get("idEmpleado"));
					empleado.setContrasenia(BouncyEncryption.encrypt(this.attrs.get("nuevaContrasenia").toString()));
					regresar= DaoFactory.getInstance().update(session, empleado)>= 1;
					break;
			} // switch
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			regresar=false;
			throw e;
		} // catch    		
		return regresar;
	} // ejecutar

}
