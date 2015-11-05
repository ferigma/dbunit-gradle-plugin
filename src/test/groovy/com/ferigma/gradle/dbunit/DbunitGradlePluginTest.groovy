package com.ferigma.gradle.dbunit

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.h2.tools.DeleteDbFiles
import org.junit.BeforeClass
import org.junit.Test

import com.ferigma.gradle.dbunit.tasks.CompareTask
import com.ferigma.gradle.dbunit.tasks.ExportTask
import com.ferigma.gradle.dbunit.tasks.OperationTask
import com.ferigma.gradle.dbunit.tasks.source.CompareSource
import com.ferigma.gradle.dbunit.tasks.source.ExportSource
import com.ferigma.gradle.dbunit.tasks.source.OperationSource

class DbunitGradlePluginTest {

   static final DB_USERNAME = "sa"
   static final DB_PASSWORD = "sa"
   static final DB_PATH = "/tmp/test_db"
   static final DB_NAME = "dbunit_test_db"
   static final DB_URL = "jdbc:h2:$DB_PATH/$DB_NAME"
   static final DB_DRIVER = "org.h2.Driver"
   static final DATA_TYPE_FACTORY = "org.dbunit.ext.h2.H2DataTypeFactory"

   static Project project
   static Task operationTask
   static Task compareTask
   static Task exportTask

   @BeforeClass
   static void init() {

      // Delete Test DB
      DeleteDbFiles.execute(DB_PATH, DB_NAME, true)

      // Create Test DB
      Class.forName(DB_DRIVER)
      Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME,
               DB_PASSWORD)
      Statement stat = conn.createStatement()

      stat.execute("""create table course(
         id int primary key,
         name varchar(255),
         description varchar(255))""")
      stat.execute("""create table student(
         id int primary key,
         name varchar(255),
         born_date date)""")
      stat.execute("""create table course_student(
         course_id int not null,
         student_id int not null,
         primary key (course_id, student_id),
         foreign key (course_id) references course(id),
         foreign key (student_id) references student(id))""")
      stat.close()
      conn.close()

      // Apply plugin
      project = ProjectBuilder.builder().build()
      project.apply plugin: 'dbunit'

      // Operation Task (CLEAN_INSERT)
      operationTask = project.tasks.operation {
         username = DB_USERNAME
         password = DB_PASSWORD
         url = DB_URL
         driver = DB_DRIVER
         dataTypeFactoryName = DATA_TYPE_FACTORY
         sources = [
            new OperationSource(transaction: true, type: "CLEAN_INSERT",
            format: "xml", file: "build/resources/test/sample-data.xml")
         ]
      }

      // CompareTask
      compareTask = project.tasks.compare {
         username = DB_USERNAME
         password = DB_PASSWORD
         url = DB_URL
         driver = DB_DRIVER
         dataTypeFactoryName = DATA_TYPE_FACTORY
         sources = [
            new CompareSource(format: "xml",
            file: "build/resources/test/sample-data.xml")
         ]
      }

      // ExportTask
      exportTask = project.tasks.export {
         username = DB_USERNAME
         password = DB_PASSWORD
         url = DB_URL
         driver = DB_DRIVER
         dataTypeFactoryName = DATA_TYPE_FACTORY
         sources = [
            new ExportSource(format: "xml",
            destination: "build/test-results/result-data.xml")
         ]
      }
   }

   @Test
   void testExistTasks() {
      assert project.tasks.compare instanceof CompareTask
      assert project.tasks.export instanceof ExportTask
      assert project.tasks.operation instanceof OperationTask
   }

   @Test
   void testCleanInsertOperationTask() {
      operationTask.execute()
   }

   @Test
   void testCompareTask() {
      operationTask.execute()
      compareTask.execute()
   }

   @Test
   void testExportTask() {
      operationTask.execute()
      exportTask.execute()
   }

   @Test
   void pluginAddsExtension() {
      assert project.extensions.findByName('dbunit')
   }
}
