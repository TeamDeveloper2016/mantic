package mx.org.kaana.kajool.procesos.beans;

import java.util.Date;
import java.io.Serializable;
import mx.org.kaana.kajool.procesos.enums.ESemaforos;
import org.quartz.JobKey;
/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 30/05/2014
 *@time 04:10:45 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class Jobs implements Serializable {
  private static final long serialVersionUID = 8393637354828656663L;
	private String jobName;
	private String jobGroup;
	private Date nextFireTime;
	private Date previousFireTime;
	private JobKey jobKey;
	private String semaforo;
	private String expresion;
	private String summary;
	private String server;

	public Jobs() {
		this("", "", new Date(), new Date());
	}

	public Jobs(String jobName, String jobGroup, Date nextFireTime, Date previousFireTime) {
		this(jobName, jobGroup, nextFireTime, previousFireTime, "");
	}
	
	public Jobs(String jobName, String jobGroup, Date nextFireTime, Date previousFireTime, String server) {
		this(jobName, jobGroup, nextFireTime, previousFireTime, null, ESemaforos.VERDE.getNombre(), null, null, server);
	}
	
	public Jobs(String jobName, String jobGroup, Date nextFireTime, Date previousFireTime, JobKey jobKey, String semaforo, String expresion, String summary, String server) {
		this.jobName         = jobName;
		this.jobGroup        = jobGroup;
		this.nextFireTime    = nextFireTime;
		this.previousFireTime= previousFireTime;
		this.jobKey          = jobKey;
		this.semaforo        = semaforo;
		this.expresion       = expresion;
		this.summary         = summary;
		this.server          = server;
	}	

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName= jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup= jobGroup;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime= nextFireTime;
	}

	public Date getPreviousFireTime() {
		return previousFireTime;
	}

	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime= previousFireTime;
	}	

	public JobKey getJobKey() {
		return jobKey;
	}

	public void setJobKey(JobKey jobKey) {
		this.jobKey= jobKey;
	}	

	public String getSemaforo() {
		return semaforo;
	}

	public void setSemaforo(String semaforo) {
		this.semaforo= semaforo;
	}		

	public String getExpresion() {
		return expresion;
	}

	public void setExpresion(String expresion) {
		this.expresion= expresion;
	}	

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary=summary;
	}	

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}	

  @Override
  public String toString() {
    return "Jobs{" + "jobName=" + jobName + ", jobGroup=" + jobGroup + ", nextFireTime=" + nextFireTime + ", previousFireTime=" + previousFireTime + '}';
  }
}
