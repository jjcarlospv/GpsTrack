package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations;

public enum MiddleTier
{

    UAT("http://clauster123-001-site1.btempurl.com/tracking/wsTracking.asmx");

    private final String wsdl;

    ///192.168.0.62
    MiddleTier(String wsdl)
    {
        this.wsdl = wsdl;
    }

    public String toString()
    {
        return wsdl;
    }

}
