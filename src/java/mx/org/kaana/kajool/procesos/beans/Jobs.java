package mx.org.kaana.kajool.procesos.beans;

import java.util.Date;
import java.io.Serializable;
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

	public Jobs() {
		this("", "", new Date(), new Date());
	}

	public Jobs(String jobName, String jobGroup, Date nextFireTime, Date previousFireTime) {
		this.jobName         = jobName;
		this.jobGroup        = jobGroup;
		this.nextFireTime    = nextFireTime;
		this.previousFireTime= previousFireTime;
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

  @Override
  public String toString() {
    return "Jobs{" + "jobName=" + jobName + ", jobGroup=" + jobGroup + ", nextFireTime=" + nextFireTime + ", previousFireTime=" + previousFireTime + '}';
  }


}
