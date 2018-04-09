package mx.org.kaana.libs.motor;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Jul 2, 2012
 *@time 4:23:56 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import mx.org.kaana.kajool.enums.ETiposColumnasSql;
import java.util.Map;
import mx.org.kaana.libs.formato.Cadena;

public class RenglonCampo {

  private String campo;
  private String tipo;
  private String atributo;
  private String clase;
	private boolean nemonico;

  public RenglonCampo(String campo, String tipo, Map<String,Integer> auxiliar, boolean nemonico) {
    setCampo(campo);
		setNemonico(nemonico);
    establecerTipoColumna(recuperarTipoColumna(tipo) );		
		setAtributo(isNemonico() ? Cadena.toBeanNameNemonico(getCampo()) : Cadena.toBeanNameEspecial(getCampo()));
		setClase(isNemonico() ? Cadena.toClassNameNemonico(getCampo()) : Cadena.toClassName(getCampo()));		
    validarDuplicidad(auxiliar);
  }

  public void setCampo(String campo) {
    this.campo = campo;
  }

  public String getCampo() {
    return campo;
  }
	
	private void setTipo(String tipo) {
    this.tipo = tipo;
  }

	public boolean isNemonico() {
		return nemonico;
	}

	public void setNemonico(boolean nemonico) {
		this.nemonico=nemonico;
	}

  public String getTipo() {
    return tipo;
  }

  public void setAtributo(String atributo) {
    this.atributo = atributo;
  }

  public String getAtributo() {
    return atributo;
  }

  private void setClase(String clase) {
    this.clase = clase;
  }

  public String getClase() {
    return clase;
  }

  private ETiposColumnasSql recuperarTipoColumna(String tipo) {
    ETiposColumnasSql [] tipos = ETiposColumnasSql.values();
    ETiposColumnasSql regresar = null;
    for (ETiposColumnasSql type : tipos) {
      if ( tipo.contains(type.getTipo()) ) {
        regresar = type;
        break;
      }
    } // for
    return regresar;
  }

  private void establecerTipoColumna(ETiposColumnasSql tipoColumna) {
    switch(tipoColumna) {
      case NUMBER:
      case INT:
      case INTEGER:
        this.tipo = "Long";
        break;
      case VARCHAR:
      case VARCHAR2:
        this.tipo = "String";
        break;
      case TIMESTAMP:
        this.tipo = "Timestamp";
        break;
      case DATE:
        this.tipo = "Date";
				break;
			case BLOB:
				this.tipo = "Blob";
				break;
      case DOUBLE:
        this.tipo = "Double";
        break;
      default:
        this.tipo = "Long";
    } // switch
  }

  public String toString() {
    StringBuilder sb= new StringBuilder();
    sb.append("[");
    sb.append(getCampo());
    sb.append(",");
    sb.append(getTipo());
    sb.append(",");
    sb.append(getAtributo());
    sb.append(",");
    sb.append(getClase());
    sb.append("]");
    return sb.toString();
  } // toString

  private void validarDuplicidad(Map<String, Integer> auxiliar) {
    Integer contador= 0;
    if(auxiliar.get(Cadena.toBeanNameEspecial(getCampo()))!= null) {
      contador= auxiliar.get(Cadena.toBeanNameEspecial(getCampo()));
      auxiliar.put(Cadena.toBeanNameEspecial(getCampo()), contador==0 ? contador+2 : contador+1);
    } // if
    else
      auxiliar.put(Cadena.toBeanNameEspecial(getCampo()), 0);
  }

}
