package com.ferigma.gradle.dbunit.tasks

import org.dbunit.ant.Compare
import org.gradle.api.tasks.Input

import com.ferigma.gradle.dbunit.tasks.source.CompareSource


class CompareTask extends AbstractDbunitTask {

   @Input
   List<CompareSource> sources

   CompareTask() {
      super()
      description = "Compare a dataset with database."
   }

   @Override
   void doTask() {

      sources.each {
         Compare compare = new Compare()
         compare.sort = it.sort
         compare.format = it.format
         compare.src = new File(it.file)
         compare.addQuery(it.queries)
         compare.addTable(it.tables)

         // Comparation execution
         compare.execute(connection)
      }
   }
}
