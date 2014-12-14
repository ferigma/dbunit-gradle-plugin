package com.ferigma.gradle.dbunit.tasks.source

import org.dbunit.ant.Query
import org.dbunit.ant.Table

class CompareSource extends Source {

   boolean sort
   List<Table> tables
   List<Query> queries
}
