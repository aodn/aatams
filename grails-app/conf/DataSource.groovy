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
//            url = "jdbc:hsqldb:file:devDB;shutdown=true"
//            url = "jdbc:hsqldb:mem:devDB"
            driverClassName = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:5432/aatams"
            username = "aatams"
            password = "aatams"
            
        }
    }
    test {
        dataSource {
//            dbCreate = "update"
            url = "jdbc:comp/env/jdbc/aatams_dev"
        }
    }
    
    production 
    {
        // Problems with connecting to database in test/production? 
        // Make *sure* you've got Grails Environment set to "production"!!
        dataSource 
        {
            dbCreate = "create" // one of 'create', 'create-drop','update'
            jndiName = "java:comp/env/jdbc/aatams3"
        }
    }
}
