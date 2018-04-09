package mx.org.kaana.kajool.procesos.plantillas.formularios.backing;

/**
 *@company Instituto Nacional de Estadistica y Geografia
 *@project KAJOOL (Control system polls)
 *@date 5/06/2014
 *@time 05:28:22 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name="kajoolPlantillasFormulariosDinamico")
@ViewScoped
public class Dinamico implements Serializable {

  private static final Log LOG = LogFactory.getLog(Dinamico.class);

  private String mascara;
  private String validaciones;
  private String mensaje;
  private Object entrada;

  public String getMascara() {
    return mascara;
  }

  public void setMascara(String mascara) {
    this.mascara = mascara;
  }

  public String getValidaciones() {
    return validaciones;
  }

  public void setValidaciones(String validaciones) {
    this.validaciones = validaciones;
  }

  public String getMensaje() {
    return mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public Object getEntrada() {
    return entrada;
  }

  public void setEntrada(Object entrada) {
    this.entrada = entrada;
  }

  public List<String> getMasks() {
    return Arrays.asList("libre", "fecha", "fecha-hora", "registro", "hora", "hora-completa", "tarjeta-credito", "decimal", "decimal-signo", "letras", "vocales", "texto", "numero", "un-digito", "dos-digitos", "tres-digitos", "tres-digitos-default", "cuatro-digitos", "cinco-digitos", "siete-digitos", "diez-digitos", "entero", "entero-blanco", "entero-signo", "entero-sin-signo", "flotante", "flotante-signo", "rfc", "curp", "moneda", "moneda-decimal", "mayusculas", "minusculas", "cuenta", "numeros-letras", "nombre-dto", "telefono", "ip", "version", "clave-ct-call-center", "clave-ct", "clave-operativa", "resultado-entrevista-basico", "resultado-entrevista-modulo", "no-aplica");
  }

  @PostConstruct
  private void init() {
    this.mascara= "entero";
    this.validaciones= "requerido|esta-en({\"valores\": \"1,3,5-8,10\"})";
    this.entrada= "9";
  }

}
