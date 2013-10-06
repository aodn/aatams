databaseChangeLog = {
    include file: 'rebased_db_initialisation.groovy'
    include file: 'missing_indices.groovy'
    include file: 'foreign_key_indices.groovy'
    include file: 'correct_url_in_detection_count_per_station.groovy'
    include file: 'truncate_instead_of_delete_on_refresh_matview.groovy'
}
