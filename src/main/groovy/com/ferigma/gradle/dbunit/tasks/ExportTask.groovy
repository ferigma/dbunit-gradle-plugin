package com.ferigma.gradle.dbunit.tasks

import org.dbunit.ant.Export
import org.gradle.api.tasks.Input

import com.ferigma.gradle.dbunit.tasks.source.ExportSource


class ExportTask extends AbstractDbunitTask {

   @Input
   List<ExportSource> sources

   ExportTask() {
      super()
      description = "Export database tables into a dataset file."
   }

   @Override
   void doTask() {

      sources.each {
         Export export = new Export()
         export.ordered = it.ordered
         export.dest = new File(it.destination)
         export.doctype = it.doctype
         export.format = it.format
         export.encoding = it.encoding
         export.addQuery(it.queries)
         export.addTable(it.tables)

         // Export execution
         export.execute(connection)
      }
   }
}
