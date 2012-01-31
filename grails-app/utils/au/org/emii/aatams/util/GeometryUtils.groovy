package au.org.emii.aatams.util

import org.apache.shiro.SecurityUtils
import org.apache.shiro.UnavailableSecurityManagerException

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author jburgess
 */
class GeometryUtils 
{
	static double scrambleCoordinate(double coord)
	{
        if (!SecurityUtils.subject.isAuthenticated())
        {
            return truncate2(coord)

        }
        else
        {
            return coord
        }
	}
	
    static Point scrambleLocation(Point origPoint)
    {
        if (origPoint == null)
        {
            return null
        }
        
        try
        {
            if (!SecurityUtils.subject.isAuthenticated())
            {
                return doScramble(origPoint)
            }
            else
            {
                return origPoint
            }
        }
        catch (UnavailableSecurityManagerException e)
        {
            // It's possible that a security manager isn't yet accessible when
            // this is called (i.e. during start-up).
            return doScramble(origPoint)
        }
    }
    
    private static double round(double d, int decimalPlace) 
    {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_DOWN);
        return bd.doubleValue();
    }
    
    private static double truncate2 (double x)
    {
        return round(x, 2)
    }
    
    private static Point doScramble(Point origPoint)
    {
        double lon = truncate2(origPoint.getCoordinate().x)
        double lat = truncate2(origPoint.getCoordinate().y)

		Point p = new GeometryFactory().createPoint(
                    new Coordinate(lon, lat))
		p.SRID = origPoint.SRID
		
        return p
    }
}

