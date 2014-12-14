package com.ferigma.gradle.dbunit.tasks

import java.sql.Connection
import java.sql.Driver

import org.dbunit.database.DatabaseConfig
import org.dbunit.database.DatabaseConnection
import org.dbunit.database.ForwardOnlyResultSetTableFactory
import org.dbunit.database.IDatabaseConnection
import org.dbunit.database.IMetadataHandler
import org.dbunit.dataset.datatype.IDataTypeFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import com.ferigma.gradle.dbunit.DbunitGradlePlugin

/**
 * Base class for all DBUnit plugin tasks.
 *
 * @author Fernando Iglesias Martinez
 */
abstract class AbstractDbunitTask extends DefaultTask {

   static final String DEFAULT_DATA_TYPE_FACTORY_NAME =
   "org.dbunit.dataset.datatype.DefaultDataTypeFactory"
   static final String DEFAULT_METADA_HANDLER_NAME =
   "org.dbunit.database.DefaultMetadataHandler"

   static final String PROPERTY_USER = "user"
   static final String PROPERTY_PASSWORD = "password"

   /** The driver. */
   @Input
   String driver

   /** The username. */
   @Input
   String username

   /** The password. */
   @Input
   String password

   /** The url. */
   @Input
   String url

   /** The schema name that tables can be found under. */
   @Input
   @Optional
   String schema

   /**
    * Set the DataType factory to add support for non-standard database vendor
    * data types.
    */
   @Input
   @Optional
   String dataTypeFactoryName = DEFAULT_DATA_TYPE_FACTORY_NAME

   /** Enable or disable usage of JDBC batched statement by DbUnit */
   @Input
   @Optional
   boolean supportBatchStatement = false

   /**
    * Enable or disable multiple schemas support by prefixing table names with
    * the schema name.
    */
   @Input
   @Optional
   boolean useQualifiedTableNames = false

   /**
    * Enable or disable the warning message displayed when DbUnit encounter an
    * unsupported data type.
    */
   @Input
   @Optional
   boolean datatypeWarning = false

   /** escapePattern */
   @Input
   @Optional
   String escapePattern

   /** skipOracleRecycleBinTables */
   @Input
   @Optional
   boolean skipOracleRecycleBinTables = false

   /**
    * Skip the execution when true, very handy when using together with
    * maven.test.skip.
    */
   @Input
   @Optional
   boolean skip = false

   /** Class name of metadata handler. */
   @Input
   @Optional
   String metadataHandlerName = DEFAULT_METADA_HANDLER_NAME

   /** Be case sensitive when handling tables. */
   @Input
   @Optional
   boolean caseSensitiveTableNames = false

   IDatabaseConnection connection

   AbstractDbunitTask() {
      super()
      group = DbunitGradlePlugin.PLUGIN_GROUP
   }

   @TaskAction
   void run() {

      try {

         logger.debug("Creating connection")

         // Connection properties
         Properties properties = new Properties()
         properties.put(PROPERTY_USER, username)
         properties.put(PROPERTY_PASSWORD, password)

         // Database driver
         Driver driver = (Driver) Class.forName(driver).newInstance()
         Connection driverConnection = driver.connect(url, properties)
         driverConnection.setAutoCommit(true)

         // Database connection
         connection = new DatabaseConnection(driverConnection, schema)
         DatabaseConfig config = connection.getConfig()

         // Connection configuration
         config.setProperty(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, supportBatchStatement)
         config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, useQualifiedTableNames)
         config.setProperty(DatabaseConfig.FEATURE_DATATYPE_WARNING, datatypeWarning)
         config.setProperty(DatabaseConfig.FEATURE_SKIP_ORACLE_RECYCLEBIN_TABLES, skipOracleRecycleBinTables)
         config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, caseSensitiveTableNames)
         config.setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, escapePattern)
         config.setProperty(DatabaseConfig.PROPERTY_RESULTSET_TABLE_FACTORY, new ForwardOnlyResultSetTableFactory())

         // Setup data type factory
         IDataTypeFactory dataTypeFactory = (IDataTypeFactory) Class.forName(dataTypeFactoryName).newInstance()
         config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dataTypeFactory)

         // Setup metadata handler
         IMetadataHandler metadataHandler = (IMetadataHandler) Class.forName(metadataHandlerName).newInstance()
         config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, metadataHandler)

         // Do concrete task
         doTask()

      } catch (Exception e) {
         logger.error("Exception", e)
      } finally {
         if (connection != null) {
            connection.close()
         }
      }
   }

   /**
    * After the initial configuration, delegate to concrete task the
    * configuration and execution.
    */
   protected abstract void doTask()
}
