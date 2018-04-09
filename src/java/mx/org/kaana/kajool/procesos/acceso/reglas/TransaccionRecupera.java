package mx.org.kaana.kajool.procesos.acceso.reglas;

import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.dto.TcJanalUsuariosDto;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 4/09/2015
 *@time 04:23:02 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class TransaccionRecupera extends IBaseTnx {
    private Long idUsuario;
    private Map param;

    public TransaccionRecupera(Long idUsuario,Map param) {
        this.idUsuario=idUsuario;
        this.param=param;
    }

    @Override
    protected boolean ejecutar(Session sesion ,EAccion accion) throws Exception {
         boolean regresar=true;
         switch(accion){
             case MODIFICAR:
                 regresar=DaoFactory.getInstance().update(sesion,TcJanalUsuariosDto.class,this.idUsuario,this.param)>0L;break;
         }
         return regresar;
    }


}
