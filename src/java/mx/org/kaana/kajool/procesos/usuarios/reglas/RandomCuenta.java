package mx.org.kaana.kajool.procesos.usuarios.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalEmpleadosDto;
import mx.org.kaana.libs.formato.Error;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 3/09/2015
 * @time 04:05:52 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class RandomCuenta {

  private String nombres;
  private String apellidoPaterno;
  private String apellidoMaterno;
  private String cuentaGenerada;
  private Long idEmpleado;
  private boolean encontrado;
  private final String[] PALABRAS_EXCLUIDAS = {"ma.", "los", "para", "con", "las", "les", "por", "sin", "has"};

  public RandomCuenta(String nombres, String apellidoPaterno, String apellidoMaterno, Long idEmpleado) {
    this.nombres        = nombres;
    this.apellidoPaterno= apellidoPaterno;
    this.apellidoMaterno= apellidoMaterno;
    this.idEmpleado     = idEmpleado;
    this.cuentaGenerada = "";
    randomize();
  }

  public String getCuentaGenerada() {
    return this.cuentaGenerada;
  }

  public boolean isFound() {
    return encontrado;
  }

  private void format() {
    if (this.apellidoMaterno != null) {
      this.apellidoMaterno = this.apellidoMaterno.trim();
      this.apellidoMaterno = this.apellidoMaterno.replaceAll(" ", "");
      this.apellidoMaterno = this.apellidoMaterno.toLowerCase();
      this.apellidoMaterno = Cadena.sinAcentos(this.apellidoMaterno);
      this.apellidoMaterno = this.apellidoMaterno.replaceAll("ñ", "n");
    } // 
    if (this.apellidoPaterno != null) {
      this.apellidoPaterno = this.apellidoPaterno.trim();
      this.apellidoPaterno = this.apellidoPaterno.replaceAll(" ", "");
      this.apellidoPaterno = this.apellidoPaterno.toLowerCase();
      this.apellidoPaterno = Cadena.sinAcentos(this.apellidoPaterno);
      this.apellidoPaterno = this.apellidoPaterno.replaceAll("ñ", "n");

    } // if
    if (this.nombres != null) {
      this.nombres = this.nombres.trim();
      this.nombres = this.nombres.toLowerCase();
      this.nombres = Cadena.sinAcentos(this.nombres);
      this.nombres = this.nombres.replaceAll("ñ", "n");
    } // if
  }

  private void randomize() {
    format();
    String[] nombreSeparado = this.nombres.split(" ");
    if (this.apellidoPaterno != null) {
      this.cuentaGenerada = concatPointAccount(getStartName(nombreSeparado), this.apellidoPaterno);
      if (toSearch(this.cuentaGenerada)) {
        if (this.apellidoMaterno != null) {
          this.cuentaGenerada = concatPointAccount(getStartName(nombreSeparado), this.apellidoMaterno);
          if (toSearch(this.cuentaGenerada)) {
            this.cuentaGenerada = concatPointAccount(concatAccount(getStartName(nombreSeparado), this.apellidoPaterno), this.apellidoMaterno);
            if (toSearch(this.cuentaGenerada)) {
              this.cuentaGenerada = concatPointAccount(concatAccount(getStartName(nombreSeparado), this.apellidoMaterno), this.apellidoPaterno);
              if (toSearch(this.cuentaGenerada)) {
                this.cuentaGenerada = randomName(nombreSeparado, this.apellidoPaterno);
                if (toSearch(this.cuentaGenerada)) {
                  this.cuentaGenerada = randomName(nombreSeparado, this.apellidoMaterno);
                } // if
              } // if
            } // if 
          } // if
        } // if materno
        else {
          this.cuentaGenerada = concatPointAccount(this.apellidoPaterno, getStartName(nombreSeparado));
          if (toSearch(this.cuentaGenerada)) {
            this.cuentaGenerada = randomName(nombreSeparado, this.apellidoPaterno);
          } // if
        } // else
      } // if paterno
    } // if
    else {
      if (this.apellidoMaterno != null) {
        this.cuentaGenerada = concatPointAccount(getStartName(nombreSeparado), this.apellidoMaterno);
        if (toSearch(this.cuentaGenerada)) {
          this.cuentaGenerada = randomName(nombreSeparado, this.apellidoMaterno);
        } // if 
      } // if materno
    } // else
    if (this.cuentaGenerada != null && toSearch(this.cuentaGenerada)) {
      this.cuentaGenerada = null;
    }
  }

  private String randomName(String[] nombreSeparado, String apellido) {
    String regresar = null;
    String temporal;
    int count = 0;
    try {
      if (isExcludeWord(nombreSeparado[0])) {
        if (isExcludeWord(nombreSeparado[1])) {
          if (isExcludeWord(nombreSeparado[2])) {
            temporal = nombreSeparado[3];
          } // if
          else {
            temporal = nombreSeparado[2];
          } // else
        } // if				
        else {
          temporal = nombreSeparado[1];
        }
      } // if
      else {
        temporal = nombreSeparado[0];
      }
      for (String nombre: nombreSeparado) {
        if (nombreSeparado.length == ++count) {
          if (nombreSeparado.length == 1) {
            regresar = concatPointAccount(temporal.concat(this.apellidoMaterno != null && !this.apellidoMaterno.equals("") ? this.apellidoMaterno.substring(0, 3) : (this.apellidoPaterno != null && !this.apellidoPaterno.equals("") ? this.apellidoPaterno.substring(0, 3) : "xx")), this.apellidoPaterno != null && !this.apellidoPaterno.equals("") ? this.apellidoPaterno : this.apellidoMaterno);
          } // if
          break;
        } // if
        else {
          if (nombreSeparado[count].length() > 2 && !isExcludeWord(nombreSeparado[count])) {
            temporal = concatAccount(temporal, nombreSeparado[count]);
            regresar = concatPointAccount(temporal, apellido);
            if (!toSearch(regresar)) {
              break;
            }
            else {
              regresar = null;
            } // else
          } // if
        } // else
      } // fo
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  private String concatPointAccount(String campo1, String campo2) {
    return campo1.concat(".").concat(campo2);
  }

  private String concatAccount(String campo1, String campo2) {
    return campo1.concat(campo2);
  }

  private boolean toSearch(String cuenta) {
    boolean regresar = false;
    Map<String, Object> params = null;
    List<TcJanalEmpleadosDto> listaTrUsuariosDto;
    try {
      params = new HashMap<String, Object>();
      params.put("cuenta", cuenta);
      listaTrUsuariosDto = DaoFactory.getInstance().findViewCriteria(TcJanalEmpleadosDto.class, params, "findUsuario");
      regresar = !listaTrUsuariosDto.isEmpty();
/*      
      for (TcJanalEmpleadosDto usuario : listaTrUsuariosDto) {
        regresar = !usuario.getIdEmpleado().equals(this.idEmpleado);
        if (regresar == false) {
          break;
        }
      } // for
*/ 
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch
    return regresar;
  }

  private boolean isExcludeWord(String nombre) {
    boolean regresar = false;
    try {
      if (nombre.length() > 2) {
        for (String excluida : PALABRAS_EXCLUIDAS) {
          if (excluida.equals(nombre)) {
            regresar = excluida.equals(nombre);
            break;
          } // false
        } // for
      }
      else {
        regresar = true;
      }
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch

    return regresar;
  }

  private String getStartName(String[] nombres) {
    String regresar = null;
    try {
      for (String nombre : nombres) {
        if (!isExcludeWord(nombre)) {
          regresar = nombre;
          break;
        } // if
      } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // try
    return regresar;
  }

  public static void main(String[] args) {
    RandomCuenta random= new RandomCuenta("ruben", "castilla", "casillas", -1L);
    System.out.println(random.getCuentaGenerada());
  }
  
}
