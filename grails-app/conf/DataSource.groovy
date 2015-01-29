dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"

    properties
    {
        maxActive = 20
        validationQuery = "SELECT 1"
    }
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'

    show_sql = false
}

def env = System.getenv()

// environment specific settings
environments {
    development {
        dataSource {
            url = env['AATAMS_DATA_SOURCE_URL'] ?: "jdbc:postgresql://localhost:5432/aatams"
            username = env['AATAMS_DATA_SOURCE_USERNAME'] ?: "aatams"
            password = env['AATAMS_DATA_SOURCE_PASSWORD'] ?: "aatams"
        }
    }

    dbdiff {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            url = "jdbc:postgresql://localhost:5432/aatams_diff"
        }
    }

    test {
        dataSource {
            dbCreate = "create" // one of 'create', 'create-drop','update'
            url = "jdbc:postgresql://localhost:5432/aatams_test"
            username = "aatams_test"
            password = "aatams_test"
        }
    }

    production
    {
        // Problems with connecting to database in test/production?
        // Make *sure* you've got Grails Environment set to "production"!!
        dataSource
        {
            jndiName = "java:comp/env/jdbc/aatams3"
        }
    }
}

/* Added by the Hibernate Spatial Plugin. */
dataSource {
   dialect = org.hibernatespatial.postgis.PostgisDialect
}
