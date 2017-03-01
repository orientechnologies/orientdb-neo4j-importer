package com.orientechnologies.orient.neo4jimporter;

import com.orientechnologies.orient.core.exception.OConfigurationException;
import com.orientechnologies.orient.depsloader.OPluginDependencyManager;
import com.orientechnologies.orient.http.OServerCommandNeo4jImporter;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.config.OServerParameterConfiguration;
import com.orientechnologies.orient.server.network.OServerNetworkListener;
import com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpAbstract;
import com.orientechnologies.orient.server.plugin.OServerPluginAbstract;

/**
 * Created by gabriele on 01/03/17.
 */
public class ONeo4jImporterPlugin extends OServerPluginAbstract {

  private OServer server;

  public ONeo4jImporterPlugin() {}


  public int executeJob(String[] args) throws Exception {

    final ONeo4jImporter neo4jImporter = ONeo4jImporterCommandLineParser.getNeo4jImporter(args);

    ONeo4jImporterSettings settings = neo4jImporter.getSettings();
    String neo4jLibPath = settings.neo4jLibPath;

    // defining child class loader to load neo4j dependencies
    OPluginDependencyManager.setNewChildClassLoaderFromJarDir(neo4jLibPath);

    return neo4jImporter.execute();
  }

  @Override
  public String getName() {
    return "neo4j-importer";
  }

  @Override
  public void startup() {

    final OServerNetworkListener listener = server.getListenerByProtocol(ONetworkProtocolHttpAbstract.class);
    if (listener == null)
      throw new OConfigurationException("HTTP listener not found");

    listener.registerStatelessCommand(new OServerCommandNeo4jImporter());

  }

  @Override
  public void config(OServer oServer, OServerParameterConfiguration[] iParams) {
    server = oServer;
  }

  @Override
  public void shutdown() {
    super.shutdown();
  }
}
