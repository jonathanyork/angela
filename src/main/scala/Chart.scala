package com.bigdlittled.angela

import javax.jcr._

case class Chart(name: String, data: String, size: Long)

object ChartSerializer {
  def read(node: Node): Chart = {
    var name = node.getProperty("name").getString();
    var data = node.getProperty("data").getString();
    var size = node.getProperty("size").getLong();
    new Chart(name, data, size)
  }
  def write(node: Node, chart: Chart) = {
     node.setProperty("name", chart.name)
     node.setProperty("data", chart.data)
     node.setProperty("size", chart.size)
  }
}
