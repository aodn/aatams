databaseChangeLog = {
    changeSet(author: "dnahodil", id: "1412133664000-1", failOnError: true) {
        addColumn(tableName: "project") {
            column(name: "is_protected", type: "bool", defaultValueBoolean: false) {
                constraints(nullable: "false")
            }
        }
    }
}
