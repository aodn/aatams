package au.org.emii.aatams.export

import grails.test.*

class AbstractStreamingExporterServiceTests extends GrailsUnitTestCase {

    void testPaging() {

        def actualResults = []

        def nullOutputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        }

        def exporter = new AbstractStreamingExporterService() {

            // These are all irrelevant to the test, but required for compilation.
            void writeCsvHeader(OutputStream out) {}
            def applyEmbargo(results, params) { return results }
            String getReportName() { "report" }
            int getLimit() { return 123 }
            void indicateExportStart(params) {}

            // Store each page as we go...
            def writeCsvChunk(resultList, OutputStream out) {
                actualResults += resultList
            }

            //
            // Simulate 3 pages being returned:
            //
            // page 0: two rows returned from DB, but all embargoed
            // page 1: one row returned from DB, and not embargoed
            // page 2: no more rows
            //
            def pageNumber = 0
            def readData(filterParams) {

                def pageResults = [
                    [results: [], rowCount: 2],
                    [results: ['some result'], rowCount: 1],
                    [results: [], rowCount: 0]
                ]

                return pageResults[pageNumber++]
            }
        }

        exporter.writeCsvData([:], nullOutputStream)

        assertEquals(['some result'], actualResults)
    }
}
