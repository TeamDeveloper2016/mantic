package mx.org.kaana.kajool.procesos.beans;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.db.dto.TrJanalCaratulaDto;
import mx.org.kaana.kajool.db.dto.TrJanalFamiliasDto;
import mx.org.kaana.kajool.db.dto.TrJanalModuloDto;
import mx.org.kaana.kajool.db.dto.TrJanalPersonasDto;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 13/10/2016
 *@time 12:15:35 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Cuestionario implements Serializable {
	
  @SerializedName("trJanalCaratulaDto")
	private TrJanalCaratulaDto trJanalCaratulaDto;
  @SerializedName("personas")
	private List<TrJanalPersonasDto> personas;
  @SerializedName("trJanalFamiliasDto")
	private TrJanalFamiliasDto trJanalFamiliasDto;
  @SerializedName("trJanalModuloDto")
	private TrJanalModuloDto trJanalModuloDto; 
	
	public Cuestionario() {
		this.trJanalCaratulaDto= new TrJanalCaratulaDto();		
		this.personas          = new ArrayList<TrJanalPersonasDto>();
		this.trJanalFamiliasDto= new TrJanalFamiliasDto();
		this.trJanalModuloDto  = new TrJanalModuloDto();
	}

	public TrJanalCaratulaDto getTrJanalCaratulaDto() {
		return trJanalCaratulaDto;
	}

	public void setTrJanalCaratulaDto(TrJanalCaratulaDto trJanalCaratulaDto) {
		this.trJanalCaratulaDto=trJanalCaratulaDto;
	}

	public List<TrJanalPersonasDto> getPersonas() {
		return personas;
	}

	public void setPersonas(List<TrJanalPersonasDto> personas) {
		this.personas=personas;
	}

	public TrJanalFamiliasDto getTrJanalFamiliasDto() {
		return trJanalFamiliasDto;
	}

	public void setTrJanalFamiliasDto(TrJanalFamiliasDto trJanalFamiliasDto) {
		this.trJanalFamiliasDto=trJanalFamiliasDto;
	}

	public TrJanalModuloDto getTrJanalModuloDto() {
		return trJanalModuloDto;
	}

	public void setTrJanalModuloDto(TrJanalModuloDto trJanalModuloDto) {
		this.trJanalModuloDto=trJanalModuloDto;
	}	

  @Override
  public String toString() {
    return "Cuestionario{" + "trJanalCaratulaDto=" + trJanalCaratulaDto + ", personas=" + personas + ", trJanalFamiliasDto=" + trJanalFamiliasDto + ", trJanalModuloDto=" + trJanalModuloDto + '}';
  }
	
}
