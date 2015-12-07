/*
 * Esta clase es la encargada de definir si un punto se encuentra dentro de un poligono (coleccion de puntos).
 */

package smoothcombtt.xpo.com.gpstrack.TrackingModule.common;

public class Polygon
{

    /**
     * Indica si un punto se encuentra dentro o fuera de una coleccion de puntos
     *
     * @param geofencing .- Matriz de puntos (poligono)
     * @param pointGPS   .- Punto
     * @return.- True si punto se encuentra dentro de matriz de puntos, false por el contrario.
     */
    public boolean contains(Point[] geofencing, Point pointGPS)
    {
        int sides = geofencing.length;
        int hits = 0;
        double lastx = geofencing[sides - 1].latitude;
        double lasty = geofencing[sides - 1].longitude;
        double curx, cury;
        // Walk the edges of the polygon
        for (int i = 0; i < sides; lastx = curx, lasty = cury, i++)
        {
            curx = geofencing[i].latitude;
            cury = geofencing[i].longitude;
            if (cury == lasty)
            {
                continue;
            }
            double leftx;
            if (curx < lastx)
            {
                if (pointGPS.latitude >= lastx)
                {
                    continue;
                }
                leftx = curx;
            }
            else
            {
                if (pointGPS.latitude >= curx)
                {
                    continue;
                }
                leftx = lastx;
            }

            double test1, test2;
            if (pointGPS.longitude < lasty)
            {
                if (pointGPS.longitude < cury || pointGPS.longitude >= lasty)
                {
                    continue;
                }
                if (pointGPS.latitude < leftx)
                {
                    hits++;
                    continue;
                }
                test1 = pointGPS.latitude - curx;
                test2 = pointGPS.longitude - cury;
            }
            else
            {
                if (pointGPS.longitude < lasty || pointGPS.longitude >= cury)
                {
                    continue;
                }
                if (pointGPS.latitude < leftx)
                {
                    hits++;
                    continue;
                }
                test1 = pointGPS.latitude - lastx;
                test2 = pointGPS.longitude - lasty;
            }

            if (test1 < (test2 / (lasty - cury) * (lastx - curx)))
            {
                hits++;
            }
        }
        return ((hits & 1) != 0);
    }

}
