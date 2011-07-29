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
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            driverClassName = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:5433/aatams"
            username = "aatams"
            password = "aatams"
            
        }
    }

    //
    // data-entry copy
    //
//    development {
//        dataSource {
//            dbCreate = "update"
//            driverClassName = "org.postgresql.Driver"
////            url = "jdbc:postgresql://localhost:5432/aatams3"
//            url = "jdbc:postgresql://localhost:5433/aatams3"
//            username = "aatams"
//            password = "aatams"
//            
//        }
//    }
    test {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            driverClassName = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:5433/aatams_test"
            username = "aatams"
            password = "aatams"
        }
//        dataSource {
//            dbCreate = "update"
//            url = "jdbc:hsqldb:mem:testDb"
//        }
    }
    
    production 
    {
        // Problems with connecting to database in test/production? 
        // Make *sure* you've got Grails Environment set to "production"!!
        dataSource 
        {
//            dbCreate = "create" // one of 'create', 'create-drop','update'
            jndiName = "java:comp/env/jdbc/aatams3"
        }
    }
}
