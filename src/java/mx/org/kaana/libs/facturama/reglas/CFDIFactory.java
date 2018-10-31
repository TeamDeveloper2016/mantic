package mx.org.kaana.libs.facturama.reglas;

import java.io.Serializable;
import java.util.List;
import mx.org.kaana.libs.facturama.container.FacturamaApi;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.Product;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;

/**
 *@company MANTIC
 *@project KAANA (Sistema de seguimiento y control de proyectos)
 *@date 30/10/2018
 *@time 10:30:40 PM 
 *@author Team Developer 2018 <team.developer@gmail.com>
 */

public class CFDIFactory implements Serializable {

	private static final long serialVersionUID=-5361573067043698091L;
  private static final String USER       = "FERRBONANZA";
  private static final String PASSWORD   = "ZABONAN2018";
	private static final Boolean PRODUCTION= false;
	
	private FacturamaApi facturama;
	
  private static CFDIFactory instance;
  private static Object mutex;
  /**
   * Inicialización de variable mutex
   */
  static {
    mutex = new Object();
  }
	
  /**
   * Contructor default
   */
  private CFDIFactory() {
		this.facturama= new FacturamaApi(this.USER, this.PASSWORD, this.PRODUCTION);
  }

  /**
   * Devuelve la instancia de la clase.
   *
   * @return Instancia de la clase.
   */
  public static CFDIFactory getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new CFDIFactory();
      }
    } // if
    return instance;
  }

	public List<Client> getClients() throws FacturamaException, Exception {
		return this.facturama.Clients().List();
	}
	
	public List<Product> getProducts() throws FacturamaException, Exception {
		return this.facturama.Products().List();
	}

	public List<CfdiSearchResult> getCfdis() throws FacturamaException, Exception {
		return this.facturama.Cfdis().List();
	}

  public void download(String path, String id) throws Exception {
		this.facturama.Cfdis().SavePdf(path+ ".pdf", id);
		this.facturama.Cfdis().SaveXml(path+ ".xml", id);
	}	
	
}
