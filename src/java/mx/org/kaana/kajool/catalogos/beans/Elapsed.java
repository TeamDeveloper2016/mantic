package mx.org.kaana.kajool.catalogos.beans;

import java.util.Calendar;
import mx.org.kaana.libs.formato.Fecha;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 16/12/2018
 *@time 10:05:49 AM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Elapsed {
	
	private static final Log LOG=LogFactory.getLog(Elapsed.class);

	private long elapsed;
	private long missing;

	public Elapsed() {
		this(0, 0);
	}

	public Elapsed(long elapsed, long missing) {
		this.elapsed=elapsed;
		this.missing=missing;
	}

	public long getElapsed() {
		return elapsed;
	}

	public long getMissing() {
		return missing;
	}

	@Override
	public int hashCode() {
		int hash=3;
		hash=97*hash+(int) (this.elapsed^(this.elapsed>>>32));
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null) {
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		final Elapsed other=(Elapsed) obj;
		if (this.elapsed!=other.elapsed) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Elapsed{"+"elapsed="+elapsed+", missing="+missing+'}';
	}

	public void calcualte(Calendar start, Calendar finished, Long progreso, Long total) {
		this.elapsed= finished.getTimeInMillis() - start.getTimeInMillis();
		long unit   = (long)(this.elapsed/ progreso);
		this.missing= (total- progreso)* unit;
	}
	
	public String formatElapsed() {
	  return Fecha.toFormatSecondsToHour((long)(this.elapsed/ 1000));
	}
	
	public String formatMissing() {
	  return Fecha.toFormatSecondsToHour((long)(this.missing/ 1000));
	}
	
	public static void main(String ... agrs) {
		long seconds= (long)(Calendar.getInstance().getTimeInMillis()/1000L);
	  LOG.info("Seconds: "+ seconds);
		LOG.info("Format: "+ Fecha.toFormatSecondsToHour(seconds));
	}
	
}

