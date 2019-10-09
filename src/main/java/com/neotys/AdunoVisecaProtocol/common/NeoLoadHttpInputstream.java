package com.neotys.AdunoVisecaProtocol.common;


import com.bsiag.scout.shared.proxy.http.SerializableMimeMessage;
import com.bsiag.scout.shared.proxy.http.StaticDate;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Array;
import java.util.HashMap;


public class NeoLoadHttpInputstream extends  ObjectInputStream  {

        private static final HashMap<String, Class> primClasses = new HashMap(8, 1.0F);

        static {
            primClasses.put("boolean", Boolean.TYPE);
            primClasses.put("byte", Byte.TYPE);
            primClasses.put("char", Character.TYPE);
            primClasses.put("short", Short.TYPE);
            primClasses.put("int", Integer.TYPE);
            primClasses.put("long", Long.TYPE);
            primClasses.put("float", Float.TYPE);
            primClasses.put("double", Double.TYPE);
            primClasses.put("void", Void.TYPE);
            primClasses.put("Z", Boolean.TYPE);
            primClasses.put("B", Byte.TYPE);
            primClasses.put("C", Character.TYPE);
            primClasses.put("S", Short.TYPE);
            primClasses.put("I", Integer.TYPE);
            primClasses.put("J", Long.TYPE);
            primClasses.put("F", Float.TYPE);
            primClasses.put("D", Double.TYPE);
            primClasses.put("V", Void.TYPE);
        }

        public NeoLoadHttpInputstream(InputStream in) throws IOException {
            super(in);
            this.enableResolveObject(true);
        }

    protected Object resolveObject(Object obj) throws IOException {
        if (obj instanceof StaticDate) {
            obj = ((StaticDate)obj).getDate();
        } else if (obj instanceof SerializableMimeMessage)
        {
            SerializableMimeMessage mess=(SerializableMimeMessage) obj;
            obj= mess.getMessage();
        }

        return obj;
    }


        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            return this.defaultResolveClass(desc.getName());
        }

        private Class<?> defaultResolveClass(String className) throws ClassNotFoundException, IOException {
            Class c = (Class)primClasses.get(className);
            if (c != null) {
                return c;
            } else {
                try {
                    int arrayDim;
                    for(arrayDim = 0; className.startsWith("["); ++arrayDim) {
                        className = className.substring(1);
                    }

                    if (className.matches("L.*;")) {
                        className = className.substring(1, className.length() - 1);
                    }

                    if (arrayDim > 0) {
                        c = this.defaultResolveClass(className);
                        int[] dimensions = new int[arrayDim];
                        c = Array.newInstance(c, dimensions).getClass();
                    } else {
                       // System.out.println(className);

                        ClassLoader classLoader = NeoLoadHttpInputstream.class.getClassLoader();
                        c = classLoader.loadClass(className);
                       // System.out.println(  c.getClass().getName());
                    }

                    return c;
                } catch (ClassNotFoundException var5) {
              //      logger.error("reading serialized object from http proxy tunnel: " + var5.getMessage(), var5);
                    throw var5;
                }
            }
        }
    }


