databaseChangeLog = {
    include file: 'rebased_db_initialisation.groovy'
    include file: 'missing_indices.groovy'
    include file: 'foreign_key_indices.groovy'
}
