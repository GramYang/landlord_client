package com.gram.landlord_client.sdk.socket.util;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SPIUtils {

    public static <E> E load(Class<E> clz) {
        if (clz == null) {
            return null;
        }
        ServiceLoader<E> serviceLoader = ServiceLoader.load(clz, clz.getClassLoader());
        Iterator<E> it = serviceLoader.iterator();
        try {
            if (it.hasNext()) {
                return it.next();
            }
        } catch (Exception e) {
        }
        return null;
    }
}
