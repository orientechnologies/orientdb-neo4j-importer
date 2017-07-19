package com.orientechnologies.orient.neo4jimporter;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by frank on 08/11/2016.
 * <p>
 * Tests are documented in the README.md
 * Reminder:
 * when nodes are migrated, for each vertex class, two internal properties and two indices are created declaredProperties=2
 * when edges are migrated, for each edge class, an internal property and an index are created declaredProperties=1
 * Ideas for other tests:
 * - test with edges
 */
public class ONeo4jImporterTest {

  String dbVersion = "3.2"; // used in the path

  @Test
  public void shouldImportEmptyDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_empty_db";
    settings.orientDbDir = "target/migrated_databases/graphdb_empty_db";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_empty_db");
    db.open("admin", "admin");

    //no vertices classes, no edge classes, 0 vertices, 0 edges
    assertEquals(0, db.getMetadata().getSchema().getClass("V").getSubclasses().size());
    assertEquals(0, db.getMetadata().getSchema().getClass("E").getSubclasses().size());

    assertEquals(0, db.getMetadata().getSchema().getClass("V").count());
    assertEquals(0, db.getMetadata().getSchema().getClass("E").count());
    //

    db.close();

  }

  @Test
  public void shouldImportUniqueConstraintsOnlyDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_unique_constraints_only";
    settings.orientDbDir = "target/migrated_databases/graphdb_unique_constraints_only";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_unique_constraints_only");
    db.open("admin", "admin");

    //0 vertices, 0 edges, class NodeLabelA, class NodeLabelB, class NodeLabelC, 3 properties, 3 unique indexes
    assertEquals(0, db.getMetadata().getSchema().getClass("V").count());
    assertEquals(0, db.getMetadata().getSchema().getClass("E").count());

    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelA")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelB")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelC")).isNotNull();

    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelA").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelB").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelC").declaredProperties().size());

    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelA").existsProperty("p_number"));
    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelB").existsProperty("p_string"));
    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelC").existsProperty("p_boolean"));

    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelA").areIndexed("p_number"));
    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelB").areIndexed("p_string"));
    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelC").areIndexed("p_boolean"));
    //

    db.close();

  }

  @Test
  public void shouldImportNodesOnlyDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_nodes_only";
    settings.orientDbDir = "target/migrated_databases/graphdb_nodes_only";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_nodes_only");
    db.open("admin", "admin");

    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelA")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelB")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelC")).isNotNull();

    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelA").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelB").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelC").declaredProperties().size());

    assertEquals(10, db.getMetadata().getSchema().getClass("NodeLabelA").count());
    assertEquals(10, db.getMetadata().getSchema().getClass("NodeLabelB").count());
    assertEquals(10, db.getMetadata().getSchema().getClass("NodeLabelC").count());

    db.close();

  }

  @Test
  public void shouldImportNodesOnlyNoLabelsDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_nodes_only_no_labels";
    settings.orientDbDir = "target/migrated_databases/graphdb_nodes_only_no_labels";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_nodes_only_no_labels");
    db.open("admin", "admin");

    assertEquals(1, db.getMetadata().getSchema().getClass("V").getSubclasses().size());
    assertEquals(0, db.getMetadata().getSchema().getClass("E").getSubclasses().size());

    Assertions.assertThat(db.getMetadata().getSchema().getClass("GenericClassNeo4jConversion")).isNotNull();
    assertEquals(2, db.getMetadata().getSchema().getClass("GenericClassNeo4jConversion").declaredProperties().size());

    assertEquals(30, db.getMetadata().getSchema().getClass("V").count());

    db.close();

  }

  @Test
  public void shouldImportNodesOnlyMixedLabelsNoLabelsDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_nodes_only_mixed_labels_and_no_labels";
    settings.orientDbDir = "target/migrated_databases/graphdb_nodes_only_mixed_labels_and_no_labels";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx(
        "plocal:target/migrated_databases/graphdb_nodes_only_mixed_labels_and_no_labels");
    db.open("admin", "admin");

    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelA")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelC")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("GenericClassNeo4jConversion")).isNotNull();

    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelA").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelC").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("GenericClassNeo4jConversion").declaredProperties().size());

    db.close();

  }

  @Test
  public void shouldImportNodesOnlyLabelCaseDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_nodes_only_label_case_test";
    settings.orientDbDir = "target/migrated_databases/graphdb_nodes_only_label_case_test";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_nodes_only_label_case_test");
    db.open("admin", "admin");

    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelA")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelC")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("GenericClassNeo4jConversion")).isNotNull();

    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelA").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelC").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("GenericClassNeo4jConversion").declaredProperties().size());

    db.close();

  }

  @Test
  public void shouldImportNodesOnlyLabelCaseConstraintsDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_nodes_only_label_case_test_constraints";
    settings.orientDbDir = "target/migrated_databases/graphdb_nodes_only_label_case_test_constraints";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx(
        "plocal:target/migrated_databases/graphdb_nodes_only_label_case_test_constraints");
    db.open("admin", "admin");

    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelA")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelB")).isNotNull();

    assertEquals(3, db.getMetadata().getSchema().getClass("NodeLabelA").declaredProperties().size());
    assertEquals(3, db.getMetadata().getSchema().getClass("NodeLabelB").declaredProperties().size());

    assertEquals(20, db.getMetadata().getSchema().getClass("NodeLabelA").count());
    assertEquals(20, db.getMetadata().getSchema().getClass("NodeLabelB").count());

    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelA").existsProperty("p_number"));
    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelB").existsProperty("p_number"));

    db.close();

  }

  @Test
  public void shouldImportNodesOnlyMultipleLabelsDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_nodes_only_multiple_labels";
    settings.orientDbDir = "target/migrated_databases/graphdb_nodes_only_multiple_labels";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_nodes_only_multiple_labels");
    db.open("admin", "admin");

    Assertions.assertThat(db.getMetadata().getSchema().getClass("MultipleLabelNeo4jConversion")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelA")).isNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelB")).isNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelC")).isNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelD")).isNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelE")).isNotNull();

    assertEquals(2, db.getMetadata().getSchema().getClass("MultipleLabelNeo4jConversion").declaredProperties().size());
    assertEquals(2, db.getMetadata().getSchema().getClass("NodeLabelE").declaredProperties().size());

    assertEquals(10, db.getMetadata().getSchema().getClass("NodeLabelE").count());
    assertEquals(20, db.getMetadata().getSchema().getClass("MultipleLabelNeo4jConversion").count());

    db.close();

  }

  @Ignore
  @Test
  public void shouldImportMultipleLabelsAndConstraintsDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_multiple_labels_and_constraints";
    settings.orientDbDir = "target/migrated_databases/graphdb_multiple_labels_and_constraints";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_multiple_labels_and_constraints");
    db.open("admin", "admin");

    Assertions.assertThat(db.getMetadata().getSchema().getClass("MultipleLabelNeo4jConversion")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelA")).isNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelB")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelC")).isNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelD")).isNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("NodeLabelE")).isNotNull();

    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelB").existsProperty("p_number"));
    assertEquals(true, db.getMetadata().getSchema().getClass("NodeLabelE").existsProperty("other_property"));
    assertEquals(true, db.getMetadata().getSchema().getClass("MultipleLabelNeo4jConversion").existsProperty("p_number"));
    assertEquals(true, db.getMetadata().getSchema().getClass("MultipleLabelNeo4jConversion").existsProperty("p_string"));

    assertEquals(4, db.getMetadata().getSchema().getClass("MultipleLabelNeo4jConversion").declaredProperties().size());
    assertEquals(3, db.getMetadata().getSchema().getClass("NodeLabelB").declaredProperties().size());
    assertEquals(3, db.getMetadata().getSchema().getClass("NodeLabelE").declaredProperties().size());

    assertEquals(10, db.getMetadata().getSchema().getClass("NodeLabelB").count());
    assertEquals(10, db.getMetadata().getSchema().getClass("NodeLabelE").count());
    assertEquals(20, db.getMetadata().getSchema().getClass("MultipleLabelNeo4jConversion").count());

    db.close();

  }

  @Test
  public void shouldImportMovieDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_movie";
    settings.orientDbDir = "target/migrated_databases/graphdb_movie";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_movie");
    db.open("admin", "admin");

    Assertions.assertThat(db.getMetadata().getSchema().getClass("Movie")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("Person")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("ACTED_IN")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("DIRECTED")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("FOLLOWS")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("PRODUCED")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("REVIEWED")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("WROTE")).isNotNull();

    assertEquals(171, db.getMetadata().getSchema().getClass("V").count());
    assertEquals(253, db.getMetadata().getSchema().getClass("E").count());

    assertEquals(38, db.getMetadata().getSchema().getClass("Movie").count());
    assertEquals(133, db.getMetadata().getSchema().getClass("Person").count());
    assertEquals(172, db.getMetadata().getSchema().getClass("ACTED_IN").count());
    assertEquals(44, db.getMetadata().getSchema().getClass("DIRECTED").count());
    assertEquals(3, db.getMetadata().getSchema().getClass("FOLLOWS").count());
    assertEquals(15, db.getMetadata().getSchema().getClass("PRODUCED").count());
    assertEquals(9, db.getMetadata().getSchema().getClass("REVIEWED").count());
    assertEquals(10, db.getMetadata().getSchema().getClass("WROTE").count());

    db.close();

  }

  @Test
  public void shouldImportNorthwindDb() throws Exception {

    ONeo4jImporterSettings settings = new ONeo4jImporterSettings();

    settings.neo4jDbPath = "./neo4jdbs/databases/" + dbVersion + "/graphdb_northwind";
    settings.orientDbDir = "target/migrated_databases/graphdb_northwind";
    settings.overwriteOrientDbDir = true;
    settings.createIndexOnNeo4jRelID = true;

    ONeo4jImporter importer = new ONeo4jImporter(settings);

    importer.execute();

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:target/migrated_databases/graphdb_northwind");
    db.open("admin", "admin");

    Assertions.assertThat(db.getMetadata().getSchema().getClass("Category")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("Customer")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("Order")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("Product")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("Supplier")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("ORDERS")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("PART_OF")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("PURCHASED")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("SUPPLIES")).isNotNull();

    assertEquals(1035, db.getMetadata().getSchema().getClass("V").count());
    assertEquals(3139, db.getMetadata().getSchema().getClass("E").count());

    assertEquals(8, db.getMetadata().getSchema().getClass("Category").count());
    assertEquals(91, db.getMetadata().getSchema().getClass("Customer").count());
    assertEquals(830, db.getMetadata().getSchema().getClass("Order").count());
    assertEquals(77, db.getMetadata().getSchema().getClass("Product").count());
    assertEquals(29, db.getMetadata().getSchema().getClass("Supplier").count());
    assertEquals(2155, db.getMetadata().getSchema().getClass("ORDERS").count());
    assertEquals(77, db.getMetadata().getSchema().getClass("PART_OF").count());
    assertEquals(830, db.getMetadata().getSchema().getClass("PURCHASED").count());
    assertEquals(77, db.getMetadata().getSchema().getClass("SUPPLIES").count());

    assertEquals(true, db.getMetadata().getSchema().getClass("Category").existsProperty("categoryID"));
    assertEquals(3, db.getMetadata().getSchema().getClass("Category").declaredProperties().size());
    assertEquals(true, db.getMetadata().getSchema().getClass("Category").areIndexed("categoryID"));

    assertEquals(true, db.getMetadata().getSchema().getClass("Customer").existsProperty("customerID"));
    assertEquals(3, db.getMetadata().getSchema().getClass("Customer").declaredProperties().size());
    assertEquals(true, db.getMetadata().getSchema().getClass("Customer").areIndexed("customerID"));

    assertEquals(true, db.getMetadata().getSchema().getClass("Order").existsProperty("orderID"));
    assertEquals(3, db.getMetadata().getSchema().getClass("Order").declaredProperties().size());
    assertEquals(true, db.getMetadata().getSchema().getClass("Order").areIndexed("orderID"));

    assertEquals(true, db.getMetadata().getSchema().getClass("Product").existsProperty("productID"));
    assertEquals(3, db.getMetadata().getSchema().getClass("Product").declaredProperties().size());
    assertEquals(true, db.getMetadata().getSchema().getClass("Product").areIndexed("productID"));

    db.close();

  }

}
