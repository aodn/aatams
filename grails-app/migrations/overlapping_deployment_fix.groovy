databaseChangeLog = {
    changeSet(author: 'jburgess', id: '1435639819000-01') {

        [
            [ name: "initialisationdatetime_timestamp", type: "TIMESTAMP WITH TIME ZONE" ],
            [ name: "initialisationdatetime_zone", type: "VARCHAR(255)" ]
        ].each { columnDetails ->

            def name = columnDetails.name
            def type = columnDetails.type

            addColumn(tableName: 'receiver_deployment') {
                column(name: "${name}_bak", type: type)
            }

            sql("update receiver_deployment set ${name}_bak = ${name}")

            rollback {
                sql("update receiver_deployment set ${name} = ${name}_bak where ${name}_bak is not null")
                dropColumn(tableName: 'receiver_deployment', columnName: "${name}_bak")
            }
        }

        def deployments_cte = """
-- CTE with a proper range type (tstzrange) - then we can use the 'overlaps' (&&) operator below.
with deployments as (
  select
    installation_station.name as station_name,
    deployment_and_recovery.receiver_id as receiver_id,
    receiver_name, deployment_and_recovery.receiver_deployment_id,
    receiver_deployment.initialisationdatetime_timestamp as initialisationdatetime_timestamp,
    receiver_deployment.deploymentdatetime_timestamp,
    tstzrange(receiver_deployment.initialisationdatetime_timestamp, recoverydatetime_timestamp) as deployment_interval
  from deployment_and_recovery
  join receiver on deployment_and_recovery.receiver_id = receiver.id
  join receiver_deployment on deployment_and_recovery.receiver_deployment_id = receiver_deployment.id
  join installation_station on receiver_deployment.station_id = installation_station.id
  where receiver_deployment.initialisationdatetime_timestamp is not null and recoverydatetime_timestamp is not null
  and receiver_deployment.initialisationdatetime_timestamp <= recoverydatetime_timestamp
)

"""

        // Log the overlapping deployments which will be "auto-corrected".
        grailsChange {
            change {
                log.warn("Setting initialisation date equal to deployment date for following deployments:")
                sql.eachRow(deployments_cte + """
select
  lhs.receiver_id, lhs.receiver_name,
  lhs.deployment_interval as lhs_deployment_interval,
  rhs.deployment_interval as rhs_deployment_interval,
  'https://aatams.emii.org.au/aatams/receiver/show/' || lhs.receiver_id as receiver_url
from deployments lhs
join deployments rhs
  on lhs.receiver_name = rhs.receiver_name
  and lhs.deployment_interval && rhs.deployment_interval
  and lhs.receiver_deployment_id != rhs.receiver_deployment_id
  and lhs.initialisationdatetime_timestamp <= rhs.initialisationdatetime_timestamp -- just so that we get only one record for each overlap
  and lhs.station_name != rhs.station_name
order by lhs.receiver_name, lhs.initialisationdatetime_timestamp
""")            { row ->
    log.warn(row)
                }

            }
        }

        // Do the auto-correct (of overlaps).
        sql(deployments_cte + """

update receiver_deployment
set initialisationdatetime_timestamp = deploymentdatetime_timestamp
where id in (
  select receiver_deployment.id as id
  from deployments lhs
  join deployments rhs
    on lhs.receiver_name = rhs.receiver_name
    and lhs.deployment_interval && rhs.deployment_interval
    and lhs.receiver_deployment_id != rhs.receiver_deployment_id
    and lhs.initialisationdatetime_timestamp <= rhs.initialisationdatetime_timestamp -- just so that we get only one record for each overlap
)
""")

        // Log deployments where initialisation date > deployment date.
        grailsChange {
            change {
                log.warn("Setting initialisation date equal to deployment date for following deployments:")
                sql.eachRow("""
select id, receiver_id, initialisationdatetime_timestamp, deploymentdatetime_timestamp
from receiver_deployment
where initialisationdatetime_timestamp > deploymentdatetime_timestamp
""") { row ->
    log.warn(row)
                }
            }
        }

        // Do the auto-correct (of init date > deployment date)
        sql("""
update receiver_deployment
set initialisationdatetime_timestamp = deploymentdatetime_timestamp
where initialisationdatetime_timestamp > deploymentdatetime_timestamp
""")
    }
}
