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

import de.fuberlin.wiwiss.silk.output.{Formatter, LinkWriter}
import de.fuberlin.wiwiss.silk.util.plugin.Plugin
import de.fuberlin.wiwiss.silk.entity.Link
import org.apache.clerezza.rdf.core._
import org.apache.clerezza.rdf.core.impl._

/**
 * A file writer.
 */
@Plugin(id = "fpWriter", label = "fpWriter", description = "Writes the links to a pre-registered triple collection")
case class FpWriter() extends LinkWriter {
 
  lazy val tc = FpWriter.get
  
  override def write(link: Link, predicateUri: String) {
    if (link.source != link.target) {
      if (link.source < link.target) {
        tc.add(new TripleImpl(new UriRef(link.source), new UriRef(predicateUri), new UriRef(link.target)))
      } else {
        tc.add(new TripleImpl(new UriRef(link.target), new UriRef(predicateUri), new UriRef(link.source)))
      }
    }
  }
}

object FpWriter extends InheritableThreadLocal[TripleCollection] {
}