package mx.org.kaana.kajool.beans;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date Sep 14, 2015
 * @time 5:12:08 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */


public class GradosGrupos  {

  private int id;
  private String clave;
  private String mapa;
  private String ciclo;
  private String grado;
  private String grupo;
  private Long maximoAlumnos;

  public GradosGrupos(int id, String clave, String mapa, String ciclo, String grado, String grupo, Long maximoAlumnos) {
    this.id = id;
    this.clave = clave;
    this.mapa = mapa;
    this.ciclo = ciclo;
    this.grado = grado;
    this.grupo = grupo;
    this.maximoAlumnos = maximoAlumnos;
  }

  public GradosGrupos() {
    this(-1,null,null,null,null,null,null);
  }


  public void setCiclo(String ciclo) {
    this.ciclo = ciclo;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public void setGrado(String grado) {
    this.grado = grado;
  }

  public void setGrupo(String grupo) {
    this.grupo = grupo;
  }

  public void setMapa(String mapa) {
    this.mapa = mapa;
  }

  public void setMaximoAlumnos(Long maximoAlumnos) {
    this.maximoAlumnos = maximoAlumnos;
  }

  public String getCiclo() {
    return ciclo;
  }

  public String getClave() {
    return clave;
  }

  public String getGrado() {
    return grado;
  }

  public String getGrupo() {
    return grupo;
  }

  public String getMapa() {
    return mapa;
  }

  public Long getMaximoAlumnos() {
    return maximoAlumnos;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  }
