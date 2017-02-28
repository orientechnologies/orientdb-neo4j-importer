package http;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.neo4jimporter.ONeo4jImporter;
import com.orientechnologies.orient.neo4jimporter.ONeo4jImporterCommandLineParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriele on 27/02/17.
 */
public class ONeo4jImporterJob  implements Runnable {

  private final ODocument cfg;
  private       ONeo4ImporterListener listener;
  public Status      status;

  public ONeo4jImporterJob(ODocument cfg, ONeo4ImporterListener listener) {
    this.cfg = cfg;
    this.listener = listener;
  }


  @Override
  public void run() {

    List<String> argsList = new ArrayList<String>();
    final String neo4jLibDir = cfg.field("neo4jlibdir");
    final String neo4jDbDir = cfg.field("neo4jdbdir");
    final String odbDir = cfg.field("odbdir");
    final String options = cfg.field("o");

    argsList.add(neo4jLibDir);
    argsList.add(neo4jDbDir);
    if(odbDir != null) {
      argsList.add(odbDir);
    }
    if(options != null) {
      argsList.add(options);
    }
    status = Status.RUNNING;

    String[] args = argsList.toArray(new String[argsList.size()]);

    try {
      final ONeo4jImporter neo4jImporter = ONeo4jImporterCommandLineParser.getNeo4jImporter(args);
      neo4jImporter.execute();
    } catch (Exception e) {
    }

    synchronized (listener) {
      status = Status.FINISHED;
      try {
        listener.wait(5000);
        listener.onEnd(this);
      } catch (InterruptedException e) {
      }
    }
  }

  public void validate() {

  }

  public enum Status {
    STARTED, RUNNING, FINISHED
  }
}
