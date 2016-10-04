/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.AbstractSampleApp;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOdsReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BatchExportApp.java 7199 2014-08-27 13:58:10Z teodord $
 */
public class BatchExportApp extends AbstractSampleApp
{


	/**
	 *
	 */
	public static void main(String[] args)
	{
		main(new BatchExportApp(), args);
	}
	
	
	/**
	 *
	 */
	public void test() throws JRException
	{
		fill();
		pdf();
		html();
		rtf();
		xls();
		jxl();
		csv();
		odt();
		ods();
		docx();
		xlsx();
		pptx();
		xhtml();
	}


	/**
	 *
	 */
	public void fill() throws JRException
	{
		long start = System.currentTimeMillis();
		JasperFillManager.fillReportToFile(
			"build/reports/Report1.jasper",
			null, 
			new JREmptyDataSource(2)
			);
		JasperFillManager.fillReportToFile(
			"build/reports/Report2.jasper",
			null, 
			new JREmptyDataSource(2)
			);
		JasperFillManager.fillReportToFile(
			"build/reports/Report3.jasper",
			null, 
			new JREmptyDataSource(2)
			);
		System.err.println("Filling time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void pdf() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JRPdfExporter exporter = new JRPdfExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.pdf"));
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		configuration.setCreatingBatchModeBookmarks(true);
		exporter.setConfiguration(configuration);
		
		exporter.exportReport();
		
		System.err.println("PDF creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void html() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		HtmlExporter exporter = new HtmlExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleHtmlExporterOutput("build/reports/BatchExportReport.html"));
		
		exporter.exportReport();
		
		System.err.println("HTML creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void rtf() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JRRtfExporter exporter = new JRRtfExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleWriterExporterOutput("build/reports/BatchExportReport.rtf"));
		
		exporter.exportReport();

		System.err.println("RTF creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xls() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JRXlsExporter exporter = new JRXlsExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.xls"));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(false);
		exporter.setConfiguration(configuration);
		
		exporter.exportReport();

		System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public void jxl() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		net.sf.jasperreports.engine.export.JExcelApiExporter exporter = 
			new net.sf.jasperreports.engine.export.JExcelApiExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.jxl.xls"));
		net.sf.jasperreports.export.SimpleJxlReportConfiguration configuration = 
			new net.sf.jasperreports.export.SimpleJxlReportConfiguration();
		configuration.setOnePagePerSheet(false);
		exporter.setConfiguration(configuration);
		
		exporter.exportReport();

		System.err.println("XLS creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void csv() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JRCsvExporter exporter = new JRCsvExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleWriterExporterOutput("build/reports/BatchExportReport.csv"));
		
		exporter.exportReport();

		System.err.println("CSV creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void odt() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JROdtExporter exporter = new JROdtExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.odt"));
		
		exporter.exportReport();

		System.err.println("ODT creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void ods() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JROdsExporter exporter = new JROdsExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.ods"));
		SimpleOdsReportConfiguration configuration = new SimpleOdsReportConfiguration();
		configuration.setOnePagePerSheet(false);
		exporter.setConfiguration(configuration);
		
		exporter.exportReport();

		System.err.println("ODS creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void docx() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JRDocxExporter exporter = new JRDocxExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.docx"));
		
		exporter.exportReport();

		System.err.println("DOCX creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void xlsx() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.xlsx"));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setOnePagePerSheet(false);
		exporter.setConfiguration(configuration);
		
		exporter.exportReport();

		System.err.println("XLSX creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	public void pptx() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		JRPptxExporter exporter = new JRPptxExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("build/reports/BatchExportReport.pptx"));
		
		exporter.exportReport();

		System.err.println("PPTX creation time : " + (System.currentTimeMillis() - start));
	}
	
	
	/**
	 *
	 */
	@SuppressWarnings("deprecation")
	public void xhtml() throws JRException
	{
		long start = System.currentTimeMillis();
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report1.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report2.jrprint"));
		jasperPrintList.add((JasperPrint)JRLoader.loadObjectFromFile("build/reports/Report3.jrprint"));
		
		net.sf.jasperreports.engine.export.JRXhtmlExporter exporter = 
			new net.sf.jasperreports.engine.export.JRXhtmlExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleHtmlExporterOutput("build/reports/BatchExportReport.xhtml"));
		
		exporter.exportReport();

		System.err.println("XHTML creation time : " + (System.currentTimeMillis() - start));
	}


}
