package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class CustomSoapObject extends SoapObject
{

    public CustomSoapObject(String namespace, String name)
    {
        super(namespace, name);
    }

    public CustomSoapObject(SoapObject soap)
    {
        super(soap.getNamespace(), soap.getName());
        for (int i = 0; i < soap.getPropertyCount(); i++)
        {
            try
            {
                PropertyInfo propertyInfo = new PropertyInfo();
                soap.getPropertyInfo(i, propertyInfo);
                this.addProperty(propertyInfo);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }

        }
    }

    public PropertyInfo[] getPropertyAsArray(String propertyName)
    {
        List<PropertyInfo> res = new ArrayList<PropertyInfo>();
        for (Object property : properties)
        {
            if (property instanceof PropertyInfo)
            {
                if (((PropertyInfo) property).getName().equals(propertyName))
                {
                    res.add(((PropertyInfo) property));
                }
            }
            else
            {
                System.out.println(property.toString());
            }
        }

        return res.toArray(new PropertyInfo[res.size()]);
    }

}
