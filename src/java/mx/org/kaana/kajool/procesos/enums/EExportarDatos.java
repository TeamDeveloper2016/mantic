package mx.org.kaana.kajool.procesos.enums;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 12/10/2016
 *@time 02:46:37 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.procesos.reportes.reglas.IBaseExporter;

public enum EExportarDatos implements IBaseExporter{
  
	TODOS				("CapturaCuestionarios", new String[]{"trJanalVisitas","trJanalCaratula","trJanalPersonas","trJanalFamilias","trJanalModulo","tcJanalEntidades","tcJanalMunicipios","tcJanalLocalidades"}, "Captura", new String[]{"tr_visitas","tr_caratula","tr_personas","tr_familia","tr_modulo","tc_entidades","tc_municipios","tc_localidades"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"0","1","2","3","4","5","6","7"}),
	VISITAS			("CapturaCuestionarios", new String[]{"trJanalVisitas"}, "Captura", new String[]{"tr_visitas"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"0"}),
  CARATULA		("CapturaCuestionarios", new String[]{"trJanalCaratula"}, "Captura", new String[]{"tr_caratula"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"1"}),
  PERSONAS		("CapturaCuestionarios", new String[]{"trJanalPersonas"}, "Captura", new String[]{"tr_personas"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"2"}),
  FAMILIA			("CapturaCuestionarios", new String[]{"trJanalFamilias"}, "Captura", new String[]{"tr_familia"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"3"}),
  MODULO			("CapturaCuestionarios", new String[]{"trJanalModulo"}, "Captura", new String[]{"tr_modulo"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"4"}),
	ENTIDADES		("CapturaCuestionarios", new String[]{"tcJanalEntidades"}, "Captura", new String[]{"tc_entidades"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"5"}),
	MUNICIPIOS  ("CapturaCuestionarios", new String[]{"tcJanalMunicipios"}, "Captura", new String[]{"tc_municipios"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"6"}),
	LOCALIDADES ("CapturaCuestionarios", new String[]{"tcJanalLocalidades"}, "Captura", new String[]{"tc_localidades"}, "/Paginas/Explotacion/filtro","informacion","", new String[]{"7"});
	
  private static final Map<Integer, EExportarDatos> lookup= new HashMap<>();
	private String proceso;
  private String[] idXml;
  private String descripcion;
  private String[] dbfFile;
  private String paginaRegreso;
  private String archivoZip;
  private String patron;
	private String[] indice;

	private EExportarDatos(String proceso, String[] idXml, String descripcion, String[] dbfFile, String paginaRegreso, String archivoZip, String patron, String[] indice) {
		this.proceso      = proceso;
		this.idXml        = idXml;
		this.descripcion  = descripcion;
		this.dbfFile      = dbfFile;
		this.paginaRegreso= paginaRegreso;
		this.archivoZip   = archivoZip;
		this.patron       = patron;
		this.indice       = indice;
	}

  static {
    for (EExportarDatos item: EnumSet.allOf(EExportarDatos.class)) 
      lookup.put(item.ordinal()+ 1, item);    
  } // static
  
	@Override
	public String getProceso() {
		return proceso;
	}

	@Override
	public String[] getIdXml() {
		return idXml;
	}

	@Override
	public String getDescripcion() {
		return descripcion;
	}

	@Override
	public String[] getDbfFile() {
		return dbfFile;
	}

	@Override
	public String getPaginaRegreso() {
		return paginaRegreso;
	}

	@Override
	public String getArchivoZip() {
		return archivoZip;
	}

	@Override
	public String getPatron() {
		return patron;
	} 

	public String[] getIndice() {
		return indice;
	}
  
  public static EExportarDatos fromOrdinal(Long ordinal) {
    return lookup.get(ordinal.intValue());
  } 
}
