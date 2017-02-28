package com.orientechnologies.orient.neo4jimporter;

import com.orientechnologies.orient.core.OConstants;
import com.orientechnologies.orient.core.exception.OConfigurationException;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.config.OServerParameterConfiguration;
import com.orientechnologies.orient.server.network.OServerNetworkListener;
import com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpAbstract;
import com.orientechnologies.orient.server.plugin.OServerPluginAbstract;
import http.OServerCommandNeo4jImporter;

import static com.orientechnologies.orient.neo4jimporter.ONeo4jImporter.PROGRAM_NAME;

/**
 * Created by frank on 13/11/2016.
 */
public class ONeo4jImporterMain extends OServerPluginAbstract {

  private OServer server;

  public static void main(String[] args) {

    System.out.println();
    System.out.println(String.format(PROGRAM_NAME + " v.%s - %s", OConstants.getVersion(), OConstants.COPYRIGHT));
    System.out.println();

    //parses the command line parameters, and starts the import (.execute). Then exits
    int returnValue = 1;
    try {

      final ONeo4jImporter neo4jImporter = ONeo4jImporterCommandLineParser.getNeo4jImporter(args);

      returnValue = neo4jImporter.execute();

    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }

    System.exit(returnValue);

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
