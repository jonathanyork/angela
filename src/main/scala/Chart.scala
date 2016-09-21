package com.bigdlittled.angela

import scalaz._
import Scalaz._
import javax.jcr.Node
import javax.jcr.Property
import com.typesafe.scalalogging.LazyLogging

case class Chart(name: String, data: String, size: String)

object ChartSerializer extends LazyLogging {
  def read(node: Node): (String \/ Chart) = {
  logger.debug("Reading node: " + node.toString())
    for {
      name <- getProperty(node, "name") \/> "No name property"
      data <- getProperty(node, "data") \/> "No data property"
      size <- getProperty(node, "size") \/> "No size property"
    } yield Chart(name, data, size)
  }

  def write(node: Node, chart: Chart) = {
    logger.debug("Writing chart '" + chart.toString() + "' to node " + node.toString())
    node.setProperty("name", chart.name)
    node.setProperty("data", chart.data)
    node.setProperty("size", chart.size)
  }
  
  def getProperty(node: Node, name: String) = {
    logger.debug("Looking for '" + name + "' on node " + node.toString())
    if (node.hasProperty(name)) {
      logger.debug("Found '" + name + "' = '" + node.getProperty(name).toString() +"'")
      Some(node.getProperty(name).toString())
    } else {
      logger.debug("Didn't find '" + name + "' on node " + node.toString())
      None
    }
  }
}
