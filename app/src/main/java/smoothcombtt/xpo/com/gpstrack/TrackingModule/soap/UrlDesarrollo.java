package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap;

public class UrlDesarrollo extends MiddleTierURL
{

    public String toString()
    {
        String ret = super.toString();
        if (ret == null)
        {
            return "http://200.123.0.82/PCMC.WebServices/WSMobileService.asmx";
        }
        return ret;
    }
}
