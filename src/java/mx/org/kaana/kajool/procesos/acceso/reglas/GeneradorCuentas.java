package mx.org.kaana.kajool.procesos.acceso.reglas;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 14/09/2015
 * @time 08:09:58 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.db.dto.TrJanalUsuariosDelegaDto;

public class GeneradorCuentas implements Serializable{

		private static final long serialVersionUID= 4826441195423797120L;		
		private final String[] PALABRAS_EXCLUIDAS = {"ma.", "los", "para", "con", "las", "les", "por", "sin", "has"};
		private String nombres;
		private String apellidoPaterno;
		private String apellidoMaterno;
		private String cuentaGenerada;		
		private Long idEmpleado;
		private boolean empledo;

		public GeneradorCuentas(String nombres, String apellidoPaterno, String apellidoMaterno) {
			this(nombres, apellidoPaterno, apellidoMaterno, -1L, false);
		} // GeneradorCuentas
		
		public GeneradorCuentas(String nombres, String apellidoPaterno, String apellidoMaterno, Long idEmpleado, boolean empleado) {
			this.nombres        = nombres;
			this.apellidoPaterno= apellidoPaterno;
			this.apellidoMaterno= apellidoMaterno;		
			this.idEmpleado     = idEmpleado;
			this.empledo        = empleado;
			format();
			generar();
		} // GeneradorCuentas

		public String getCuentaGenerada() {
			return cuentaGenerada;
		} // getCuentaGenerada

		private void format() {
			if (this.apellidoMaterno!= null)
				this.apellidoMaterno= format(this.apellidoMaterno);			
			if (this.apellidoPaterno!= null)
				this.apellidoPaterno= format(this.apellidoPaterno);		
			if (this.nombres!= null)
				this.nombres= format(this.nombres);
		} // format

		private String format(String campo){
			String regresar= campo;
			regresar= regresar.trim();
			regresar= regresar.replaceAll(" ", "");
			regresar= regresar.toLowerCase();
			regresar= Cadena.sinAcentos(regresar);
			regresar= regresar.replaceAll("ñ", "n");
			return regresar;
		} // format

		private void generar() {		
			String[] nombreSeparado= this.nombres.split(" ");
			if (this.apellidoPaterno!=null) {
				this.cuentaGenerada= concatenarCuentaPunto(getNombreInicial(nombreSeparado), this.apellidoPaterno);
				if (consultarCuenta(this.cuentaGenerada)) {
					if (this.apellidoMaterno!=null) {
						this.cuentaGenerada= concatenarCuentaPunto(getNombreInicial(nombreSeparado), this.apellidoMaterno);
						if (consultarCuenta(this.cuentaGenerada)) {
							this.cuentaGenerada= concatenarCuentaPunto(concatenarCuenta(getNombreInicial(nombreSeparado), this.apellidoPaterno), this.apellidoMaterno);
							if (consultarCuenta(this.cuentaGenerada)) {
								this.cuentaGenerada= concatenarCuentaPunto(concatenarCuenta(getNombreInicial(nombreSeparado), this.apellidoMaterno), this.apellidoPaterno);
								if (consultarCuenta(this.cuentaGenerada)) {
									this.cuentaGenerada= generarNombres(nombreSeparado, this.apellidoPaterno);
									if (consultarCuenta(this.cuentaGenerada))
										this.cuentaGenerada= generarNombres(nombreSeparado, this.apellidoMaterno);								
								} // if
							} // if
						} // if
					} // if materno
					else {
						this.cuentaGenerada= concatenarCuentaPunto(this.apellidoPaterno, getNombreInicial(nombreSeparado));
						if (consultarCuenta(this.cuentaGenerada))
							this.cuentaGenerada= generarNombres(nombreSeparado, this.apellidoPaterno);					
					} // else
				} // if paterno
			} // if
			else {
				if (this.apellidoMaterno!= null) {
					this.cuentaGenerada= concatenarCuentaPunto(getNombreInicial(nombreSeparado), this.apellidoMaterno);
					if (consultarCuenta(this.cuentaGenerada))
						this.cuentaGenerada= generarNombres(nombreSeparado, this.apellidoMaterno);				
				} // if materno
			} // else
			if (this.cuentaGenerada!= null && consultarCuenta(this.cuentaGenerada))
				this.cuentaGenerada= null;		
		} // generar

		private String generarNombres(String[] nombreSeparado, String apellido) {
			String regresar= null;
			String temporal= null;
			int count      = 0;
			String campoUno= null;
			String campoDos= null;
			try {
				if (isPalabraExcluida(nombreSeparado[0])) {
					if (isPalabraExcluida(nombreSeparado[1])) {
						if (isPalabraExcluida(nombreSeparado[2]))
							temporal= nombreSeparado[3];					
						else
							temporal= nombreSeparado[2];					
					} // if
					else
						temporal= nombreSeparado[1];
				} // if
				else
					temporal= nombreSeparado[0];
				for (String nombre : nombreSeparado) {
					if (nombreSeparado.length== ++count) {
						if (nombreSeparado.length== 1) {
							campoUno= temporal.concat(this.apellidoMaterno!= null && !this.apellidoMaterno.equals("") ? this.apellidoMaterno.substring(0, 3) : (this.apellidoPaterno!= null && !this.apellidoPaterno.equals("") ? this.apellidoPaterno.substring(0, 3) : "xx"));
							campoDos= apellidoPaterno!=null&&!apellidoPaterno.equals("") ? apellidoPaterno : apellidoMaterno;
							regresar= concatenarCuentaPunto(campoUno, campoDos);
						} // if
						break;
					} // if
					else {
						if (nombreSeparado[count].length()> 2 && !isPalabraExcluida(nombreSeparado[count])) {
							temporal= concatenarCuenta(temporal, nombreSeparado[count]);
							regresar= concatenarCuentaPunto(temporal, apellido);
							if (!consultarCuenta(regresar))
								break;							
							else {
								regresar= null;
							} // else
						} // if
					} // else
				} // for
			} // try
			catch (Exception e) {
				Error.mensaje(e);
			} // catch
			return regresar;
		} // generarNombres

		private String concatenarCuentaPunto(String campo1, String campo2) {
			return campo1.concat(".").concat(campo2);
		} // concatenarCuentaPunto

		private String concatenarCuenta(String campo1, String campo2) {
			return campo1.concat(campo2);
		} // concatenarCuenta

		private boolean consultarCuenta(String cuenta) {
			boolean regresar          = false;
			Map<String, Object> params= toParams(cuenta);
			try {
				if(this.empledo){
					regresar= consultarCuentaEmpleado(params);
					if(!regresar)
						regresar= consultarCuentaUsuario(params);
				} // if
				else
					regresar= consultarCuentaUsuario(params);
			} // try
			catch (Exception e) {								
				throw e;
			} // catch
			finally {
				Methods.clean(params);
			} // finally			
			return regresar;
		} // consultarCuenta
		
		private boolean consultarCuentaUsuario(Map<String, Object> params) {
			boolean regresar                       = false;			
			List<TrJanalUsuariosDelegaDto> usuarios= null;
			try {
				usuarios= DaoFactory.getInstance().findViewCriteria(TrJanalUsuariosDelegaDto.class, params, "login");
				regresar= !usuarios.isEmpty();
			} // try
			catch (Exception e) {
				Error.mensaje(e);
			} // catch
			return regresar;
		} // consultarCuentaUsuario
		
		private boolean consultarCuentaEmpleado(Map<String, Object> params) {
			boolean regresar                 = false;
			List<TcJanalUsuariosDto> usuarios= null;
			try {
				usuarios= DaoFactory.getInstance().findViewCriteria(TcJanalUsuariosDto.class, params, "login");
				regresar= !usuarios.isEmpty();
				for (TcJanalUsuariosDto usuario : usuarios) {
					//regresar= !usuario.getIdEmpleado().equals(this.idEmpleado);
					if (regresar== false)
						break;					
				} // if
			} // try
			catch (Exception e) {
				Error.mensaje(e);
			} // catch
			return regresar;
		} // consultarCuentaEmpleado
		
		private Map<String, Object> toParams(String cuenta){
			Map<String, Object>regresar= null;
			try {
				regresar= new HashMap<>();
				regresar.put("login", cuenta);
			} // try
			catch (Exception e) {								
				throw e;
			} // catch			
			return regresar;
		} // toParams

		private boolean isPalabraExcluida(String nombre) {
			boolean regresar= false;
			try {
				if (nombre.length()> 2) {
					for (String excluida : PALABRAS_EXCLUIDAS) {
						if (excluida.equals(nombre)) {
							regresar= excluida.equals(nombre);
							break;
						} // false
					} // for
				} // if
				else
					regresar=true;
			} // try
			catch (Exception e) {
				Error.mensaje(e);
			} // catch
			return regresar;
		} // isPalabraExcluida

		private String getNombreInicial(String[] nombres) {
			String regresar= null;
			try {
				for (String nombre : nombres) {
					if (!isPalabraExcluida(nombre)) {
						regresar= nombre;
						break;
					} // if
				} // for
			} // try
			catch (Exception e) {
				Error.mensaje(e);
			} // try
			return regresar;
		}	// getNombreInicial	
}
