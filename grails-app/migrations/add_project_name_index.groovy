databaseChangeLog = {
    changeSet(author: "tfotak", id: "1392946213000-1") {
        createIndex(indexName: "project_name_index", tableName: "project", unique: "false") {
            column(name: "name")
        }
    }
}
