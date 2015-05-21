package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import groovy.sql.*

abstract class AbstractJdbcTemplateVueDetectionFileProcessorServiceIntegrationTests extends AbstractGrailsUnitTestCase
{
    def dataSource
    def grailsApplication
    def vueDetectionFileProcessorService
    def sql

    protected void setUp()
    {
        super.setUp()

        sql = Sql.newInstance(dataSource)

        def statistics = Statistics.findByKey('numValidDetections')?: new Statistics(key: 'numValidDetections', value: sql.firstRow('select count(*) from valid_detection;').count)
        statistics.save(failOnError: true, flush: true)
    }
}
