package codetagging.simflect.builder;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by govind on 23/05/16.
 */
public class TypeReflection {

    public static Object get(String className, String methodName, Object param, Class classDataType,  Context context)
            throws ClassNotFoundException, NoSuchMethodException, Exception {
        Object returnObject = null;
        try {
            Class classType = Class.forName(className);
            Object classObject = null;
            Constructor[] constructors = classType.getConstructors();
            for (Constructor constructor: constructors
                    ) {
                Class[] parameterTypes = constructor.getParameterTypes();
                if(parameterTypes != null) {
                    if(parameterTypes.length == 1 && parameterTypes[0] == Context.class) {
                        classObject = constructor.newInstance(context);
                        break;
                    }
                }
            }
            Method method = classType.getDeclaredMethod(methodName, classDataType);
            returnObject = method.invoke(classObject, param);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new ClassNotFoundException(className);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new NoSuchMethodException(methodName);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return returnObject;
    }

    public static Object getDefault(String className, String methodName, int instanceParam)
            throws ClassNotFoundException, NoSuchMethodException, Exception {
        Object returnObject = null;
        try {
            Class classType = Class.forName(className);
            Object classObject = null;
            classObject = classType.getDeclaredMethod("getDefault", int.class).invoke(null, instanceParam);
            /*Constructor[] constructors = classType.getConstructors();
            for (Constructor constructor: constructors
                    ) {
                Class[] parameterTypes = constructor.getParameterTypes();
                if(parameterTypes != null) {
                    if(parameterTypes.length == 1 && parameterTypes[0] == int.class) {
                        classObject = constructor.newInstance(instanceParam);
                        break;
                    }
                }
            }*/
            Method method = classType.getDeclaredMethod(methodName);
            returnObject = method.invoke(classObject);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new ClassNotFoundException(className);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new NoSuchMethodException(methodName);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return returnObject;
    }

    public static Object getClassStaticInstance(String className,
                                                String staticMethodName,
                                                Object param,
                                                Class dataType) throws ClassNotFoundException, IllegalArgumentException, Exception {
        Object classObject = null;
        try {
            Class classReference = Class.forName(className);
            if(param == null) {
                classObject = classReference.getMethod(staticMethodName, new Class<?>[0])
                        .invoke(null, new Object[0]);
            } else {
                classObject = classReference.getMethod(staticMethodName, dataType)
                        .invoke(null, param);
            }
        }
        catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            throw  new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return classObject;
    }

    public static Object getClassContextInstance(String className, Context context) throws
            ClassNotFoundException, IllegalArgumentException, Exception {
        Object classObject = null;
        try {
            Class classReference = Class.forName(className);
            Constructor[] constructors = classReference.getConstructors();
            for (Constructor constructor: constructors
                    ) {
                Class[] parameterTypes = constructor.getParameterTypes();
                if(parameterTypes != null) {
                    if(parameterTypes.length == 1 && parameterTypes[0] == Context.class) {
                        classObject = constructor.newInstance(context);
                        break;
                    }
                }
            }
        }
        catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return classObject;
    }

    public static Object invokeMethod(Object classObject,
                                      String methodName,
                                      Object param,
                                      Class dataType) throws NoSuchMethodException, IllegalArgumentException, Exception {
        Object returnObject = null;
        try {
            Method method = null;
            if(param == null) {
                method = classObject.getClass().getDeclaredMethod(methodName);
                returnObject = method.invoke(classObject);
            } else {
                method = classObject.getClass().getMethod(methodName, dataType);
                returnObject = method.invoke(classObject, param);
            }
        }
        catch (NoSuchMethodException e) {
            throw new NoSuchMethodException(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return returnObject;
    }

    public static boolean isClassAvailable(String className) {
        try {
            Class classReference = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isMethodAvailable(Object classObject, String methodName, Class paramType) {
        Method method = null;
        try {
            if(paramType != null) {
                method = classObject.getClass().getMethod(methodName, new Class[] {paramType});
            } else {
                method = classObject.getClass().getMethod(methodName);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
        return method != null;
    }

}
