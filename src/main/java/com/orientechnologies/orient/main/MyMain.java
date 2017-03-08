package com.orientechnologies.orient.main;

import com.orientechnologies.orient.depsloader.OPluginDependencyManager;
import com.orientechnologies.orient.neo4jimporter.ONeo4jImporter;
import com.orientechnologies.orient.neo4jimporter.ONeo4jImporterCommandLineParser;
import com.orientechnologies.orient.neo4jimporter.ONeo4jImporterSettings;

/**
 * Created by gabriele on 06/03/17.
 */
public class MyMain {


  public static void main(String[] args) throws Exception {

    String[] arguments = new String[6];

    arguments[0] = "-neo4jlibdir";
    arguments[1] = "/Users/gabriele/neo4j-community-3.1.1/lib";
    arguments[2] = "-neo4jdbdir";
    arguments[3] = "/Users/gabriele/neo4j-community-3.1.1/data/databases/graph.db";
    arguments[4] = "-odbdir";
    arguments[5] = "/Users/gabriele/orientdb-community-2.2.18-SNAPSHOT/databases/neo4jImport";

    Thread.sleep(7000);

    // defining child class loader to load neo4j dependencies
    OPluginDependencyManager.setNewChildClassLoaderFromJarDir(arguments[1]);

    final ONeo4jImporter neo4jImporter = ONeo4jImporterCommandLineParser.getNeo4jImporter(arguments);

    ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
    ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

    try {
      neo4jImporter.execute();
    } catch(Exception e) {
      System.out.println("Exception message: " + e.getMessage());
      System.out.println("Stacktrace:\n" + e.getStackTrace());
    }
  }
}
