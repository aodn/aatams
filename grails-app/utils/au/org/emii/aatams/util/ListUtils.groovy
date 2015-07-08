package au.org.emii.aatams.util

/**
 *
 * @author jburgess
 */
class ListUtils  {
    static final String SEPARATOR = ", "
    static String fold(list, attribute) {
        return fold(list, attribute, SEPARATOR)
    }

    static String fold(list, attribute, separator) {
        def retString = ""
        list.each {
            retString += String.valueOf(it[attribute]) + separator
        }

        // Remove the trailing ','
        if (retString.endsWith(separator)) {
            retString = retString.substring(0, retString.length() - separator.length())
        }

        return retString
    }
}

