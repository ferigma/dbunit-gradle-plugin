package com.ferigma.gradle.dbunit.tasks

import org.dbunit.ant.Operation
import org.gradle.api.tasks.Input

import com.ferigma.gradle.dbunit.tasks.source.OperationSource

class OperationTask extends AbstractDbunitTask {

   @Input
   List<OperationSource> sources

   OperationTask() {
      super()
      description = "Execute a database operation using an external dataset file."
   }

   @Override
   void doTask() {

      sources.each {
         Operation operation = new Operation()
         operation.transaction = it.transaction
         operation.type = it.type
         operation.format = it.format
         operation.src = new File(it.file)

         // Operation execution
         operation.execute(connection)
      }
   }
}
