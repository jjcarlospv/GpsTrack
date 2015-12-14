package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

import android.util.Log;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.WSMobileService;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapDotNetService;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapField;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapServiceComplexParam;
import smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.annotations.KSoapServiceParam;

public abstract class AnnotatedService implements KSoapService
{

    protected SoapSerializationEnvelope envelope;
    protected SoapObject request;
    protected String nameSpace = "http://traking.wsServices/";

    protected String soapAction = null;
    protected CustomHttpTransportSE androidHttpTransport;

    protected final Class interceptAndInitialize(Class<WSMobileService> clazz, String methodName, Object... params)
    {
        try
        {
            Method method = findMethodByName(clazz, methodName);

            KSoapDotNetService annotation = method.getAnnotation(KSoapDotNetService.class);
            request = new SoapObject(nameSpace, annotation.methodName());
            soapAction = nameSpace + annotation.methodName();
            for (int i = 0; i < params.length; i++)
            {
                Object param = params[i];

                if (param != null)
                {
                    Annotation[] annotations = method.getParameterAnnotations()[i];
                    for (Annotation paramAnnotation : annotations)
                    {
                        if (paramAnnotation instanceof KSoapServiceParam)
                        {

                            request.addProperty(((KSoapServiceParam) paramAnnotation).value(), param.toString());

                        }
                        else if (paramAnnotation instanceof KSoapServiceComplexParam)
                        {

                            SoapObject soapObject = new SoapObject("", ((KSoapServiceComplexParam) paramAnnotation).value());

                            for (Method accesor : param.getClass().getMethods())
                            {
                                if (accesor.getName().startsWith("get"))
                                {
                                    KSoapField annotation2 = accesor.getAnnotation(KSoapField.class);
                                    if (annotation2 != null)
                                    {
                                        soapObject.addProperty(annotation2.value(), accesor.invoke(param, (Object[]) null));
                                    }
                                }
                            }
                            request.addSoapObject(soapObject);
                        }
                    }
                }

            }

            envelope.setOutputSoapObject(request);
            return method.getReturnType();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }

    }

    private Method findMethodByName(Class<WSMobileService> clazz, String methodName)
    {
        Method ret = null;
        for (Method method : clazz.getMethods())
        {
            String name = method.getName();
            if (name.equals(methodName))
            {
                ret = method;
                break;
            }
        }
        return ret;
    }

    protected final Object parseResult(SoapObject soap, Class clazz)
    {
        Object instance;
        try
        {
            // soap.getProperty(aResult.value());
            instance = clazz.newInstance();
            for (Method method : clazz.getMethods())
            {
                try
                {
                    if (method.getName().startsWith("set"))
                    {
                        KSoapField annotation = method.getAnnotation(KSoapField.class);
                        if (annotation == null)
                        {
                            continue;
                        }
                        if (method.getParameterTypes()[0] == Integer.class)
                        {
                            if (!soap.getProperty(annotation.value()).toString().equals("anyType{}"))
                            {
                                method.invoke(instance, Integer.valueOf(soap.getProperty(annotation.value()).toString()));
                            }
                            else
                            {
                                method.invoke(instance, null);
                            }
                        }
                        else if (method.getParameterTypes()[0] == Double.class)
                        {

                            if (!soap.getProperty(annotation.value()).toString().equals("anyType{}"))
                            {
                                method.invoke(instance, Double.valueOf(soap.getProperty(annotation.value()).toString()));
                            }
                            else
                            {
                                method.invoke(instance, null);
                            }
                        }
                        else if (method.getParameterTypes()[0] == Boolean.class)
                        {
                            if (!soap.getProperty(annotation.value()).toString().equals("anyType{}"))
                            {
                                method.invoke(instance, Boolean.valueOf(soap.getProperty(annotation.value()).toString()));
                            }
                            else
                            {
                                method.invoke(instance, null);
                            }
                        }
                        else if (method.getParameterTypes()[0] == String.class)
                        {
                            if (soap.getProperty(annotation.value()) != null && !soap.getProperty(annotation.value()).toString().equals("anyType{}"))
                            {
                                method.invoke(instance, soap.getProperty(annotation.value()).toString());
                            }
                            else
                            {
                                method.invoke(instance, "");
                            }
                        }
                        else if (method.getParameterTypes()[0].isArray())
                        {
                            CustomSoapObject tmp = new CustomSoapObject(soap);
                            PropertyInfo[] params = (PropertyInfo[]) tmp.getPropertyAsArray(annotation.value());
                            Class<?> componentType = method.getParameterTypes()[0].getComponentType();
                            Object arrayMutatorValues = Array.newInstance(componentType, params.length);
                            for (int j = 0; j < params.length; j++)
                            {
                                // try {
                                PropertyInfo detail = params[j];
                                Object parseResult = null;
                                if (detail.getValue() instanceof SoapObject)
                                {
                                    parseResult = parseResult((SoapObject) detail.getValue(), componentType);
                                }
                                else if (detail.getValue() instanceof SoapPrimitive)
                                {
                                    if (componentType == Double.class)
                                    {
                                        SoapPrimitive value = ((SoapPrimitive) detail.getValue());
                                        parseResult = Double.valueOf(value.toString());
                                    }
                                    else if (componentType == String.class)
                                    {
                                        SoapPrimitive value = ((SoapPrimitive) detail.getValue());
                                        parseResult = String.valueOf(value.toString());
                                    }
                                }
                                else
                                {
                                    System.out.println(detail);
                                }
                                Array.set(arrayMutatorValues, j, parseResult);
                            }
                            method.invoke(instance, arrayMutatorValues);
                        }
                        else
                        {
                            try
                            {
                                method.invoke(instance, soap.getProperty(annotation.value()).toString());
                            }
                            catch (Throwable e)
                            {

                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    Log.e("AnnotatedService", e.getMessage());

                    continue;
                }
            }
            return instance;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error Parsing Result");
        }
    }

    protected final Object callWS(String operation, Object... params)
    {
        Object result = null;
        try
        {            Class interceptAndInitialize = interceptAndInitialize(WSMobileService.class, operation, params);
            androidHttpTransport.call(soapAction, envelope); ///CONSULTA AL WEB SERVICE

            result = parseResult((SoapObject) envelope.getResponse(), interceptAndInitialize);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //throw new PCMCServiceException(e, e.getMessage()); //  comment jean
        }
        return null; // add jean
    }

    @Override
    public void setAndroidHttpTransport(CustomHttpTransportSE androidHttpTransport)
    {
        this.androidHttpTransport = androidHttpTransport;
    }

    @Override
    public void setDotNetEnvelope(SoapSerializationEnvelope envelope)
    {
        this.envelope = envelope;

    }
}
