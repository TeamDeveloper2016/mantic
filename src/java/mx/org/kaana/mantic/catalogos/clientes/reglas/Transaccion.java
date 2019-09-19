package mx.org.kaana.mantic.catalogos.clientes.reglas;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.dto.IBaseDto;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.libs.facturama.reglas.CFDIGestor;
import mx.org.kaana.libs.facturama.reglas.TransaccionFactura;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import mx.org.kaana.mantic.catalogos.articulos.beans.Importado;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteContactoRepresentante;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteDomicilio;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.clientes.beans.RegistroCliente;
import mx.org.kaana.mantic.catalogos.personas.beans.PersonaTipoContacto;
import mx.org.kaana.mantic.db.dto.TcManticClientesArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDeudasDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesPagosArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticDomiciliosDto;
import mx.org.kaana.mantic.db.dto.TcManticPersonasDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteDomicilioDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteTipoContactoDto;
import mx.org.kaana.mantic.db.dto.TrManticClienteRepresentanteDto;
import mx.org.kaana.mantic.db.dto.TrManticPersonaTipoContactoDto;
import mx.org.kaana.mantic.enums.ETipoPersona;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import org.hibernate.Session;
import mx.org.kaana.mantic.inventarios.entradas.beans.Nombres;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transaccion extends TransaccionFactura {

	private static final Log LOG=LogFactory.getLog(Transaccion.class);
	
	private static final String ESTILO= "sentinel";
  private IBaseDto dto;
  private RegistroCliente registroCliente;
  private String messageError;
	private Importado file;
	private TcManticClientesDto cliente;
	private TcManticClientesDeudasDto clienteDeuda;
	private Long representante;
	private Long idClientePago;

	public Transaccion(IBaseDto dto) {
		this.dto = dto;
	}
	
  public Transaccion(RegistroCliente registroCliente) {
    this.registroCliente = registroCliente;
  }

	public Transaccion(Importado file, TcManticClientesDto cliente, Long representante) {
		this.file         = file;
		this.cliente      = cliente;
		this.representante= representante;
	}
	
	public Transaccion(Importado file, TcManticClientesDeudasDto clienteDeuda, Long idClientePago) {
		this.file         = file;
		this.clienteDeuda = clienteDeuda;
		this.idClientePago= idClientePago;
	}

  @Override
  protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {
    boolean regresar = false;
    try {
			if(this.registroCliente!= null)
				this.registroCliente.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      switch (accion) {
        case AGREGAR:
          regresar = procesarCliente(sesion);
          break;
        case MODIFICAR:
          regresar = actualizarCliente(sesion);
          break;
        case ELIMINAR:
          regresar = eliminarCliente(sesion);
          break;
				case DEPURAR:
					regresar= DaoFactory.getInstance().delete(sesion, this.dto)>= 1L;
					break;
				case REGISTRAR:
					regresar= true;
					toUpdateDeleteFile(sesion);
					break;
				case SUBIR:
					regresar= true;
					toUpdateDeleteFilePago(sesion);
					break;
      } // switch
      if (!regresar) {
        throw new Exception("");
      }
    } // try
    catch (Exception e) {
      throw new Exception(this.messageError.concat("<br/>")+ e);
    } // catch		
    return regresar;
  } // ejecutar

	@Override
  public boolean procesarCliente(Session sesion) throws Exception {
    boolean regresar = false;
    Long idCliente   = -1L;    
		this.messageError= "Error al registrar el cliente";
		if (eliminarRegistros(sesion)) {
			this.registroCliente.getCliente().setIdUsuario(JsfBase.getIdUsuario());
			this.registroCliente.getCliente().setIdEmpresa(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			idCliente = DaoFactory.getInstance().insert(sesion, this.registroCliente.getCliente());
			if (registraClientesDomicilios(sesion, idCliente)) {
				if (registraClientesRepresentantes(sesion, idCliente)) {
					regresar = registraClientesTipoContacto(sesion, idCliente);
				}
			} // if
		} // if
		sesion.flush();
		if(idCliente > -1)
			registraClienteFacturama(sesion, idCliente);    
    return regresar;
  } // procesarCliente

	private void registraClienteFacturama(Session sesion, Long idCliente){		
		CFDIGestor gestor     = null;
		ClienteFactura cliente= null;
		try {
			gestor= new CFDIGestor(idCliente);
			cliente= gestor.toClienteFactura(sesion);
			setCliente(cliente);
			super.procesarCliente(sesion);
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
	} // registraArticuloFacturama
	
	private void actualizarClienteFacturama(Session sesion, Long idCliente){		
		CFDIGestor gestor     = null;
		ClienteFactura cliente= null;
		try {
			gestor= new CFDIGestor(idCliente);
			cliente= gestor.toClienteFactura(sesion);
			setCliente(cliente);
			if(cliente.getIdFacturama()!= null)
				updateCliente(sesion);
			else
				super.procesarCliente(sesion);
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
	} // actualizarArticuloFacturama
	
	private void eliminarClienteFacturama(Session sesion, String idCliente){						
		try {			
			if(idCliente!= null && Long.valueOf(idCliente)>= 1L)
				removeCliente(sesion, idCliente);
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
	} // actualizarArticuloFacturama
	
  private boolean actualizarCliente(Session sesion) throws Exception {
    boolean regresar = false;
    Long idCliente = this.registroCliente.getIdCliente();
		if (registraClientesDomicilios(sesion, idCliente)) {
			if (registraClientesRepresentantes(sesion, idCliente)) {
				if (registraClientesTipoContacto(sesion, idCliente)) {
					regresar = DaoFactory.getInstance().update(sesion, this.registroCliente.getCliente()) >= 1L;
					sesion.flush();
					actualizarClienteFacturama(sesion, this.registroCliente.getIdCliente());
				}
			} // if
		} // if    
    return regresar;
  } // actualizarCliente

  private boolean eliminarCliente(Session sesion) throws Exception {
    boolean regresar          = false;
    Map<String, Object> params= null;
    try {
      params = new HashMap<>();
      params.put("idCliente", this.registroCliente.getIdCliente());
      if (DaoFactory.getInstance().deleteAll(sesion, TrManticClienteDomicilioDto.class, params) > -1L) {
        if (DaoFactory.getInstance().deleteAll(sesion, TrManticClienteRepresentanteDto.class, params) > -1L) {
          if (DaoFactory.getInstance().deleteAll(sesion, TrManticClienteTipoContactoDto.class, params) > -1L) {
            regresar = DaoFactory.getInstance().delete(sesion, TcManticClientesDto.class, this.registroCliente.getIdCliente()) >= 1L;
						eliminarClienteFacturama(sesion, this.registroCliente.getCliente().getIdFacturama());
          }
        } // if
      } // if
    } // try // try    
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } // eliminarCliente

  private boolean registraClientesDomicilios(Session sesion, Long idCliente) throws Exception {
    TrManticClienteDomicilioDto dto = null;
    ESql sqlAccion    = null;
    int count         = 0;
    int countPrincipal= 0;
    boolean validate  = false;
    boolean regresar  = false;
    try {
			if(this.registroCliente.getClientesDomicilio().size()== 1)
					this.registroCliente.getClientesDomicilio().get(0).setIdPrincipal(1L);
      for (ClienteDomicilio clienteDomicilio : this.registroCliente.getClientesDomicilio()) {								
				if(clienteDomicilio.getIdPrincipal().equals(1L))
					countPrincipal++;
				if(countPrincipal== 0 && this.registroCliente.getClientesDomicilio().size()-1 == count)
					clienteDomicilio.setIdPrincipal(1L);
        clienteDomicilio.setIdCliente(idCliente);
        clienteDomicilio.setIdUsuario(JsfBase.getIdUsuario());
				clienteDomicilio.setIdDomicilio(toIdDomicilio(sesion, clienteDomicilio));		
        dto = (TrManticClienteDomicilioDto) clienteDomicilio;
        sqlAccion = clienteDomicilio.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
						dto.setIdPrincipal(dto.getIdPrincipal().equals(1L) ? dto.getIdPrincipal() : 2L);
            dto.setIdClienteDomicilio(-1L);
            validate = registrar(sesion, dto);
            break;
          case UPDATE:
            validate = actualizar(sesion, dto);
            break;
        } // switch
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.registroCliente.getClientesDomicilio().size();
    } // try    
    finally {
      this.messageError = "Error al registrar los domicilios, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesDomicilios

  private boolean registraClientesRepresentantes(Session sesion, Long idCliente) throws Exception {
    TrManticClienteRepresentanteDto dto = null;
    ESql sqlAccion    = null;
    int count         = 0;
    int countPrincipal= 0;
    boolean validate  = false;
    boolean regresar  = false;
    try {
			if(this.registroCliente.getPersonasTiposContacto().size()== 1)
					this.registroCliente.getPersonasTiposContacto().get(0).setIdPrincipal(1L);
      for (ClienteContactoRepresentante clienteRepresentante : this.registroCliente.getPersonasTiposContacto()) {
				if(clienteRepresentante.getIdPrincipal().equals(1L))
					countPrincipal++;
				if(countPrincipal== 0 && this.registroCliente.getPersonasTiposContacto().size()-1 == count)
					clienteRepresentante.setIdPrincipal(1L);
        clienteRepresentante.setIdCliente(idCliente);
        clienteRepresentante.setIdUsuario(JsfBase.getIdUsuario());
        clienteRepresentante.setIdRepresentante(addRepresentante(sesion, clienteRepresentante));
        dto = (TrManticClienteRepresentanteDto) clienteRepresentante;
        sqlAccion = clienteRepresentante.getSqlAccion();
        switch (sqlAccion) {
          case INSERT:
            dto.setIdClienteRepresentante(-1L);
            validate = registrar(sesion, dto);
            break;
          case UPDATE:
            validate = actualizar(sesion, dto);
            break;
        } // switch
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.registroCliente.getPersonasTiposContacto().size();
    } // try // try    
    finally {
      this.messageError = "Error al registrar los representantes, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesRepresentantes
	
	private Long addRepresentante(Session sesion, ClienteContactoRepresentante clienteRepresentante) throws Exception{
		Long regresar= -1L;
		TcManticPersonasDto representante= null;
		try {
			representante= new TcManticPersonasDto();
			representante.setNombres(clienteRepresentante.getNombres());
			representante.setPaterno(clienteRepresentante.getPaterno());
			representante.setMaterno(clienteRepresentante.getMaterno());
			representante.setIdTipoPersona(ETipoPersona.REPRESENTANTE_LEGAL.getIdTipoPersona());	
			representante.setIdTipoSexo(1L);
			representante.setEstilo(ESTILO);
			representante.setIdPersonaTitulo(1L);		
			regresar= DaoFactory.getInstance().insert(sesion, representante);
			if(regresar > -1L)
				registraPersonasTipoContacto(sesion, regresar, clienteRepresentante.getContactos());
		} // try		
		finally{
			this.messageError = "Error al registrar los representantes, verifique que no haya duplicados";
		} // finally
		return regresar;
	} // addRepresentante

	private boolean registraPersonasTipoContacto(Session sesion, Long idPersona, List<PersonaTipoContacto> tiposContactos) throws Exception {
    TrManticPersonaTipoContactoDto dto = null;
    ESql sqlAccion  = null;
    int count       = 0;
    boolean validate= false;
    boolean regresar= false;
    try {
      for (PersonaTipoContacto personaTipoContacto : tiposContactos) {
				if(personaTipoContacto.getValor()!= null && !Cadena.isVacio(personaTipoContacto.getValor())){
					personaTipoContacto.setIdPersona(idPersona);
					personaTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
					personaTipoContacto.setOrden(count+1L);
					dto = (TrManticPersonaTipoContactoDto) personaTipoContacto;
					sqlAccion = personaTipoContacto.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdPersonaTipoContacto(-1L);
							validate = registrar(sesion, dto);
							break;
						case UPDATE:
							validate = actualizar(sesion, dto);
							break;
					} // switch
				} // if
				else
					validate= true;
        if (validate) 
          count++;        
      } // for		
      regresar = count == tiposContactos.size();
    } // try    
    finally {
      this.messageError = "Error al registrar los tipos de contacto, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesTipoContacto
	
  private boolean registraClientesTipoContacto(Session sesion, Long idCliente) throws Exception {
    TrManticClienteTipoContactoDto dto = null;
    ESql sqlAccion       = null;
    int count            = 0;
    int orden            = 1;
    boolean validateOrden= true;
    boolean validate     = false;
    boolean regresar     = false;
    try {
      for (ClienteTipoContacto clienteTipoContacto : this.registroCliente.getClientesTiposContacto()) {
				if(clienteTipoContacto.getValor()!= null && !Cadena.isVacio(clienteTipoContacto.getValor())){
					if(clienteTipoContacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()) && validateOrden){
						clienteTipoContacto.setOrden(1L);
						validateOrden= false;
					} // if
					else
						clienteTipoContacto.setOrden(orden + 1L);
					clienteTipoContacto.setIdCliente(idCliente);
					clienteTipoContacto.setIdUsuario(JsfBase.getIdUsuario());
					dto = (TrManticClienteTipoContactoDto) clienteTipoContacto;
					sqlAccion = clienteTipoContacto.getSqlAccion();
					switch (sqlAccion) {
						case INSERT:
							dto.setIdClienteTipoContacto(-1L);
							validate = registrar(sesion, dto);
							break;
						case UPDATE:
							validate = actualizar(sesion, dto);
							break;
					} // switch
					orden++;
				} // if
				else
					validate= true;
        if (validate) {
          count++;
        }
      } // for		
      regresar = count == this.registroCliente.getClientesTiposContacto().size();
    } // try    
    finally {
      this.messageError = "Error al registrar los tipos de contacto, verifique que no haya duplicados";
    } // finally
    return regresar;
  } // registraClientesTipoContacto

  private boolean eliminarRegistros(Session sesion) throws Exception {
    boolean regresar = true;
    int count = 0;
    try {
      for (IBaseDto dto : this.registroCliente.getDeleteList()) {
        if (DaoFactory.getInstance().delete(sesion, dto) >= 1L) 
          count++;        
      } // for
      regresar= (count == this.registroCliente.getDeleteList().size());
    } // try    
    finally {
      this.messageError = "Error al eliminar registros";
    } // finally
    return regresar;
  } // eliminarRegistros

  private boolean registrar(Session sesion, IBaseDto dto) throws Exception {
    return registrarSentencia(sesion, dto) >= 1L;
  } // registrar
  
	private Long registrarSentencia(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().insert(sesion, dto);
  } // registrar

  private boolean actualizar(Session sesion, IBaseDto dto) throws Exception {
    return DaoFactory.getInstance().update(sesion, dto) >= 1L;
  } // actualizar
	
	private Long toIdDomicilio(Session sesion, ClienteDomicilio clienteDomicilio) throws Exception{		
		Entity entityDomicilio= null;
		Long regresar         = -1L;		
		entityDomicilio= toDomicilio(sesion, clienteDomicilio);
		if(entityDomicilio!= null)
			regresar= entityDomicilio.getKey();
		else
			regresar= insertDomicilio(sesion, clienteDomicilio);									
		return regresar;
	} // registrarDomicilio	
	
	private Long insertDomicilio(Session sesion, ClienteDomicilio clienteDomicilio) throws Exception{
		TcManticDomiciliosDto domicilio= null;
		Long regresar= -1L;		
		domicilio= new TcManticDomiciliosDto();
		domicilio.setIdLocalidad(clienteDomicilio.getIdLocalidad().getKey());
		domicilio.setAsentamiento(clienteDomicilio.getColonia());
		domicilio.setCalle(clienteDomicilio.getCalle());
		domicilio.setCodigoPostal(clienteDomicilio.getCodigoPostal());
		domicilio.setEntreCalle(clienteDomicilio.getEntreCalle());
		domicilio.setIdUsuario(JsfBase.getIdUsuario());
		domicilio.setNumeroExterior(clienteDomicilio.getExterior());
		domicilio.setNumeroInterior(clienteDomicilio.getInterior());
		domicilio.setYcalle(clienteDomicilio.getyCalle());
		regresar= DaoFactory.getInstance().insert(sesion, domicilio);		
		return regresar;
	} // insertDomicilio
	
	private Entity toDomicilio(Session sesion, ClienteDomicilio clienteDomicilio) throws Exception{
		Entity regresar          = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idLocalidad", clienteDomicilio.getIdLocalidad().getKey());
			params.put("codigoPostal", clienteDomicilio.getCodigoPostal());
			params.put("calle", clienteDomicilio.getCalle());
			params.put("numeroExterior", clienteDomicilio.getExterior());
			params.put("numeroInterior", clienteDomicilio.getInterior());
			params.put("asentamiento", clienteDomicilio.getColonia());
			params.put("entreCalle", clienteDomicilio.getEntreCalle());
			params.put("yCalle", clienteDomicilio.getyCalle());
			regresar= (Entity) DaoFactory.getInstance().toEntity(sesion, "TcManticDomiciliosDto", "domicilioExiste", params);
		} // try		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toDomicilio
	
	protected void toUpdateDeleteFile(Session sesion) throws Exception {
		TcManticClientesArchivosDto tmp= null;
		if(this.cliente.getIdCliente()!= -1L) {			
			if(this.file!= null) {
				tmp= new TcManticClientesArchivosDto(
					this.representante,
					this.file.getRuta(),
					this.file.getFileSize(),
					JsfBase.getIdUsuario(),
					-1L,
					2L,
					1L,
					this.file.getObservaciones(),
					Configuracion.getInstance().getPropiedadSistemaServidor("clientes").concat(this.file.getRuta()).concat(this.file.getName()),
					this.file.getName(),
					this.file.getOriginal()
				);
				TcManticClientesArchivosDto exists= (TcManticClientesArchivosDto)DaoFactory.getInstance().toEntity(TcManticClientesArchivosDto.class, "TcManticClientesArchivosDto", "identically", tmp.toMap());
				File reference= new File(tmp.getAlias());
				if(exists== null && reference.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticClientesArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!reference.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("clientes").concat(this.file.getRuta()), ".".concat(this.file.getFormat().name()), this.toListFile(sesion, this.file, 2L));
			} // if	
  	} // if	
	} // toUpdateDeleteXml
	
	protected void toUpdateDeleteFilePago(Session sesion) throws Exception {
		TcManticClientesPagosArchivosDto tmp= null;
		if(this.clienteDeuda.getIdClienteDeuda()!= -1L) {			
			if(this.file!= null) {
				tmp= new TcManticClientesPagosArchivosDto(
					this.file.getRuta(),
					this.file.getFileSize(),
					JsfBase.getIdUsuario(),
					2L,
					1L,
					this.file.getObservaciones(),
					this.idClientePago,	
					Configuracion.getInstance().getPropiedadSistemaServidor("cobros").concat(this.file.getRuta()).concat(this.file.getName()),
					-1L,																				
					this.file.getName()					
				);
				TcManticClientesPagosArchivosDto exists= (TcManticClientesPagosArchivosDto)DaoFactory.getInstance().toEntity(TcManticClientesPagosArchivosDto.class, "TcManticClientesPagosArchivosDto", "identically", tmp.toMap());
				File reference= new File(tmp.getAlias());
				if(exists== null && reference.exists()) {
					DaoFactory.getInstance().updateAll(sesion, TcManticClientesPagosArchivosDto.class, tmp.toMap());
					DaoFactory.getInstance().insert(sesion, tmp);
				} // if
				else
				  if(!reference.exists())
						LOG.warn("INVESTIGAR PORQUE NO EXISTE EL ARCHIVO EN EL SERVIDOR: "+ tmp.getAlias());
				sesion.flush();
				this.toDeleteAll(Configuracion.getInstance().getPropiedadSistemaServidor("cobros").concat(this.file.getRuta()), ".".concat(this.file.getFormat().name()), this.toListFile(sesion, this.file, 2L));
			} // if	
  	} // if	
	} // toUpdateDeleteXml
	
	private List<Nombres> toListFile(Session sesion, Importado tmp, Long idTipoArchivo) throws Exception {
		List<Nombres> regresar    = null;
		Map<String, Object> params= null;
		try {
			params  = new HashMap<>();
			params.put("idTipoArchivo", idTipoArchivo);
			params.put("ruta", tmp.getRuta());
			regresar= (List<Nombres>)DaoFactory.getInstance().toEntitySet(sesion, Nombres.class, "TcManticClientesArchivosDto", "listado", params);
			regresar.add(new Nombres(tmp.getName()));
		} // try  // try 		
		finally {
			Methods.clean(params);
		} // finally
		return regresar;
	} // toListFile
	
	private void toDeleteAll(String path, String type, List<Nombres> listado) {
    FileSearch fileSearch = new FileSearch();
    fileSearch.searchDirectory(new File(path), type.toLowerCase());
    if(fileSearch.getResult().size()> 0){
		  for (String matched: fileSearch.getResult()) {
				String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
				if(listado.indexOf(new Nombres(name))< 0) {
				  File file= new File(matched);
				  file.delete();
				} // if
      } // for
		} // if
	} // toDeleteAll
}