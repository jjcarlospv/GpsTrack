package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class CustomHttpTransportSE extends HttpTransportSE
{

    public CustomHttpTransportSE(String url)
    {
        super(url, 120000); //Configurar timeout servicios - 1 min cambiar a algun valor configurable
    }

    protected byte[] createRequestData(SoapEnvelope envelope) throws IOException
    {
        String result = new String(super.createRequestData(envelope));
        result = result.replace(" i:type=\"d:", " type=\"s:");
        result = result.replace(" i:type=\":", " type=\"tns:");
        result = result.replace(" i:null=\"true\"", "");
        return result.getBytes();
    }
}