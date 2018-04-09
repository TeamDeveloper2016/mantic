package mx.org.kaana.libs.motor;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 4, 2012
 *@time 2:12:39 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.text.MessageFormat;
import java.util.List;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.motor.beans.DetalleMotor;
import mx.org.kaana.kajool.enums.ETipoGeneracion;

public class Generacion {

  private ETipoGeneracion tipoGeneracion;
  private DetalleMotor detalleMotor;
  private boolean kajoolLocal;

  private static final String NO_APLICA = "No aplica";

  public Generacion(DetalleMotor detalleMotor, boolean kajoolLocal) {
    this(ETipoGeneracion.TABLA, detalleMotor, kajoolLocal);
  }

  public Generacion(ETipoGeneracion tipoGeneracion, DetalleMotor detalleMotor,  boolean kajoolLocal) {
    this.tipoGeneracion = tipoGeneracion;
    this.detalleMotor   = detalleMotor;
    this.kajoolLocal     = kajoolLocal;
  }

  public ETipoGeneracion getTipoGeneracion() {
    return tipoGeneracion;
  }

  public void setTipoGeneracion(ETipoGeneracion tipoGeneracion) {
    this.tipoGeneracion=tipoGeneracion;
  }

  public DetalleMotor getDetalleMotor() {
    return detalleMotor;
  }

  public void setDetalleMotor(DetalleMotor detalleMotor) {
    this.detalleMotor=detalleMotor;
  }

  public List<String> getDefinicionCamposLlave() throws Exception {
    GeneracionConsulta generacionConsulta = null;
    List<String> definicionCamposLlave    = null;
    try {
      generacionConsulta = new GeneracionConsulta(this.detalleMotor);
      if ( generacionConsulta.validarConsulta() ) {
        definicionCamposLlave = generacionConsulta.recuperarCamposLLave();
      }
    }
    catch(Exception e) {
      Error.mensaje(e);
      throw e;
    }
    return definicionCamposLlave;
  }

  public boolean registrar() throws Exception {
    boolean regresar = false;

    switch(this.tipoGeneracion) {
      case TABLA :
        registrarTabla();
        break;
      case CONSULTA :
        registrarConsulta();
        break;
      case ENCUESTA :
        registrarEncuesta();
        break;
    } // switch

    return regresar;
  }

  private void registrarTabla() throws Exception {
    GeneratorTable generatorTable = null;
    String paqueteCompleto        = null;
		String llavePrimaria          = null;
    try {
      paqueteCompleto = this.detalleMotor.getNombreEncuesta().equals(NO_APLICA) ? this.detalleMotor.getPaquete() : this.detalleMotor.getPaquete();
			//llavePrimaria   = "id".concat(this.detalleMotor.getNombreTabla().toLowerCase().replace("tr_janal", "").replace("tc_janal", ""));
      if (this.detalleMotor.getEsSinonimo().equals("1"))
        generatorTable = new GeneratorTable(this.detalleMotor.getTablaSinonimo(), paqueteCompleto.toLowerCase(), true, this.detalleMotor.getLlaveSinonimo(), this.detalleMotor.getEsNemonico().equals("1"), this.detalleMotor.getConDao().equals("1"), this.detalleMotor.getNombreEncuesta());
      else {
        generatorTable = new GeneratorTable(this.detalleMotor.getNombreTabla(), paqueteCompleto.toLowerCase(), false, llavePrimaria, this.detalleMotor.getEsNemonico().equals("1"), this.detalleMotor.getConDao().equals("1"), this.detalleMotor.getNombreEncuesta());
        generatorTable.setTablaDto(true);
      }

      generatorTable.generator(this.kajoolLocal);
      this.detalleMotor.setResultadoDto(generatorTable.getEscritura());
      this.detalleMotor.setResultadoXML(generatorTable.getSqlXml());
    } // try
    catch(Exception e) {
      throw e;
    }
  }

  private void registrarConsulta() throws Exception {
    GeneratorQuery generatorQuery  = null;
    String paqueteCompleto         = null;
    final String NOMBRE_VISTA      = "Vista{0}";
    try {
      paqueteCompleto = this.detalleMotor.getNombreEncuesta().equals(NO_APLICA) ? this.detalleMotor.getPaquete().concat(".view") : this.detalleMotor.getPaquete().concat(".").concat(this.detalleMotor.getNombreEncuesta());
      generatorQuery= new GeneratorQuery(MessageFormat.format(NOMBRE_VISTA, new Object[] { this.detalleMotor.getNombreTabla() }), this.detalleMotor.getResultadoXML(), paqueteCompleto.toLowerCase(), this.detalleMotor.getCampoLlave(), this.detalleMotor.getEsBean().equals("1"), this.detalleMotor.getEsNemonico().equals("1"), this.detalleMotor.getNombreEncuesta());
      generatorQuery.generator(this.kajoolLocal);
      this.detalleMotor.setResultadoXMLConsulta(generatorQuery.getSqlXml());
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      throw e;
    }
  }

  private void registrarEncuesta() throws Exception {
    try {

    } // try
    catch(Exception e) {
      Error.mensaje(e);
      throw e;
    }
  }


}
