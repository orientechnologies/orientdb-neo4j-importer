/*
 *
 *  * Copyright 2014 Orient Technologies.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  
 */

package com.orientechnologies.orient.neo4jimporter;

/**
 * OrientDB's Neo4j Importer - Settings Class
 *
 * @author Santo Leto
 */

public class ONeo4jImporterSettings {
  public String  neo4jLibPath;
  public String  neo4jDbPath;
  public String  orientDbDir;
  public boolean overwriteOrientDbDir = false;
  public boolean createIndexOnNeo4jRelID = false;

  public String getNeo4jLibPath() {
    return neo4jLibPath;
  }

  public void setNeo4jLibPath(String neo4jLibPath) {
    this.neo4jLibPath = neo4jLibPath;
  }

  public String getNeo4jDbPath() {
    return neo4jDbPath;
  }

  public void setNeo4jDbPath(String neo4jDbPath) {
    this.neo4jDbPath = neo4jDbPath;
  }

  public String getOrientDbDir() {
    return orientDbDir;
  }

  public void setOrientDbDir(String orientDbDir) {
    this.orientDbDir = orientDbDir;
  }

  public boolean isOverwriteOrientDbDir() {
    return overwriteOrientDbDir;
  }

  public void setOverwriteOrientDbDir(boolean overwriteOrientDbDir) {
    this.overwriteOrientDbDir = overwriteOrientDbDir;
  }

  public boolean isCreateIndexOnNeo4jRelID() {
    return createIndexOnNeo4jRelID;
  }

  public void setCreateIndexOnNeo4jRelID(boolean createIndexOnNeo4jRelID) {
    this.createIndexOnNeo4jRelID = createIndexOnNeo4jRelID;
  }
}
