package mx.org.kaana.mantic.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.procesos.enums.ETipoGrafica;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;

public enum EGraficasTablero {

  UTILIDAD_SUCURSAL(" ", "Utilidad", "total", "Sucursales", "sucursal", "utilidadSucursales", " ", ETipoGrafica.COLUMNA, "270", 110, 1, Constantes.SQL_TODOS_REGISTROS, "tc_mantic_ventas"),  
  UTILIDAD_CAJA    (" ", "Utilidad", "utilidadCaja", "Cajas", "caja", "utilidadCaja", " ", ETipoGrafica.LINEAL, "200", 80, 1, Constantes.SQL_TODOS_REGISTROS, "tc_mantic_ventas"),    
	CUENTAS_COBRAR   (" ", "Importe / Saldo", "importe~saldo", "Clientes", "cliente", "cuentasCobrar", " ", ETipoGrafica.COLUMNA, "270", 80, 2, 5L, "tc_mantic_clientes_deudas"),
  CUENTAS_PAGAR    (" ", "Importe / Saldo", "importe~saldo", "Proveedores", "proveedor", "cuentasPagar", " ", ETipoGrafica.COLUMNA, "270", 80, 2, 5L, "tc_mantic_empresas_deudas"),    
	VENTAS_SUCURSAL  ("Total de ventas por sucursal", "Ventas", "total", "Sucursales", "sucursal", "ventasSucursal", " ", ETipoGrafica.BARRAS, "180", 80, 1, Constantes.SQL_TODOS_REGISTROS, "tc_mantic_ventas"),
	ART_MAS_VENDIDOS ("Articulos con mas ventas", "Total", "total", "Articulo", "articulo", "articulosMasVentas", " ", ETipoGrafica.PIE, "313", 80, 1, 10L, "tc_mantic_ventas"),
	ART_MAS_UTILIDAD ("Articulos con mas utilidad", "Total", "total", "Articulo", "articulo", "articulosMasUtilidad", " ", ETipoGrafica.PIE, "313", 80, 1, 10L, "tc_mantic_ventas"),  
	VENTAS_EMPLEADO  ("Ventas por empleado", "", "", "", "", "ventasPorEmpleado", " ", ETipoGrafica.COLUMNA, "313", 80, 1, 5L, "tc_mantic_ventas");  
  
	private static final String vista= "VistaIndicadoresTableroDto";
  private String tituloGeneral;
	private String tituloLadox;
	private String aliasLadox;
	private String tituloLadoy;
	private String aliasLadoy;	
	private String idVista;
	private String descripcion;
	private ETipoGrafica tipoChart;
	private String height;
	private Integer sizeY;
	private Integer numColumnas;
	private Long records;
	private String tablaPivote;
	private static final Map<String, EGraficasTablero> lookup= new HashMap<>();

  static {
    for (EGraficasTablero item: EnumSet.allOf(EGraficasTablero.class)) 
      lookup.put(item.name(), item);    
  } // static   

	private EGraficasTablero(String tituloGeneral, String tituloLadox, String aliasLadox, String tituloLadoy, String aliasLadoy, String idVista, String descripcion, ETipoGrafica tipoChart, String height, Integer sizeY, Integer numColumnas, Long records, String tablaPivote) {
		this.tituloGeneral= tituloGeneral;
		this.tituloLadox  = tituloLadox;
		this.aliasLadox   = aliasLadox;
		this.tituloLadoy  = tituloLadoy;
		this.aliasLadoy   = aliasLadoy;
		this.idVista      = idVista;
		this.descripcion  = descripcion;
		this.tipoChart    = tipoChart;
		this.height       = height;
		this.sizeY        = sizeY;
		this.numColumnas  = numColumnas;
		this.records      = records;
		this.tablaPivote  = tablaPivote;
	}		

	public static String getVista() {
		return vista;
	}

	public String getTituloGeneral() {
		return tituloGeneral;
	}

	public String getTituloLadox() {
		return tituloLadox;
	}

	public String getAliasLadox() {
		return aliasLadox;
	}

	public String getTituloLadoy() {
		return tituloLadoy;
	}

	public String getAliasLadoy() {
		return aliasLadoy;
	}

	public String getIdVista() {
		return idVista;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public ETipoGrafica getTipoChart() {
		return tipoChart;
	}	

	public String getHeight() {
		return height;
	}

	public Integer getSizeY() {
		return sizeY;
	}

	public Integer getNumColumnas() {
		return numColumnas;
	}	

	public Long getRecords() {
		return records;
	}	

	public String getTablaPivote() {
		return tablaPivote;
	}	
	
	public static EGraficasTablero fromNameTablero(String name) {
    return lookup.get(name);
  } // fromNameTablero
	
	public String getIdPivote(){
		return Cadena.toNameBean(idVista);
	}
}