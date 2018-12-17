package javas;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class plugin {

    private static final Class[] parameters = new Class[]{URL.class};

    public static void addtoClassPath(String s) throws Exception {
        File f = new File(s);
        URL u = f.toURI().toURL();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        URL urls[] = urlClassLoader.getURLs();
        for (int i = 0; i < urls.length; i++) {
            if (Objects.equals(urls[i].toString(),u.toString())) {
                return;
            }
        }

        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL",parameters);
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[] { u });
        //Class loadedClass = urlClassLoader.loadClass("plugin.core");
    }

    public static void main(String args[])
    {
        System.out.println("Main function call");

        String s = "C:\\jars\\plugin.jar";

        try {
            addtoClassPath(s);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }

        System.out.println("End Main function call");
    }
}
