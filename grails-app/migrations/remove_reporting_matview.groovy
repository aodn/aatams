databaseChangeLog = {

    changeSet(author: "ja", id: "1458164415000-01") {

        dropView(viewName: "aatams_acoustictag_totals_project_view")

        dropView(viewName: "aatams_acoustictag_totals_species_view")

        dropView(viewName: "aatams_acoustictag_data_summary_project_view")

        dropView(viewName: "aatams_acoustictag_all_deployments_view")

        dropView(viewName: "aatams_acoustictag_data_summary_species_view")

        dropTable(tableName: 'aatams_acoustictag_totals_species_view_mv')

        dropTable(tableName: 'aatams_acoustictag_all_detections_view_mv')

        dropTable(tableName: 'aatams_acoustictag_all_deployments_mv')
    }
}
