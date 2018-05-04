/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.mantic.catalogos.proveedores.reglas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Agente;
import mx.org.kaana.mantic.catalogos.proveedores.beans.CondicionPago;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Contacto;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Domicilio;
import mx.org.kaana.mantic.catalogos.proveedores.beans.Responsable;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorPagoDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorPersonaDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedorTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticProveedoresAgentesDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class Transaccion extends IBaseTnx {

  private static final Log LOG = LogFactory.getLog(Transaccion.class);
  
  private TcManticProveedoresDto tcMantiProveedorDto;
  private List<Domicilio> domicilios;
  private List<Agente> agentes;
  private List<Contacto> contactos;
  private List<CondicionPago> condicionPago;
  private List<Responsable> responsables;
  private List<IBaseDto> eliminados; 
  private IBaseDto dtoSingleDepura; 
  
  private String notificacion; 
  
  public Transaccion (IBaseDto dtoSingleDepura ) {
    this.dtoSingleDepura = dtoSingleDepura;
  }
  
  public Transaccion (TcManticProveedoresDto tcMantiProveedorDto) {
     this.tcMantiProveedorDto = tcMantiProveedorDto;
  }
  
  public Transaccion (TcManticProveedoresDto tcMantiProveedorDto, List<Domicilio> domicilios, List<Agente> agentes,List<Contacto> contactos, List<CondicionPago> condicionPago,List<Responsable> responsables) {
    this(tcMantiProveedorDto,domicilios,agentes,contactos,condicionPago,responsables,null);
  }
  
  public Transaccion (TcManticProveedoresDto tcMantiProveedorDto, List<Domicilio> domicilios, List<Agente> agentes,List<Contacto> contactos, List<CondicionPago> condicionPago,List<Responsable> responsables, List<IBaseDto> eliminados) {
    this.tcMantiProveedorDto =tcMantiProveedorDto;
    this.domicilios = domicilios;
    this.agentes = agentes;
    this.contactos  = contactos;
    this.condicionPago = condicionPago;
    this.responsables = responsables; 
    this.eliminados = eliminados;
  }

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    switch (accion) {
      case AGREGAR:
        agregar(sesion);
      break;  
      case MODIFICAR:
        actualizar(sesion);
      break;  
      case ELIMINAR:
        eliminar(sesion);
      break;
      case DEPURAR:
        regresar = DaoFactory.getInstance().delete(sesion,this.dtoSingleDepura)>=0L;
      break;  
    } // switch
    return regresar;
  }

  private void procesaTodosEliminados (Session session) throws Exception {
    try {
       for (IBaseDto record : this.eliminados) {
         DaoFactory.getInstance().delete(session, record);
       }// for
    } // try
    catch (Exception e) {
      throw e;
    }// catch
  }
  
  private void procesaCondicionPagos(Session session, Long idProveedor) throws Exception {
    try {
      for (CondicionPago condicionPago : this.condicionPago) {
        switch(condicionPago.getAccion()){
          case INSERT:
             condicionPago.setIdProveedor(idProveedor);             
             DaoFactory.getInstance().insert(session, condicionPago);
          break;  
          case UPDATE :
           condicionPago.setIdUsuario(this.tcMantiProveedorDto.getIdUsuario());
           DaoFactory.getInstance().update(session,condicionPago);         
        } // switch       
      }// for
    } // try
    catch (Exception e) {      
      throw e;
    } // catch
  }
  private void procesaResponsables(Session session, Long idProveedor) throws Exception {
    try {
      for (Responsable responsable : this.responsables) {
        switch(responsable.getAccion()){
          case INSERT:
             responsable.setIdProveedor(idProveedor);
             DaoFactory.getInstance().insert(session, responsable);
          break;  
          case UPDATE :
            responsable.setIdUsuario(this.tcMantiProveedorDto.getIdUsuario());
            DaoFactory.getInstance().update(session,responsable);         
        } // switch       
      }// for
    } // try
    catch (Exception e) {
      
      throw e;
    } // catch
  }
  
  private void procesaContacto(Session session, Long idProveedor) throws Exception {
    try {
      for (Contacto contacto : this.contactos) {
        switch(contacto.getAccion()){
          case INSERT:
             contacto.setIdProveedor(idProveedor);
             DaoFactory.getInstance().insert(session, contacto);
          break;  
          case UPDATE :
            contacto.setIdUsuario(this.tcMantiProveedorDto.getIdUsuario());
            DaoFactory.getInstance().update(session,contacto);         
        } // switch       
      }// for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }
  
   private void procesaAgentes(Session session, Long idProveedor) throws Exception {
    try {
      for (Agente agente : this.agentes) {
        switch(agente.getAccion()){
          case INSERT:
             agente.setIdProveedor(idProveedor);
             DaoFactory.getInstance().insert(session, agente);
          break;  
          case UPDATE :
            agente.setIdUsuario(tcMantiProveedorDto.getIdUsuario());
            DaoFactory.getInstance().update(session,agente);   
          break;  
        } // switch       
      }// for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }
  
  private void procesaDomicilios(Session session, Long idProveedor) throws Exception {
    try {
      for (Domicilio domicilio : this.domicilios) {
        switch(domicilio.getAccion()){
          case INSERT:
             domicilio.setIdProveedor(idProveedor);
             if (domicilio.getIdDomicilio().equals(-1L)){
               DaoFactory.getInstance().insert(session,domicilio.getTcManticDomicilioDto()); 
               domicilio.setIdDomicilio(domicilio.getTcManticDomicilioDto().getIdDomicilio());
             }  
             DaoFactory.getInstance().insert(session, domicilio);
          break;  
          case UPDATE :            
            domicilio.setIdUsuario(tcMantiProveedorDto.getIdUsuario());
            DaoFactory.getInstance().update(session,domicilio);         
            DaoFactory.getInstance().update(session,domicilio.getTcManticDomicilioDto());         
        } // switch       
      }// for
    } // try
    catch (Exception e) {
      throw e;
    } // catch
  }

  private void agregar(Session session) throws Exception {
    Long idProveedor = null;
    try {
      LOG.info("Agregando provedor[".concat(this.tcMantiProveedorDto.toMap().toString()).concat("]"));
      idProveedor = DaoFactory.getInstance().insert(session, this.tcMantiProveedorDto);
      if (idProveedor >= 1L) {
        LOG.info("Agregando domicilios[".concat(this.domicilios.toString()).concat("]"));
        procesaDomicilios(session, idProveedor);
        LOG.info("Agregando agentes[".concat(this.agentes.toString()).concat("]"));
        procesaAgentes(session, idProveedor);
        LOG.info("Agregando condicionPagos[".concat(this.domicilios.toString()).concat("]"));
        procesaCondicionPagos(session, idProveedor);
        LOG.info("Agregando contactos[".concat(this.domicilios.toString()).concat("]"));
        procesaContacto(session, idProveedor);
        LOG.info("Agregando responsables[".concat(this.responsables.toString()).concat("]"));
        procesaResponsables(session,idProveedor);
      } // if
    } // try
    catch (Exception e) {
      throw e;
    }// catch    
  } 
  
  private void actualizar(Session session) throws Exception {
    Long idProveedor = null;
    try {
      LOG.info("Actualizando[".concat(this.tcMantiProveedorDto.toMap().toString() ).concat("]"));
      idProveedor = DaoFactory.getInstance().update(session, this.tcMantiProveedorDto);
      if (idProveedor >= 1L) {
        procesaDomicilios(session, idProveedor);
        procesaAgentes(session, idProveedor);
        procesaCondicionPagos(session, idProveedor);
        procesaContacto(session, idProveedor);
        procesaResponsables(session, idProveedor);
        procesaTodosEliminados(session);
      } // if      
    } // try
    catch (Exception e) {
      throw e;
    }// catch    
  }
  
  private void eliminar (Session session) throws Exception {
    Map<String,Object>  params = null;
    try {
      params = new HashMap<>();
      LOG.info("Eliminando prioveedor[".concat( this.tcMantiProveedorDto.getKey().toString()).concat("]"));
      params.put("idProveedor", this.tcMantiProveedorDto.getKey());
      DaoFactory.getInstance().deleteAll(session, TrManticProveedorDomicilioDto.class, params);
      DaoFactory.getInstance().deleteAll(session, TrManticProveedorPersonaDto.class, params);
      DaoFactory.getInstance().deleteAll(session, TrManticProveedorTipoContactoDto.class, params);
      DaoFactory.getInstance().deleteAll(session, TrManticProveedorPagoDto.class, params);
      DaoFactory.getInstance().deleteAll(session, TrManticProveedoresAgentesDto.class, params);
      DaoFactory.getInstance().delete(session,TcManticProveedoresDto.class, this.tcMantiProveedorDto.getKey());      
    } // try
    catch (Exception e) {
      throw e;
    } // catch
    finally {
      Methods.clean(params);
    }
  }

}
