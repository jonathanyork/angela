package com.bigdlittled.angela

import org.apache.jackrabbit.oak.Oak
import org.apache.jackrabbit.oak.jcr.Jcr

class Repository(val name: String) {
  def repository = new Jcr(new Oak()).createRepository();
}
