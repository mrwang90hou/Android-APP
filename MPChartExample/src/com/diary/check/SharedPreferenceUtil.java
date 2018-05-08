package com.diary.check;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * SharedPreferences 工具类<br>
 * 
 */
public class SharedPreferenceUtil {

	public static final String SHAREDPREFERENCE_CONFIG = "sino_sp_config";
	/**
	 * 保存数据的方法
	 * 
	 * @param context
	 * @param key
	 *            键值对的key
	 * @param object
	 *            键值对的值
	 */
	public static void put(Context context, String key, Object object) {
        if(object==null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCE_CONFIG, Context.MODE_PRIVATE);
		Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 得到保存数据
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            存数据时候的key
	 * @param defaultObject
	 *            不能为空,取什么类型的数据就应该是什么类型的变量
	 * @return
	 */
	public static<T> T get(Context context, String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCE_CONFIG, Context.MODE_PRIVATE);
		if (defaultObject instanceof String) {
			String stringResult = sp.getString(key, (String) defaultObject);
			return (T) stringResult;
		} else if (defaultObject instanceof Integer) {
			Integer integerResult = sp.getInt(key, (Integer) defaultObject);
			return (T) integerResult;
		} else if (defaultObject instanceof Boolean) {
			Boolean booleanResult = sp.getBoolean(key, (Boolean) defaultObject);
			return (T)booleanResult;
		} else if (defaultObject instanceof Float) {
			Float floatResult = sp.getFloat(key, (Float) defaultObject);
			return (T)floatResult;
		} else if (defaultObject instanceof Long) {
			Long longResult = sp.getLong(key, (Long) defaultObject);
			return (T)longResult;
		}

		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            存数据时候的key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCE_CONFIG, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCE_CONFIG, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            存数据时候的key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCE_CONFIG, Context.MODE_PRIVATE);
		boolean result = sp.contains(key);
		return result;
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @param context
	 *            上下文
	 * @return 返回sp所有的配置
	 */
	public static Map<String, ?> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCE_CONFIG, Context.MODE_PRIVATE);
		return sp.getAll();
	}

	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}
			return null;
		}

		public static void apply(Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}
	/**
	 * 
	 * @author wxy
	 * @createdate 2015年8月16日14:47:23
	 * @Description: (保存ArrayList)
	 * @param context
	 * @param key
	 * @param values
	 * 
	 */
	public static void setSharedPreference(Context context, String key, ArrayList<String> values) {
		String regularEx = "#";
		String str = "";
		SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
		if (values != null && values.size() > 0) {
			for (String value : values) {
				str += value;
				str += regularEx;
			}
			Editor et = sp.edit();
			et.putString(key, str);
			et.commit();
		}
	}
    public static void clearKey(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences(
                key, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }
	public static String[] getSharedPreference(Context context, String key) {
		String regularEx = "#";
		String[] str = null;
		SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
		String values;
		values = sp.getString(key, "");
		str = values.split(regularEx);

		return str;
	}

	public static void removelist(Context context, String key) {
		SharedPreferences shared = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        Editor editor = shared.edit();
		editor.remove(key);
        editor.commit();
	}
}