/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.fusepool.silk.plugins

import de.fuberlin.wiwiss.silk.datasource.DataSource
import java.net.URI
import de.fuberlin.wiwiss.silk.util.plugin.Plugin
import de.fuberlin.wiwiss.silk.util.sparql.{SparqlSamplePathsCollector, SparqlAggregatePathsCollector, EntityRetriever, RemoteSparqlEndpoint}
import java.util.logging.{Level, Logger}
import de.fuberlin.wiwiss.silk.entity.{SparqlRestriction, Path, EntityDescription}
import com.hp.hpl.jena.rdf.model.ModelFactory
import org.apache.clerezza.rdf.jena.facade.JenaGraph
import org.apache.clerezza.rdf.core.access.{TcManager, LockableMGraph}
import org.apache.clerezza.rdf.core._
import java.util.concurrent.locks._

/**
 * DataSource which retrieves all entities from TcManager using a 
 * specified MGraph name
 */
//TODO use sparql provided in TcManager
@Plugin(id = "fpTarget", label = "Fusepool Target Data Source", description = "DataSource which retrieves all entities from a Graph registered by name")
case class FpTargetDataSource(val pageSize: Int = 1000) extends DataSource {


  private val logger = Logger.getLogger(FpTargetDataSource.getClass.getName)

  private def registered = FpTargetDataSource.get
  private def graphUri = registered._1
  private def tcManager = registered._2
  private def zzGraph = tcManager.getMGraph(graphUri)
  private def jenaGraph = new JenaGraph(zzGraph)
  private def model = ModelFactory.createModelForGraph(jenaGraph)
  
  private def endpoint = new JenaSparqlEndpoint(model)
  
  override def retrieve(entityDesc: EntityDescription, entities: Seq[String]) = {
    zzGraph.getLock.readLock.lock()
    try {
      val entityRetriever = EntityRetriever(endpoint)
      entityRetriever.retrieve(entityDesc, entities)
    } finally {
      zzGraph.getLock.readLock.unlock()
    }
  }

  override def retrievePaths(restrictions: SparqlRestriction, depth: Int, limit: Option[Int]): Traversable[(Path, Double)] = {
    zzGraph.getLock.readLock.lock()
    try {
      SparqlAggregatePathsCollector(endpoint, restrictions, limit)
    } finally {
      zzGraph.getLock.readLock.unlock()
    }
  }


  override def toString = "fpSparqlEndpoint for "+graphUri
}

object FpTargetDataSource {// extends InheritableThreadLocal[(UriRef, TcManager)] {
  type t = (UriRef, TcManager)
  var value: t = null
  
  def set(p: t) = {
    this.value = p
  }
  
  def remove() = {
    this.value = null
  }
  
  def get = value
}