/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.kajool.enums;

/**
 *
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public enum EPagos {

	CUENTA_1("ENVASES UNIVERSALES SAPI SA DE CV", 12000L, 5000L, "ELEKTRON FERRETERIA S.A. DE C.V.", 30000L, 15000L),
	CUENTA_2("APLICA COMPUTADORAS SA DE CV", 7500L, 500L, "PROMAJA COMERCIAL S.A. DE C.V.", 7000L, 5000L),
	CUENTA_3("CARLOS JOSE SALAS LUJAN", 10500L, 8000L, "SINERGIA EN SOLUCIONES COMERCIALES", 9000L, 2000L),
	CUENTA_4("3M DISEÑO Y CONSTRUCCION SA DE CV", 12500L, 1500L, "EUROELECTRICA S.A. DE C.V.", 45000L, 40000L),
	CUENTA_5("JUAN SILVESTRE ARAGON DE ANDA", 9500L, 9000L, "TRAKA DE MEXICO S.A. DE C.V.", 15000L, 1000L);
	
  private String cliente;
  private String proveedor;
  private Long importe;
  private Long saldo;	
  private Long deuda;
  private Long abono;	

  private EPagos(String cliente, Long importe, Long saldo, String proveedor, Long deuda, Long abono) {
    this.cliente = cliente;
    this.importe = importe;
    this.saldo = saldo;
    this.proveedor= proveedor;
    this.deuda= deuda;
    this.abono= abono;
  }	

	public Long getIdKey(){
		return this.ordinal() + 1L;
	}

  public String getCliente() {
    return cliente;
  }

  public Long getImporte() {
    return importe;
  }

  public Long getSaldo() {
    return saldo;
  }	

  public String getProveedor() {
    return proveedor;
  }

  public Long getDeuda() {
    return deuda;
  }

  public Long getAbono() {
    return abono;
  }
  
}
