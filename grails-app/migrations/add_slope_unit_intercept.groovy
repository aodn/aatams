databaseChangeLog = {

    changeSet(author: 'ankit', id: '1675295815000-07') {

        addColumn(tableName: 'transmitter_type') {
            column(name: 'is_slope_needed', type: 'boolean', defaultValue: 'false') {
                constraints(nullable: 'false')
            }
        }

        addColumn(tableName: 'transmitter_type') {
            column(name: 'is_intercept_needed', type: 'boolean', defaultValue: 'false') {
                constraints(nullable: 'false')
            }
        }

        addColumn(tableName: 'transmitter_type') {
            column(name: 'is_unit_needed', type: 'boolean', defaultValue: 'false') {
                constraints(nullable: 'false')
            }
        }
    }
}
