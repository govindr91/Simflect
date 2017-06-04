package com.codetagging.simflect.util

import android.content.Context
import java.lang.reflect.Method

object TypeReflection {

    @Throws(ClassNotFoundException::class, NoSuchMethodException::class, Exception::class)
    operator fun get(className: String, methodName: String, param: Any, classDataType: Class<*>, context: Context): Any {
        var returnObject: Any? = null
        try {
            val classType = Class.forName(className)
            var classObject: Any? = null
            val constructors = classType.constructors
            for (constructor in constructors) {
                val parameterTypes = constructor.parameterTypes
                if (parameterTypes != null) {
                    if (parameterTypes.size == 1 && parameterTypes[0] == Context::class.java) {
                        classObject = constructor.newInstance(context)
                        break
                    }
                }
            }
            val method = classType.getDeclaredMethod(methodName, classDataType)
            returnObject = method.invoke(classObject, param)
        } catch (classNotFoundException: ClassNotFoundException) {
            throw ClassNotFoundException(className)
        } catch (noSuchMethodException: NoSuchMethodException) {
            throw NoSuchMethodException(methodName)
        } catch (e: Exception) {
            throw Exception(e.message)
        }

        return returnObject
    }

    @Throws(ClassNotFoundException::class, NoSuchMethodException::class, Exception::class)
    fun getDefault(className: String, methodName: String, instanceParam: Int): Any {
        var returnObject: Any? = null
        try {
            val classType = Class.forName(className)
            var classObject: Any? = null
            classObject = classType.getDeclaredMethod("getDefault", Int::class.javaPrimitiveType).invoke(null, instanceParam)
            val method = classType.getDeclaredMethod(methodName)
            returnObject = method.invoke(classObject)
        } catch (classNotFoundException: ClassNotFoundException) {
            throw ClassNotFoundException(className)
        } catch (noSuchMethodException: NoSuchMethodException) {
            throw NoSuchMethodException(methodName)
        } catch (e: Exception) {
            throw Exception(e.message)
        }

        return returnObject
    }

    @Throws(ClassNotFoundException::class, IllegalArgumentException::class, Exception::class)
    fun getClassStaticInstance(className: String?,
                               staticMethodName: String?,
                               param: Any?,
                               dataType: Class<*>?): Any {
        var classObject: Any? = null
        try {
            val classReference = Class.forName(className)
            if (param == null) {
                classObject = classReference.getMethod(staticMethodName, *arrayOfNulls<Class<*>>(0))
                        .invoke(null, *arrayOfNulls<Any>(0))
            } else {
                classObject = classReference.getMethod(staticMethodName, dataType)
                        .invoke(null, param)
            }
        } catch (e: ClassNotFoundException) {
            throw ClassNotFoundException(e.message)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(e.message)
        } catch (e: Exception) {
            throw Exception(e.message)
        }

        return classObject
    }

    @Throws(ClassNotFoundException::class, IllegalArgumentException::class, Exception::class)
    fun getClassContextInstance(className: String, context: Context): Any {
        var classObject: Any? = null
        try {
            val classReference = Class.forName(className)
            val constructors = classReference.constructors
            for (constructor in constructors) {
                val parameterTypes = constructor.parameterTypes
                if (parameterTypes != null) {
                    if (parameterTypes.size == 1 && parameterTypes[0] == Context::class.java) {
                        classObject = constructor.newInstance(context)
                        break
                    }
                }
            }
        } catch (e: ClassNotFoundException) {
            throw ClassNotFoundException(e.message)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(e.message)
        } catch (e: Exception) {
            throw Exception(e.message)
        }

        return classObject!!
    }

    @Throws(NoSuchMethodException::class, IllegalArgumentException::class, Exception::class)
    fun invokeMethod(classObject: Any?,
                     methodName: String?,
                     param: Any?,
                     dataType: Class<*>?): Any {
        var returnObject: Any? = null
        try {
            var method: Method? = null
            if (param == null) {
                method = classObject?.javaClass?.getDeclaredMethod(methodName)
                returnObject = method!!.invoke(classObject)
            } else {
                method = classObject?.javaClass?.getMethod(methodName, dataType)
                returnObject = method!!.invoke(classObject, param)
            }
        } catch (e: NoSuchMethodException) {
            throw NoSuchMethodException(e.message)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(e.message)
        } catch (e: Exception) {
            throw Exception(e.message)
        }

        return returnObject
    }

    fun isClassAvailable(className: String?): Boolean {
        try {
            val classReference = Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun isMethodAvailable(classObject: Any?, methodName: String?, paramType: Class<*>?): Boolean {
        var method: Method? = null
        try {
            if (paramType != null) {
                method = classObject?.javaClass?.getMethod(methodName, *arrayOf(paramType))
            } else {
                method = classObject?.javaClass?.getMethod(methodName)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            return false
        }

        return method != null
    }

}
