/*
 * Copyright 2011 the original author or authors.
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
 */

/**
 * @author Daniel Henrique Alves Lima
 */

import java.util.regex.Matcher
import java.util.regex.Pattern

if (binding.variables.containsKey("_grails_hibernate_spatial_install_package_called")) {
    return
}

_grails_hibernate_spatial_install_package_called = true

includeTargets << grailsScript("_GrailsEvents")

_hibernateSpatialUpdateConfig = {Map dataSourceOptions = [:] ->
    _hibernateSpatialUpdateDataSourceConfig(dataSourceOptions)
    
    File configDir = new File(grailsSettings.baseDir, '/grails-app/conf/')
    File configFile = new File(configDir, 'Config.groovy')
    
    Set geometryClasses = new LinkedHashSet()
    for (geometry in ['Geometry',
        'GeometryCollection', 'LineString', 'Point', 'Polygon',
        'MultiLineString', 'MultiPoint', 'MultiPolygon',
        'LinearRing', 'Puntal', 'Lineal', 'Polygonal']) {    
        geometryClasses << "com.vividsolutions.jts.geom.${geometry}".toString()
    }
    
    
    if (configFile.exists()) {
        try {
            Closure printUserTypes = {writer, String indentation = '' ->
                for (geometryClass in geometryClasses) {                
                    writer.println("${indentation}\'user-type\'(type:org.hibernatespatial.GeometryUserType, class:${geometryClass})")
                }
            }
            
            //File newConfigFile = new File(configDir, "${configFile.name}.tmp")
            File newConfigFile = File.createTempFile(configFile.name, 'tmp', configDir)
            boolean foundDefaultMapping = false
            boolean foundGeometryType = false
            boolean fileChanged = false
            
            Pattern userTypePattern = Pattern.compile('.*["\']user-type["\'].*')
            Pattern geometryTypePattern = Pattern.compile('.*class\\s*:\\s*([A-Za-z\\.]+).*')
            List userTypeLines = []
            
            newConfigFile.withPrintWriter {writer ->                
                configFile.withReader {reader ->
                    String line = null

                    while ((line = reader.readLine()) != null) {
                        if (!foundDefaultMapping) {
                            while (line != null && userTypePattern.matcher(line).matches()) {
                                userTypeLines << line
                                line = reader.readLine()
                            }

                            if (userTypeLines.size() > 0) {
                                for (userTypeLine in userTypeLines) {                                    
                                    Matcher matcher = geometryTypePattern.matcher(userTypeLine)
                                    if (matcher.matches()) {
                                        String geometryClass = matcher.replaceAll('$1')
                                        //println "geometryClass ${geometryClass}"
                                        geometryClasses.remove(geometryClass)
                                        
                                    }
                                }

                                println "geometryClasses ${geometryClasses}"
                                if (geometryClasses.size() > 0) {
                                    writer.println('   /* Added by the Hibernate Spatial Plugin. */')
                                    printUserTypes(writer, '   ')

                                    for (userTypeLine in userTypeLines) {
                                        writer.println(userTypeLine)
                                    }
                                    userTypeLines.clear()
                                    fileChanged = true
                                }
                                
                                foundDefaultMapping = true
                            }
                        }

                        writer.println(line)
                    }

                    if (!foundDefaultMapping) {
                        writer.println('/* Added by the Hibernate Spatial Plugin. */')
                        writer.println('grails.gorm.default.mapping = {')
                        printUserTypes(writer, '   ')
                        writer.println('}')
                        fileChanged = true
                    }
                }                                            
            }
            
            if (!fileChanged) {
                ant.delete(file: newConfigFile)
            } else {
                event "StatusUpdate", ["Updating ${configFile}"]
                ant.move(file: configFile, tofile: new File(configDir, "${configFile.name}.backup"))
                ant.move(file: newConfigFile, tofile: configFile)
            }
            
        } catch (Exception e) {
            event('StatusError', ["Could not update ${configFile}: ${e}"])
            throw e
        }
    }
    
    println ''
    println '================================================================'
    println 'Attention! Make sure you have:'
    println '1. A "spatially enabled" database;'
    println '2. The appropriate JDBC driver available on the classpath;'
    println '3. The JDBC connection URL configured according to your driver.'
}

_hibernateSpatialUpdateDataSourceConfig = {Map dataSourceOptions = [:] ->
    //println "dataSourceOptions ${dataSourceOptions}"
    
    boolean fileChanged = false
    List replacements = []
    Set unreplacedKeys = [] as Set
    
    for (option in dataSourceOptions.entrySet()) {
        Map replacement = [
            option: option,
            pattern: Pattern.compile("(\\S*\\s*${option.key}\\s*=\\s*)(.*)"),
            commentSub: '// $1$2',
            valueSub: '$2'
        ]
                
        replacements << replacement
    }
    
    //println "replacements ${replacements}"
    
    File configDir = new File(grailsSettings.baseDir, '/grails-app/conf/')
    for (fileName in ['DataSource.groovy', 'DataSources.groovy']) {
        File file = new File(configDir, fileName)
        //println "file ${file}"
        
        if (file.exists() && replacements.size() > 0) {
            try {
                //File newFile = new File(configDir, "${fileName}.tmp")
                newFile = File.createTempFile(fileName, 'tmp', configDir)
                newFile.withPrintWriter {writer ->
                    file.eachLine {line ->
                        String newLine = line
                        for (replacement in replacements) {
                            Matcher matcher = replacement.pattern.matcher(line)
                            //println "matches ${matcher.matches()}"
                            if (matcher.matches()) {
                                if (!matcher.replaceAll(replacement.valueSub).equals(replacement.option.value)) {
                                    newLine = matcher.replaceAll(replacement.commentSub)
                                    fileChanged = true                               
                                } else {
                                    unreplacedKeys << replacement.option.key
                                }
                                break
                            }
                        }
                        writer.println(newLine)
                    }
                    
                    if (dataSourceOptions.keySet() != unreplacedKeys) {
                       writer.println('')
                       writer.println('/* Added by the Hibernate Spatial Plugin. */')
                       writer.println('dataSource {')
                       for (option in dataSourceOptions.entrySet()) {
                           if (!unreplacedKeys.contains(option.key)) {
                               writer.println("   ${option.key} = ${option.value}")
                               fileChanged = true
                           }
                       }
                       writer.println('}')
                    }                    
                }
                
                //println "Updating ${file}"
                if (fileChanged) {
                    event "StatusUpdate", ["Updating ${file}"]
                    ant.move(file: file, tofile: new File(configDir, "${fileName}.backup"))
                    ant.move(file: newFile, tofile: file)
                } else {
                    ant.delete(file: newFile)
                }
                
            } catch (Exception e) {
                event('StatusError', ["Could not update ${file}: ${e}"])
                throw e
            }
        }
    }
}
