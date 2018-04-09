package mx.org.kaana.kajool.procesos.mantenimiento.gruposperfiles.perfiles.reglas;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date Aug 31, 2015
 *@time 6:05:49 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

import java.sql.Timestamp;
import java.util.Calendar;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.kajool.db.dto.TcJanalPerfilesDto;
import mx.org.kaana.kajool.enums.EAccion;

public class Agregar {
  private TcJanalPerfilesDto tcJanalPerfilesDto;

  public Agregar(TcJanalPerfilesDto tcJanalPerfilesDto) {
    this.tcJanalPerfilesDto= tcJanalPerfilesDto;
  }

  public TcJanalPerfilesDto getTcJanalPerfilesDto() {
    return tcJanalPerfilesDto;
  }

  public void setTcJanalPerfilesDto(TcJanalPerfilesDto tcJanalPerfilesDto) {
    this.tcJanalPerfilesDto = tcJanalPerfilesDto;
  }

  public void agregar(EAccion accion) throws Exception{
    try {
      this.tcJanalPerfilesDto.setIdUsuario(JsfBase.getAutentifica().getEmpleado().getIdUsuario());
      this.tcJanalPerfilesDto.setRegistro(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			this.tcJanalPerfilesDto.setIdMenu(24L);
      if(accion.equals(EAccion.AGREGAR))
        this.tcJanalPerfilesDto.setAcceso(1L);
      Transaccion transaccion = new Transaccion(this.tcJanalPerfilesDto);
      if (transaccion.ejecutar(accion))
        JsfBase.addMessage("La acción ".concat(accion.getName()).concat(" perfil se realizo con exito"));
    } catch (Exception e) {
      throw e;
    }// catch
  } // doAgregar
}
