package com.ferigma.gradle.dbunit

import org.dbunit.operation.CompositeOperation
import org.dbunit.operation.DatabaseOperation
import org.dbunit.operation.DeleteAllOperation
import org.dbunit.operation.DeleteOperation
import org.dbunit.operation.InsertOperation
import org.dbunit.operation.RefreshOperation
import org.dbunit.operation.TruncateTableOperation
import org.dbunit.operation.UpdateOperation
import org.dbunit.operation.DatabaseOperation.DummyOperation

enum OperationType {

   NONE("none", type: DummyOperation),
   UPDATE("update", type: UpdateOperation),
   INSERT("insert", type: InsertOperation),
   REFRESH("refresh", type: RefreshOperation),
   DELETE("delete", type: DeleteOperation),
   DELETE_ALL("deleteAll", type: DeleteAllOperation),
   TRUNCATE_TABLE("truncateTable", type: TruncateTableOperation),
   CLEAN_INSERT("cleanInsert", type: CompositeOperation)

   OperationType(String name, Class<? extends DatabaseOperation> operation) {
      this.name = name
      this.operation = operation
   }
}
