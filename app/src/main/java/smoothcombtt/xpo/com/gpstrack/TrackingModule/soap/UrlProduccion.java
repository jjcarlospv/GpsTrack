package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap;

public class UrlProduccion extends MiddleTierURL
{

    public String toString()
    {
        String ret = super.toString();
        if (ret == null)
        {
            return "http://smoothcom.pacer.com/pcmc.webservices/wsmobileservice.asmx";
        }
        return ret;
    }
}
