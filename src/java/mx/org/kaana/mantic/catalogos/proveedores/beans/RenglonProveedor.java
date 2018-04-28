/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.proveedores.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.Gestor;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;

public class RenglonProveedor implements Serializable {

  private static final long serialVersionUID = -6672874949022315665L;

  private TcManticProveedoresDto tcManticProveedoresDto;
  private UISelectEntity idTipoProveedor;
  private List<UISelectEntity> tiposProveedores;
  private List<Domicilio> domicilios;
  private List<CondicionPago> condicionPagos;
  private List<Responsable> responsables;
  private List<Contacto> contactos;
  private List<Agente> agentes;
  private List<IBaseDto> listaEliminar;

  public RenglonProveedor(Long idProveedor) throws Exception {
    this.tcManticProveedoresDto = new TcManticProveedoresDto(idProveedor);
    this.tiposProveedores = new ArrayList<>();
    this.domicilios = new ArrayList<>();    
    this.listaEliminar = new ArrayList<>();   
    init();
  }

  public List<IBaseDto> getListaEliminar() {
    return listaEliminar;
  }  

  public List<Agente> getAgentes() {
    return agentes;
  }

  
  public List<Responsable> getResponsables() {
    return responsables;
  } 

  public List<Contacto> getContactos() {
    return contactos;
  }  

  public List<CondicionPago> getCondicionPagos() {
    return condicionPagos;
  }

  public List<Domicilio> getDomicilios() {
    return domicilios;
  }

  public UISelectEntity getIdTipoProveedor() {
    return idTipoProveedor;
  }

  public List<UISelectEntity> getTiposProveedores() {
    return tiposProveedores;
  }

  public TcManticProveedoresDto getTcManticProveedoresDto() {
    return tcManticProveedoresDto;
  }

  public void loadTiposProveedores() throws Exception {
    Gestor gestor = new Gestor();
    gestor.loadTiposProveedores(false);
    this.tiposProveedores.addAll(gestor.getTiposProveedores());
    if (!this.tcManticProveedoresDto.getIdTipoProveedor().equals(-1L)) {
      int pos = this.tiposProveedores.indexOf(new UISelectEntity(this.tcManticProveedoresDto.getIdProveedor().toString()));
      this.idTipoProveedor = this.tiposProveedores.get(pos);
    } // if       
  }
  
  public void loadResponsables () throws Exception {
    Gestor gestor = new Gestor();  
    this.responsables=gestor.toResponsablesProvedor(this.tcManticProveedoresDto.getIdProveedor());   
  }
  
  public void loadContactos () throws Exception {
    Gestor  gestor = new Gestor();
    this.contactos = gestor.toContactosProvedor(this.tcManticProveedoresDto.getIdProveedor());
  }
  
  public void loadAgentes () throws Exception {
    Gestor  gestor = new Gestor();
    this.agentes = gestor.toAgentesProvedor(this.tcManticProveedoresDto.getIdProveedor());
  }

  private void init() throws Exception {
    try {
      if (this.tcManticProveedoresDto.isValid()) {
        this.tcManticProveedoresDto = (TcManticProveedoresDto) DaoFactory.getInstance().findById(TcManticProveedoresDto.class, this.tcManticProveedoresDto.getIdProveedor());
      }
      loadDomicilios();
      loadCondicionesPago();
      loadResponsables();
      loadContactos();
      loadAgentes();
    } // try
    catch (Exception e) {
      throw e;
    }// catch    
  }

  public void loadDomicilios() throws Exception {
    Gestor gestor = new Gestor();
    try {
      gestor.loadDirecciones(this.tcManticProveedoresDto.getIdProveedor());
      this.domicilios.addAll(gestor.getDirecciones());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  public void loadCondicionesPago() throws Exception {
    Gestor gestor = new Gestor();
    try {
      this.condicionPagos = gestor.toCondicionesPagoProveedor(this.tcManticProveedoresDto.getIdProveedor());
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  public void addDomicilio(Domicilio domicilio) {
   this.domicilios.add(0,domicilio);
  }

}
