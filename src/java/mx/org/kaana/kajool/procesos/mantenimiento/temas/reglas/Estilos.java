package mx.org.kaana.kajool.procesos.mantenimiento.temas.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mx.org.kaana.kajool.procesos.mantenimiento.temas.beans.Tema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 10/09/2015
 * @time 11:05:25 AM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Estilos implements Serializable {

	private static final long serialVersionUID=20468773592909229L;
  private static final Log LOG= LogFactory.getLog(Estilos.class);
  public static final List<Tema> themes;

  private static Estilos instance;
  private static Object mutex;

  static {
    mutex = new Object();
    themes= new ArrayList<>();
		themes.add(new Tema("Sentinel", "sentinel", "sentinel.png"));
		themes.add(new Tema("Spark", "spark", "spark.png"));
		themes.add(new Tema("Modena", "modena", "modena.png"));
		themes.add(new Tema("Reddy", "reddy", "reddy.png"));
		themes.add(new Tema("Orange", "orange", "orange.png"));
		themes.add(new Tema("Brightside", "brightside", "brightside.png"));
		themes.add(new Tema("Cybera", "cybera", "cybera.png"));
		themes.add(new Tema("Ronin", "ronin", "ronin.png"));
		themes.add(new Tema("Pink", "gipink", "gipink.png"));
		themes.add(new Tema("Home", "home", "home.png"));
		themes.add(new Tema("After dark", "afterdark", "afterdark.png"));
		themes.add(new Tema("After Noon", "afternoon", "afternoon.png"));
		themes.add(new Tema("After Work", "afterwork", "afterwork.png"));
		themes.add(new Tema("Bootstrap", "bootstrap", "bootstrap.png"));
		themes.add(new Tema("Pink panther", "pink-panther", "pink-panther.png"));
		themes.add(new Tema("Snieg", "snieg", "snieg.png"));
		themes.add(new Tema("Cruze", "cruze", "cruze.png"));
		themes.add(new Tema("Aristo", "aristo", "aristo.png"));
		themes.add(new Tema("Black tie", "black-tie", "black-tie.png"));
		themes.add(new Tema("Blitzer", "blitzer", "blitzer.png"));
		themes.add(new Tema("Bluesky", "bluesky", "bluesky.png"));
		themes.add(new Tema("Casa blanca", "casablanca", "casablanca.png"));
		themes.add(new Tema("Cupertino", "cupertino", "cupertino.png"));
		themes.add(new Tema("Dark hive", "dark-hive", "dark-hive.png"));
		themes.add(new Tema("Dot luv", "dot-luv", "dot-luv.png"));
		themes.add(new Tema("Eggplant", "eggplant", "eggplant.png"));
		themes.add(new Tema("Excite bike", "excite-bike", "excite-bike.png"));
		themes.add(new Tema("Flick", "flick", "flick.png"));
		themes.add(new Tema("Glass-X", "glass-x", "glass-x.png"));
		themes.add(new Tema("Hot sneaks", "hot-sneaks", "hot-sneaks.png"));
		themes.add(new Tema("Humanity", "humanity", "humanity.png"));
		themes.add(new Tema("Le frog", "le-frog", "le-frog.png"));
		themes.add(new Tema("Midnight", "midnight", "midnight.png"));
		themes.add(new Tema("Mint choc", "mint-choc", "mint-choc.png"));
		themes.add(new Tema("Overcast", "overcast", "overcast.png"));
		themes.add(new Tema("Pepper grinder", "pepper-grinder", "pepper-grinder.png"));
		themes.add(new Tema("Redmond", "redmond", "redmond.png"));
		themes.add(new Tema("Rocket", "rocket", "rocket.png"));
		themes.add(new Tema("Sam", "sam", "sam.png"));
		themes.add(new Tema("Smoothness", "smoothness", "smoothness.png"));
		themes.add(new Tema("South street", "south-street", "south-street.png"));
		themes.add(new Tema("Start", "start", "start.png"));
		themes.add(new Tema("Sunny", "sunny", "sunny.png"));
		themes.add(new Tema("Swanky purse", "swanky-purse", "swanky-purse.png"));
		themes.add(new Tema("Trontastic", "trontastic", "trontastic.png"));
		themes.add(new Tema("UI-Darkness", "ui-darkness", "ui-darkness.png"));
		themes.add(new Tema("UI-Lightness", "ui-lightness", "ui-lightness.png"));
		themes.add(new Tema("Vader", "vader", "vader.png"));
		themes.add(new Tema("Metro", "metro", "metro.png"));
  }

  private Estilos() {
  }

  public static Estilos getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance= new Estilos();
        LOG.info("Galeria de estilos cargado e inicializado !");
      }
    } // if
    return instance;
  }
	
	public List<Tema> getThemes() {
		return Collections.unmodifiableList(themes);
	}
	
}
