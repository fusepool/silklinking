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

package eu.fusepool.scala.silk

import de.fuberlin.wiwiss.silk.config.LinkingConfig
import de.fuberlin.wiwiss.silk.plugins.Plugins
import java.io.File
import de.fuberlin.wiwiss.silk.config.LinkSpecification
import de.fuberlin.wiwiss.silk.plugins.jena.JenaPlugins
import eu.fusepool.silk.plugins.FusepoolPlugins
import eu.fusepool.silk.plugins.{FpSourceDataSource, FpTargetDataSource, FpWriter}
import de.fuberlin.wiwiss.silk.util.StringUtils._
import de.fuberlin.wiwiss.silk.util.CollectLogs
import java.util.logging.{Level, Logger}
import java.io.InputStream
import de.fuberlin.wiwiss.silk.GenerateLinksTask
import org.apache.clerezza.rdf.core.access.{LockableMGraph, TcManager}
import org.apache.clerezza.rdf.core.{UriRef, TripleCollection}
//import de.fuberlin.wiwiss.silk.execution.GenerateLinksTask
import de.fuberlin.wiwiss.silk.datasource.DataSource

/**
 * Executes the complete Silk workflow.
 */
object Silk {
  /**
   * The default number of threads to be used for matching.
   */
  val DefaultThreads = 8

  private val logger = Logger.getLogger(Silk.getClass.getName)

  //Print welcome message on start-up
  //println("Silk Link Discovery Framework - Version 2.5.3")

  //Register all available plugins
  Plugins.register()
  FusepoolPlugins.register()
  JenaPlugins.register()


  /**
   * Executes Silk using a specific configuration file.
   *
   * @param configFile The configuration file.
   * @param linkSpecID The link specifications to be executed. If not given, all link specifications are executed.
   * @param numThreads The number of threads to be used for matching.
   * @param reload Specifies if the entity cache is to be reloaded before executing the matching. Default: true
   */
  /*def executeFile(configFile: File, linkSpecID: String = null, numThreads: Int = DefaultThreads, reload: Boolean = true) {
    executeConfig(LinkingConfig.load(configFile), linkSpecID, numThreads, reload)
  }*/

  /**
   * Executes Silk using a specific configuration.
   *
   * @param config The configuration.
   * @param linkSpecID The link specifications to be executed. If not given, all link specifications are executed.
   * @param numThreads The number of threads to be used for matching.
   * @param reload Specifies if the entity cache is to be reloaded before executing the matching. Default: true
   */
  def executeConfig(config: LinkingConfig, linkSpecID: String = null, numThreads: Int = DefaultThreads, reload: Boolean = true) {
    if (linkSpecID != null) {
      //Execute a specific link specification
      val linkSpec = config.linkSpec(linkSpecID)
      executeLinkSpec(config, linkSpec, numThreads, reload)
    } else {
      //Execute all link specifications
      for (linkSpec <- config.linkSpecs) {
        executeLinkSpec(config, linkSpec, numThreads, reload)
      }
    }
  }

  

  
  def executeStream(configStream: InputStream, linkSpecID: String, numThreads: Int, reload: Boolean) {
    executeConfig(LinkingConfig.load(configStream), linkSpecID, numThreads, reload)
  }
  
  def executeStream(sourceGraph: TripleCollection, targetGraphName: UriRef, tcManager: TcManager,
                    output: TripleCollection,configStream: InputStream, 
                    linkSpecID: String, numThreads: Int, reload: Boolean) {
    FpWriter.set(output)
    //currently these aree not thread locals
    synchronized {
      FpSourceDataSource.set(sourceGraph)
      FpTargetDataSource.set((targetGraphName, tcManager))
      executeConfig(LinkingConfig.load(configStream), linkSpecID, numThreads, reload)
      FpSourceDataSource.remove()
      FpTargetDataSource.remove()
    }
    FpWriter.remove()
  }
  
  def executeStream(sourceGraph: TripleCollection, targetGraph: TripleCollection,
                    output: TripleCollection,configStream: InputStream, 
                    linkSpecID: String, numThreads: Int, reload: Boolean) {
    FpWriter.set(output)
    //currently these aree not thread locals
    synchronized {
      FpSourceDataSource.set(sourceGraph)
      FpTargetDataSource.set(targetGraph)
      executeConfig(LinkingConfig.load(configStream), linkSpecID, numThreads, reload)
      FpSourceDataSource.remove()
      FpTargetDataSource.remove()
    }
    FpWriter.remove()
  }
  
  
  /**
   * Executes a single link specification.
   *
   * @param config The configuration.
   * @param linkSpec The link specifications to be executed.
   * @param numThreads The number of threads to be used for matching.
   * @param reload Specifies if the entity cache is to be reloaded before executing the matching. Default: true
   */
  private def executeLinkSpec(config: LinkingConfig, linkSpec: LinkSpecification, numThreads: Int = DefaultThreads, reload: Boolean = true) {
    val links = new GenerateLinksTask(
      sources = config.sources,
      linkSpec = linkSpec,
      outputs = linkSpec.outputs ++ config.outputs,
      runtimeConfig = config.runtime.copy(numThreads = numThreads, reloadCache = reload)
    )()
    println("********* links"+links.length)
  }


}
