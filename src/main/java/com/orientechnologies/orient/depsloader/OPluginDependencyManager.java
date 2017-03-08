package com.orientechnologies.orient.depsloader;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by gabriele on 01/03/17.
 */
public class OPluginDependencyManager {

  public static void setNewChildClassLoaderFromJarDir(String jarDir) throws Exception {

    ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
    ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

    // Add the conf dir to the classpath. Chain the current thread classloader
    URL[] urls = getJarDependenciesUrls(jarDir);
    URLClassLoader urlClassLoader = new OChildFirstURLClassLoader(urls, currentThreadClassLoader);

    // Replace the thread classloader - assumes you have permissions to do so
    Thread.currentThread().setContextClassLoader(urlClassLoader);

    ClassLoader currentThreadClassLoader2 = Thread.currentThread().getContextClassLoader();
    ClassLoader sysClassLoader2 = ClassLoader.getSystemClassLoader();

    Field scl = ClassLoader.class.getDeclaredField("scl"); // Get system class loader
    scl.setAccessible(true); // Set accessible
    scl.set(null, urlClassLoader);

    ClassLoader sysClassLoader3 = ClassLoader.getSystemClassLoader();
    System.out.println();

  }

  public static void setNewChildClassLoaderFromJarDir() throws Exception {

    ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

    ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

    // Add the conf dir to the classpath. Chain the current thread classloader
    URL[] urls = new URL[0];
    URLClassLoader urlClassLoader = new OChildFirstURLClassLoader(urls, currentThreadClassLoader);

    ClassLoader currentThreadClassLoader2 = Thread.currentThread().getContextClassLoader();

    // Replace the thread classloader - assumes you have permissions to do so
    Thread.currentThread().setContextClassLoader(urlClassLoader);

    ClassLoader currentThreadClassLoader3 = Thread.currentThread().getContextClassLoader();
    ClassLoader sysClassLoader2 = ClassLoader.getSystemClassLoader();

    Field scl = ClassLoader.class.getDeclaredField("scl"); // Get system class loader
    scl.setAccessible(true); // Set accessible
    scl.set(null, urlClassLoader);

    ClassLoader sysClassLoader3 = ClassLoader.getSystemClassLoader();
    System.out.println();

  }

  public static void addUrlsToCurrentThreadClassLoader(String jarDir) {

    ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
    if(!(currentThreadClassLoader instanceof OChildFirstURLClassLoader)) {
      System.out.println("No urls added to the current class loader because it's not an instance of OChildFirstURLClassLoader.");
      return;
    }
    OChildFirstURLClassLoader classLoader = (OChildFirstURLClassLoader) currentThreadClassLoader;
    URL[] urls = getJarDependenciesUrls(jarDir);

    for(int i=0; i<urls.length; i++) {
      classLoader.addURL(urls[i]);
    }

  }

  private static URL[] getJarDependenciesUrls(String jarDir) {

    // search for JAR files in the given directory
    FileFilter jarFilter = new FileFilter() {
      public boolean accept(File pathname) {
        return pathname.getName().endsWith(".jar");
      }
    };

    // create URL for each JAR file found
    File[] jarFiles = new File(jarDir).listFiles(jarFilter);
    URL[] urls;

    if (jarFiles != null) {
      urls = new URL[jarFiles.length];

      for (int i = 0; i < jarFiles.length; i++) {
        try {
//          URL u = new URL("jar","","file:" + jarFiles[i].getAbsolutePath() + "!/");
          URL u = new URL("jar:file:" + jarFiles[i].getAbsolutePath() + "!/");
          urls[i] = u;
        } catch (MalformedURLException e) {
          throw new RuntimeException("Could not get URL for JAR file: " + jarFiles[i], e);
        }
      }

    } else {
      // no JAR files found
      urls = new URL[0];
    }

    return urls;
  }

}
