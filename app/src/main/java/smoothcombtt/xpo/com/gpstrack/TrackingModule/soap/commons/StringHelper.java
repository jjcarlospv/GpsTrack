/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package smoothcombtt.xpo.com.gpstrack.TrackingModule.soap.commons;

import org.kobjects.util.Strings;

import java.util.Hashtable;
import java.util.Vector;


/**
 * @author piero.delaguila
 */
public class StringHelper extends Helper
{

    public static boolean matches(String string, char[] regexp)
    {
        char[] tmp = string.toCharArray();
        for (int i = 0; i < tmp.length; i++)
        {
            int matchesC = 0;
            for (int j = 0; j < regexp.length - 1; j += 2)
            {
                if (tmp[i] >= regexp[j] && tmp[i] <= regexp[j + 1])
                {
                    matchesC++;
                }

            }
            if (matchesC == 0)
            {
                return false;

            }
        }
        return true;
    }

    public static String replace(String string, String[] stringsToReplace)
    {
        char token = '?';
        char[] tmp = string.toCharArray();
        StringBuffer res = new StringBuffer();
        int rep = 0;
        for (int i = 0; i < tmp.length; i++)
        {
            if (tmp[i] == token)
            {
                if (stringsToReplace.length > 0 && rep < stringsToReplace.length)
                {
                    char[] tmp1 = stringsToReplace[rep++].toCharArray();
                    for (int k = 0; k < tmp1.length; k++)
                    {
                        res.append(tmp1[k]);
                    }
                }
            }
            else
            {
                res.append(tmp[i]);
            }
        }
        return res.toString();
    }

    // public static String convertBytesToString(final byte[] matriz) {
    // final StringBuffer str = new StringBuffer();
    // Application.getApplication().invokeAndWait(new Runnable() {
    //
    // public void run() {
    // int len = matriz.length;
    //
    // int t = 0;
    // while (t < len) {
    // str.append(matriz[t] + "|");
    // t++;
    // if (t % 100000 == 0) {
    // try {
    // Thread.sleep(1000);
    // System.out.println("StringHelper.convertBytesToString(...).new Runnable() {...}.run() resting...");
    // } catch (InterruptedException e) {

    // e.printStackTrace();
    // }
    //
    // }
    // }
    //
    // }
    // });
    // return str.toString().substring(0, str.length() - 1);
    //
    // }

    public static Hashtable toHashTable(StringBuffer contents, char token)
    {
        if (contents == null)
        {
            return null;
        }
        String line = null;
        Hashtable ret = new Hashtable();
        boolean lastLine = false;
        int keyIndex = 1;
        while (!lastLine)
        {
            try
            {

                if (contents.toString().indexOf("\n") >= 0)
                {
                    line = contents.toString().substring(0, contents.toString().indexOf("\n") + 1);
                    line = line.substring(0, line.length() - 2);
                    contents.delete(0, contents.toString().indexOf("\n") + 1);
                }
                else
                {
                    line = contents.toString();
                    lastLine = true;
                }
                if (!ret.containsKey(line.substring(0, line.indexOf(token)).trim()))
                {
                    ret.put(line.substring(0, line.indexOf(token)).trim(), line.substring(line.indexOf(token) + 1, line.length()));
                    keyIndex = 1;
                }
                else
                {

                    ret.put(line.substring(0, line.indexOf(token)).trim() + keyIndex++,
                            line.substring(line.indexOf(token) + 1, line.length()));
                }
                if (lastLine)
                {
                    break;
                }
            }
            catch (Exception e)
            {
                break;
            }
        }
        return ret;
    }
/*
    public static String convertChoiceArrayToString(Vector elementList, String separatorToken)
    {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < elementList.size(); i++)
        {
            ChoiceOption o = (ChoiceOption) elementList.elementAt(i);
            ret.append(o.getValue()).append(separatorToken);
        }
        if (elementList.size() > 0)
        {
            ret.deleteCharAt(ret.length() - 1);
        }
        return ret.toString();
    }

    public static String convertInputtoString(Vector inputData, char c)
    {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < inputData.size(); i++)
        {
            ret.append(inputData.elementAt(i));
            ret.append(c);
        }
        if (ret.length() > 0)
        {
            ret.deleteCharAt(ret.length() - 1);
        }

        return ret.toString();
    }
    */

    public static final Vector split(final String data, final char splitChar)
    {
        Vector v = new Vector();

        String working = data;
        int index = working.indexOf(splitChar);

        while (index != -1)
        {
            String tmp = "";
            if (index > 0)
            {
                tmp = working.substring(0, index);
            }
            v.addElement(tmp);

            working = working.substring(index + 1);

            // Find the next index
            index = working.indexOf(splitChar);
        }

        // Add the rest of the working string
        v.addElement(working);

        return v;
    }

    public static String getLoadTypeString(String loadType)
    {
        if (loadType.equals("L"))
        {
            return "Loaded";
        }
        else if (loadType.equals("E"))
        {
            return "Empty";
        }
        else if (loadType.equals("B"))
        {
            return "Bobtail";
        }
        else if (loadType.equals("C"))
        {
            return "Chassis";
        }
        else
        {
            return loadType;
        }
    }

    public static String fromIntToStringDate(long originEta, String string)
    {
        long date = originEta;
        String tmpDate = String.valueOf(date);
        char[] tmpDateC = tmpDate.toCharArray();
        String minute = "" + tmpDateC[tmpDateC.length - 2] + tmpDateC[tmpDateC.length - 1];
        String hour = "" + tmpDateC[tmpDateC.length - 4] + tmpDateC[tmpDateC.length - 3];
        String year = "" + tmpDateC[tmpDateC.length - 8] + tmpDateC[tmpDateC.length - 7] + tmpDateC[tmpDateC.length - 6]
                + tmpDateC[tmpDateC.length - 5];
        String day = "" + tmpDateC[tmpDateC.length - 10] + tmpDateC[tmpDateC.length - 9];
        String month = "" + (tmpDateC.length > 11 ? tmpDateC[tmpDateC.length - 12] : '0') + tmpDateC[tmpDateC.length - 11];
        return month + "/" + day + "/" + year + " " + hour + ":" + minute;
    }

    public static String formatMacaddress(String macAddress)
    {
        String withoutPoints = "";

        if(macAddress != null){
            withoutPoints = Strings.replace(macAddress, ":", "");
        }

        return withoutPoints;
    }

}
