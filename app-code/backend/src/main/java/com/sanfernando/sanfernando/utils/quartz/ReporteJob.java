package com.sanfernando.sanfernando.utils.quartz;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.sanfernando.sanfernando.dao.daoImpl.ReporteDaoImpl;

public class ReporteJob implements Job{

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    Object fechaFinStr = dataMap.getString("fechaFin");
    Object horaFinStr = dataMap.getString("horaFin");
    Object idProgramacionStr = dataMap.getInt("idProgramacion");

    int idProgramacion = Integer.parseInt(idProgramacionStr.toString());

    DateTimeFormatter dateFormatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);

    LocalDate fechaFin = LocalDate.parse(fechaFinStr.toString(), dateFormatter);
    LocalTime horaFin  = LocalTime.parse(horaFinStr.toString(), timeFormatter);

    LocalDateTime fechaHoraFin = LocalDateTime.of(fechaFin, horaFin);

    System.out.println("\n\n");
    System.out.println(fechaHoraFin); 

    LocalDateTime fechaHoraActual = LocalDateTime.now();

    System.out.println("Fecha de finalización: " + fechaHoraFin);
    System.out.println("Fecha actual: " + fechaHoraActual);
    if (fechaHoraFin.isBefore(fechaHoraActual)) {
      System.out.println("La fecha de finalización es anterior a la fecha actual.");
      System.out.println("Terminando programacion..." + idProgramacion);

      /* Lógica para cambiar el estado de programacion reporte */
      ReporteDaoImpl reporteDaoImpl = new ReporteDaoImpl();
      reporteDaoImpl.stopProgramacionReporte(idProgramacion);

      try {
        context.getScheduler().shutdown();
      } catch (SchedulerException e) {
        e.printStackTrace();
      }
    } else if (fechaHoraFin.isAfter(fechaHoraActual)) {
      System.out.println("La fecha de finalización es posterior a la fecha actual.");
      System.out.println("Generando reporte..... \n");

      /* Lógica para generar el reporte */

    } else {
      System.out.println("La fecha de finalización es igual a la fecha actual.");
    }

  }
}
