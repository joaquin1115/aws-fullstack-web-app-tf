package com.sanfernando.sanfernando.utils.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteScheduler {
  private String nombreTrigger;
  private String nombreJob;
  private String nombreGroup;
  @Getter(AccessLevel.NONE)
  private Scheduler scheduler;

  public void startScheduler(Class<? extends org.quartz.Job> jobClass, String fechaFin, String horaFin, int idProgramacionReporte) throws SchedulerException {
    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
    JobDetail job = JobBuilder.newJob(jobClass)
      .withIdentity(this.nombreJob, this.nombreGroup)
      .usingJobData("fechaFin", fechaFin)
      .usingJobData("horaFin", horaFin)
      .usingJobData("idProgramacion", idProgramacionReporte)
      .build();
    Trigger trigger = TriggerBuilder.newTrigger()
      .withIdentity(this.nombreTrigger, this.nombreGroup)
      .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
      .build();
    scheduler.start();
    scheduler.scheduleJob(job, trigger);
  }

}
