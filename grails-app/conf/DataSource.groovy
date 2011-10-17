dataSource {
    pooled = true
//    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
    dialect = 'org.hibernatespatial.postgis.PostgisDialect'
}
// environment specific settings
environments {
    development {
        dataSource {
//            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            driverClassName = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:5432/aatams"
            username = "aatams"
            password = "fishybusiness"
            
            properties 
            {
                maxActive = 20
            }
        }
    }

    test {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            driverClassName = "org.postgresql.Driver"
            url = "jdbc:postgresql://dbtest.emii.org.au:5432/aatams_test?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
            username = "aatams"
            password = "fishybusiness"

            properties 
            {
                maxActive = 20
            }
        }
    }
    
    production 
    {
        // Problems with connecting to database in test/production? 
        // Make *sure* you've got Grails Environment set to "production"!!
        dataSource 
        {
            jndiName = "java:comp/env/jdbc/aatams3"
            properties 
            {
                maxActive = 20
            }
        }
    }
}
