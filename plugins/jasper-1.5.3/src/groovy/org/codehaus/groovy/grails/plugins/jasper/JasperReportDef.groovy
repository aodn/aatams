/* Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.codehaus.groovy.grails.plugins.jasper

import org.apache.commons.io.FilenameUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.core.io.Resource
import org.springframework.core.io.FileSystemResource
import net.sf.jasperreports.engine.JasperCompileManager

import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.JasperPrint

/**
 * An abstract representation of a Jasper report.
 * <p>
 * It contains the location of the report design file (name and folder) and
 * data(reportData and/or parameters) needed to fill the report.
 * @author Sebastian Hohns 2010
 */
public class JasperReportDef {

  /**
   * The name of the report file without extension.
   * <p>
   * The file can be in jrxml- or jasper-format.
   */
  String name

  /**
   * The parent folder of the report file.
   * <p>
   * This can be an absolute path or an relative path based on class path.
   */
  String folder

  /**
   * The data source used to fill the report.
   * <p>
   * This is a list of java beans.
   */
  Collection reportData

  /**
   * The target file format.
   */
  public JasperExportFormat fileFormat = JasperExportFormat.PDF_FORMAT;

  /**
   * The generated report as OutputStream.
   */
  ByteArrayOutputStream contentStream

  /**
   * Additional parameters.
   */
  Map parameters = new HashMap()

  /**
   * Locale setting.
   */
  Locale locale

  JasperPrint jasperPrinter

  private getApplicationContext() {
    return ApplicationHolder.application.mainContext
  }

  /**
   * Looks for the report file in the filesystem. The file extension can either be .jasper
   * or .jrxml. if japser.compile.files is set to true the report will be compiled and stored
   * in the same folder as the jrxml file.
   * @return the report as Resource
   * @throws Exception , report file not found
   */
  public Resource getReport() throws Exception {
    String path = getFilePath()

    Resource result = getApplicationContext().getResource(path + ".jasper")
    if (result.exists()) {
      return result
    }

    result = new FileSystemResource(path + ".jasper")
    if (result.exists()) {
      return result
    }

    result = getApplicationContext().getResource(path + ".jrxml")
    if (result.exists()) {
      return result
    }

    result = new FileSystemResource(path + ".jrxml")
    if (result.exists()) {
      return result
    }

    throw new Exception("No such report spec: ${path} (jasper or .jrxml)")
  }

  /**
   * Return the file path. The filepath can be set per file (highest priority) or based on
   * the jasper.dir.reports setting. Defaults to /report.
   * @return full path to the report, without extension
   */
  public String getFilePath() {
    if (folder) {
      return folder + File.separator + FilenameUtils.getPath(name) + FilenameUtils.getBaseName(name)
    } else if (ConfigurationHolder.config.jasper.dir.reports) {
      return ConfigurationHolder.config.jasper.dir.reports + File.separator + FilenameUtils.getPath(name) + FilenameUtils.getBaseName(name)
    } else {
      return "/reports" + File.separator + FilenameUtils.getPath(name) + FilenameUtils.getBaseName(name)
    }
  }

  public void setFilePath(String path) {
    folder = FilenameUtils.getPath(path)
    name = FilenameUtils.getName(path)
  }

  /**
   * Add a parameter to the parameter map.
   * @param key , the key
   * @param value , the value
   */
  public void addParameter(key, value) {
    parameters.put(key, value)
  }
}