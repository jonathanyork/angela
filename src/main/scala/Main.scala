package com.bigdlittled.angela

import scalaz._
import Scalaz._
import com.typesafe.scalalogging.LazyLogging
import javax.jcr.SimpleCredentials
import javax.jcr.Node
import org.apache.jackrabbit.oak._
import org.apache.jackrabbit.oak.jcr._
import org.apache.jackrabbit.oak.jcr.session._
import org.apache.jackrabbit.oak.plugins.document._
import scala.collection.JavaConverters._
import org.mongodb.scala._

object Main extends App with LazyLogging {
  logger.info("Creating new repository")

  val database = MongoClient("127.0.0.1:27017").getDatabase("oak")
  val nodestore = new DocumentMK.Builder().setMongoDB(database).getNodeStore()
  val repository = new Jcr(new Oak(nodestore)).createRepository()

//  val repository = new Jcr(new Oak()).createRepository();

  val session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
  val root = session.getRootNode();

  populateRepository(root)
  
  logger.info((getChart(root, "chart4") | "No chart found called 'chart4'").toString())
  logger.info((getChart(root, "chart13") | "No chart found called 'chart13'").toString())
  
  logger.info(getChartNames(root).toString())
  logger.info((getCharts(root) | "Error getting all the charts").toString())
  
  def createChart(node: Node, chart: Chart) {
    if (node.hasNode(chart.name)) {
      logger.info("Updating chart named '" + chart.name + "'")
      ChartSerializer.write(node.getNode(chart.name), chart)
    } else {
      logger.info("Creating chart named '" + chart.name + "'")
      ChartSerializer.write(node.addNode(chart.name), chart)
    }
  }

  def populateRepository(node: Node) = {
    1 to 10 foreach { i =>
      createChart(node, new Chart("chart" + i, "something", i.toString()))
    }    
    session.save()
  }

  def findChart(node: Node, name: String): (String \/ Node) = {
    if (node.hasNode(name)) {
      \/-(node.getNode(name))
    } else {
      -\/("Could not find chart named '" + name + "'")
    }    
  }

  def getChart(node: Node): (String \/ Chart) = {
    logger.info("Found node now getting: " + node.toString())
    ChartSerializer.read(node)
  }

  def getChart(node: Node, name: String): (String \/ Chart) = {
    findChart(node, name) match {
      case \/-(n: Node) => ChartSerializer.read(n)
      case -\/(s: String) => -\/(s)
    }
  }  
  
  def getCharts(node: Node): (String \/ Seq[Chart]) = {
    node.getNodes("chart*").asScala.toList.map {
      case n: Node => getChart(n)
    }.sequenceU
  }
  
  def getChartNames(node: Node): (String \/ Seq[String]) = {
    node.getNodes("chart*").asScala.toList.map {
      case n: Node => \/-(n.getName())
      case _ => -\/("Can't get name")
    } sequenceU
  }
}
