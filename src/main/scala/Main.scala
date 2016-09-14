package com.bigdlittled.angela

import scalaz._
import Scalaz._
import com.typesafe.scalalogging._
import javax.jcr.SimpleCredentials
import javax.jcr.Node
import org.apache.jackrabbit.oak._
import org.apache.jackrabbit.oak.jcr._
import org.apache.jackrabbit.oak.jcr.session._

object Main extends App with LazyLogging {
  logger.info("Creating new repository")

  val repository = new Jcr(new Oak()).createRepository();

  val session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
  val root = session.getRootNode();

  populateRepository(root)
  populateRepository(root)
  
  logger.info((getChart(root, "chart4") | "No chart found called 'chart4'").toString())
  logger.info((getChart(root, "chart13") | "No chart found called 'chart13'").toString())
  
  def createChart(node: Node, chart: Chart) {
    if (node.hasNode(chart.name)) {
      logger.info("Updating chart named '" + chart.name + "'")
      val existing = node.getNode(chart.name)
      ChartSerializer.write(existing, chart)
    } else {
      logger.info("Creating chart named '" + chart.name + "'")
      node.addNode(chart.name)
    }
  }

  def populateRepository(root: Node) = {
    1 to 10 foreach { i =>
      createChart(root, new Chart("chart" + i, "something", 12))
    }    
    session.save()
  }
  
  def getChart(node: Node, name: String): (String \/ Chart) = {
    if (node.hasNode(name)) {
      \/-(ChartSerializer.read(node.getNode(name)))
    } else {
      -\/("Could not find chart named '" + name + "'")
    }
  }
}
