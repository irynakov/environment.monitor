package org.yagel.monitor.api.rest;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yagel.monitor.mongo.AggregatedStatusDAO;
import org.yagel.monitor.resource.AggregatedResourceStatus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report/pdf/")
public class PdfReportService {

  @Autowired
  private AggregatedStatusDAO aggregatedStatusDAO;

  @RequestMapping(value = "aggregated/{environmentName}", method = RequestMethod.GET)
  public ResponseEntity<InputStreamResource> getPdfReport(
      @PathVariable("environmentName") String environmentName,
      @RequestParam(value = "resources", required = false) Set<String> resources,
      @RequestParam("startDate") String startDate,
      @RequestParam("endDate") String endDate) throws JRException, IOException {


//    List<AggregatedResourceStatus> aggStatusses = aggregatedStatusDAO.getAggregatedStatuses(environmentName, resources, startDate, endDate)
//        .stream()
//        .sorted(Comparator.comparing(o -> o.getResource().getName()))
//        .collect(Collectors.toList());

    String name = "report.pdf";

    Map<String,Object> params = new HashMap<>();
    params.put("ENV_NAME", environmentName);
    JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/report/pdf/aggregatedReport.jrxml"));
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

    JRPdfExporter exporter = new JRPdfExporter();
    SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(name);
    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    exporter.setExporterOutput(c);
    exporter.exportReport();

    FileInputStream st = new FileInputStream(name);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
    responseHeaders.setContentDispositionFormData("attachment", name);
    responseHeaders.setContentLength(st.available());
    return new ResponseEntity<InputStreamResource>(new InputStreamResource(st), responseHeaders, HttpStatus.OK);

    //return ResponseEntity.ok(aggStatusses);
  }
}
