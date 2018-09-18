package mx.org.kaana.mantic.compras.ordenes.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/05/2018
 *@time 11:32:16 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EOrdenes {
   
	NORMAL("normal"), ALMACEN("del almacen"), PROVEEDOR("por proveedor"), MANUAL("manual");
	
	private String titulo;
	
  private EOrdenes(String titulo) {
    this.titulo= titulo;
  }

	public String getTitulo() {
		return titulo;
	}

}
