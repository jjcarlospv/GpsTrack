package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

import org.ksoap2.serialization.SoapSerializationEnvelope;

public interface KSoapService
{

    void setAndroidHttpTransport(CustomHttpTransportSE androidHttpTransport);

    void setDotNetEnvelope(SoapSerializationEnvelope envelope);
}
