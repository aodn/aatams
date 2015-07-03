package au.org.emii.aatams.data

/**
 * Bootstrap uses instances of this to initialise data (in order to keep
 * lengthy, messy code out of BootStrap.groovy).
 * 
 * @author jburgess
 */
interface DataInitialiser  {
    void execute()
}

