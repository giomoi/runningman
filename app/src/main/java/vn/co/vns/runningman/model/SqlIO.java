package vn.co.vns.runningman.model;

import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by hoangtuan on 2/20/17.
 */
public class SqlIO {

    private final String DB_INTERGER = " INTEGER";
    private final String DB_REAL = " REAL";
    private final String DB_BOOLEAN = " BOOLEAN";
    private final String DB_TEXT = " TEXT";
    private final String DB_PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT NOT NULL";
    private HashMap<String, Field> fieldMap=new HashMap<String, Field>();
    public SqlIO(){

    }

    /**
     * Generate a object named clazz from cursor
     * @param clazz Name of class
     * @param cursor Cursor
     * @param <T> Template
     * @return T object
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> T toObject(Class<?> clazz, Cursor cursor){
        if(cursor!=null){
            try {
                T object = (T)createObject(clazz.getName());
                if(object!=null){
                    for (int i =0 ; i< cursor.getColumnCount(); i++)
                    {
                        String data = cursor.getString(i);
                        String columnCursor = cursor.getColumnName(i);
                        Field field = getField(object.getClass(), columnCursor);
                        field.setAccessible(true);
                        if(double.class.isAssignableFrom(field.getType())) {
                            field.set(object, Double.parseDouble(data));
                        }else if(String.class.isAssignableFrom(field.getType())){
                            field.set(object, data);
                        }else if(float.class.isAssignableFrom(field.getType())){
                            field.set(object, Float.parseFloat(data));
                        }else if(boolean.class.isAssignableFrom(field.getType())){
                            field.set(object, Boolean.parseBoolean(data));
                        }else if(int.class.isAssignableFrom(field.getType())){
                            field.set(object, Integer.parseInt(data));
                        }
                    }
                    return object;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (ClassCastException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Get a field from a object
     * @param clazz Class
     * @param name name of attribute.
     * @return
     */
    private Field getField(Class<?> clazz, String name) {
        Field field = fieldMap.get(clazz.getSimpleName()+"-"+name);
        if(field == null) {
            Class<?> tempClazz = clazz;
            while (tempClazz != null && field == null) {
                try {
                    field = clazz.getDeclaredField(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tempClazz = tempClazz.getSuperclass();
            }
            fieldMap.put(clazz.getSimpleName()+"-"+name, field);
        }
        return field;
    }

    /**
     * Create a instance of a class
     * @param className name of class
     * @return object
     */
    private Object createObject(String className) {
        Object object;
        try  {
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor();
            object = ctor.newInstance();
            return object;
        }  catch (ClassNotFoundException e) {
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param clazz
     */
    public String toTable(Class<?> clazz){
        String strMainSQL = "CREATE TABLE IF NOT EXISTS " + clazz.getSimpleName().toUpperCase() +" (%s) ";
        String strColumn = "";
        Field[] fs = clazz.getDeclaredFields();
        if(fs.length>0) {
            for (Field field : fs) {
                field.setAccessible(true);
                strColumn += generateStrColumn(field) + ", ";
            }
            strColumn = strColumn.substring(0, strColumn.length() - 2);
            String finalString = String.format(strMainSQL, strColumn);
            Log.i("Atts", finalString);
            return finalString;
        }
        return null;
    }

    /**
     *
     * @param field
     * @return
     */
    private String generateStrColumn(Field field) {
        String result = "";
        String fieldName = field.getName();
        if(fieldName.contains("__")) return "";
        Class<?> typeOfAtt = field.getType();
        if(double.class.isAssignableFrom(typeOfAtt)) {
            result += fieldName + DB_REAL;
        }else if(String.class.isAssignableFrom(typeOfAtt)){
            result += fieldName + DB_TEXT;
        }else if(float.class.isAssignableFrom(typeOfAtt)){
            result += fieldName + DB_REAL;
        }else if(boolean.class.isAssignableFrom(typeOfAtt)){
            result += fieldName + DB_BOOLEAN;
        }else if(int.class.isAssignableFrom(typeOfAtt)){
            result += fieldName + DB_INTERGER;
        }

        if(fieldName.charAt(0)=='_'){
            result+=DB_PRIMARY_KEY;
        }
        return result;
    }
}
