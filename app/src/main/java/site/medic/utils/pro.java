/**
 * Copyright 2013, Haruki Hasegawa
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */

/**
 * from http://stackoverflow.com/questions/2641111/where-is-android-os-systemproperties
 */

package site.medic.utils;

import android.content.Context;

import java.lang.reflect.Method;

public class pro {

  public static Boolean getBoolean(Context context, String key, boolean def)
      throws IllegalArgumentException {
    return getBoolean(context.getClassLoader(), key, def);
  }

  public static Boolean getBoolean(ClassLoader cl, String key, boolean def)
      throws IllegalArgumentException {

    Boolean ret = def;

    try {
      @SuppressWarnings("rawtypes")
      Class SystemProperties = cl.loadClass("android.os.SystemProperties");


      @SuppressWarnings("rawtypes")
      Class[] paramTypes = new Class[2];
      paramTypes[0] = String.class;
      paramTypes[1] = boolean.class;

      @SuppressWarnings("unchecked")
      Method getBoolean = SystemProperties.getMethod("getBoolean", paramTypes);


      Object[] params = new Object[2];
      params[0] = new String(key);
      params[1] = Boolean.valueOf(def);

      ret = (Boolean) getBoolean.invoke(SystemProperties, params);

    } catch (IllegalArgumentException iAE) {
      throw iAE;
    } catch (Exception e) {
      ret = def;
    }

    return ret;
  }
}
