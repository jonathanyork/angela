package com.bigdlittled.angela

import scalaz._
import Scalaz._
import javax.jcr.Node
import javax.jcr.Property

case class Chart(name: String, data: String, size: String)

object ChartSerializer {
  def read(node: Node): (String \/ Chart) = {
    for {
      name <- getProperty(node, "name") \/> "No name property"
      data <- getProperty(node, "data") \/> "No data property"
      size <- getProperty(node, "size") \/> "No size property"
    } yield Chart(name, data, size)
  }

  def write(node: Node, chart: Chart) = {
     node.setProperty("name", chart.name)
     node.setProperty("data", chart.data)
     node.setProperty("size", chart.size)
  }
  
  def getProperty(node: Node, name: String) = {
    if (node.hasProperty(name)) {
      Some(node.getProperty(name).toString())
    } else {
      None
    }
  }
}
