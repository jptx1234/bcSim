package com.github.jptx1234.bingchuanSimulator.ent;

public class Ent {

	/**
	 * @params s 形如 180371|1|10.100.115.22|DC0EA1D025BA|408|win32|121309229|11111111
	 */
	 public static String ent(String s, String time)
	  {
	    String localObject;
	    try
	    {
	      b localb = new b();
	      localb.a(time, 0);
	      localb.a(s, s.length(), 0);
	      byte[] arrayOfByte1 = new byte[1024];
	      byte[] arrayOfByte2 = new byte[256];
	      localb.a(localb.q, arrayOfByte1, s.length() << 3);
	      localb.c(arrayOfByte2, arrayOfByte1, s.length() << 3);
	      localObject = "";
	      int i = 0;
	      while (true)
	      {
	        if (i >= arrayOfByte2.length)
	          return localObject;
	        if (arrayOfByte2[i] == 0)
	          break;
	        String str = localObject + (char)arrayOfByte2[i];
	        i++;
	        localObject = str;
	      }
	    }
	    catch (Exception localException)
	    {
	      localObject = "";
	    }
	    return localObject;
	  }
}
