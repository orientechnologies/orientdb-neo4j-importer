package com.orientechnologies.orient.depsloader;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by gabriele on 01/03/17.
 */
public class OPluginDependencyManager {

  public static void setNewChildClassLoaderFromJarDir(String jarDir) {

    ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

    // Add the conf dir to the classpath. Chain the current thread classloader
    URL[] urls = getJarDependenciesUrls(jarDir);
    URLClassLoader urlClassLoader = new OChildFirstURLClassLoader(urls, currentThreadClassLoader);

    // Replace the thread classloader - assumes you have permissions to do so
    Thread.currentThread().setContextClassLoader(urlClassLoader);

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
          URL u = new URL("jar:file:" + jarFiles[i] + "!/");
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
