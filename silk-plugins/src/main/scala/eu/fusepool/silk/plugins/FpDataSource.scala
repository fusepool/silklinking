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

import com.hp.hpl.jena.rdf.model.ModelFactory
import de.fuberlin.wiwiss.silk.datasource.DataSource
import de.fuberlin.wiwiss.silk.util.plugin.Plugin
import de.fuberlin.wiwiss.silk.entity.{EntityDescription, Path, SparqlRestriction}
import de.fuberlin.wiwiss.silk.util.sparql.{EntityRetriever, SparqlAggregatePathsCollector}
import java.io.{File, FileInputStream}
import org.apache.clerezza.rdf.jena.facade.JenaGraph
import org.apache.clerezza.rdf.core.access.LockableMGraph
import org.apache.clerezza.rdf.core._
import java.util.concurrent.locks._
//import de.fuberlin.wiwiss.silk.plugins.jena._

/**
 * DataSource which retrieves all instances fom a registered Clerezza graph.
 * The Graph has to be registered with FpSourceDataSource.set
 */
trait FpDataSource extends DataSource {
  
  protected def zzGraph : TripleCollection
  private def jenaGraph = new JenaGraph(zzGraph)
  private def model = ModelFactory.createModelForGraph(jenaGraph)

  private def endpoint = new JenaSparqlEndpoint(model)

  override def retrieve(entityDesc: EntityDescription, entities: Seq[String]) = {
    println("**************************yes!")
    val result = try {
      val lock = zzGraph match {
        case lg: LockableMGraph => lg.getLock.readLock.lock(); lg.getLock.readLock
        case _ => null   
      }
      try {
        val entityRetriever = EntityRetriever(endpoint)
        entityRetriever.retrieve(entityDesc, entities)
      } finally {
        if (lock != null) {
          lock.unlock()
        }
      }
    } catch {
      case e: Throwable => e.printStackTrace(); null;
    }
    result
  }

  override def retrievePaths(restrictions: SparqlRestriction, depth: Int, limit: Option[Int]): Traversable[(Path, Double)] = {
    println("**************************yes!!!")
    val lock = zzGraph match {
      case lg: LockableMGraph => lg.getLock.readLock.lock(); lg.getLock.readLock
      case _ => null   
    }
    try {
      SparqlAggregatePathsCollector(endpoint, restrictions, limit)
    } finally {
      if (lock != null) {
        lock.unlock()
      }
    }
  }
}
