package com.ferigma.gradle.dbunit.tasks.source

import org.dbunit.ant.Query
import org.dbunit.ant.Table

class ExportSource extends Source {

   String destination
   String doctype
   String encoding
   boolean ordered
   List<Table> tables
   List<Query> queries
}
