import groovy.sql.Sql
import au.org.emii.aatams.*

def dataSource = ctx.dataSource

def sql = new Sql(dataSource)

println sql.rows('''select station_id, count(*) from detection_extract_view group by station_id''')
