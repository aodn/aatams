package au.org.emii.aatams

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

/**
 * Utility methods common to all tests.
 * 
 * @author jburgess
 */
class TestUtils 
{
    static setupMessage(controller)
    {
        controller.metaClass.message = { LinkedHashMap args -> return "${args.code}" }
    }
}

