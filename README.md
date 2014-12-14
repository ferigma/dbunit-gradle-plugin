# DbUnit Gradle Plugin

The DbUnit Gradle Plugin allows you to easily launch [DbUnit](http://dbunit.sourceforge.net) tasks from your gradle project. It is based on the [DbUnit Maven Plugin](http://mojo.codehaus.org/dbunit-maven-plugin/) to provide the same (and more) functionality but simplifying the configuration process.

## Tasks

There are three tasks included in the plugin:

- **Operation**: Allows to specify a list of sources to operate against the database. Each source contains the file path with the data and the database operation. Available operations are INSERT, CLEAN, CLEAN_INSERT, DELETE, ...
- **Compare**: Compares a file against the current database content.  
- **Export**: Exports the current database content to file.

## Getting started

1. Install the `dbunit-gradle-plugin` (in your local Maven repository, for example):
 ```groovy
 gradle publishToMavenLocal
 ```
2. Inside the `build.gradle` file of your current project include the plugin and the library with the database driver:

 ```groovy
 buildscript {
    repositories {
       mavenLocal()
       mavenCentral()
    }
    dependencies {
       classpath "com.ferigma:dbunit-gradle-plugin:0.1.0"
       classpath "com.h2database:h2:1.4.179"
    }
 }
 ```

3. Apply the plugin wherever you want using the DSL gradle syntax. Example:

 - Create a new task that populates your DB, executing the operation `CLEAN_INSERT` and using the file `$rootFir/db/sample-data.xml`:

  ```groovy
  task populateTestDb(type: com.ferigma.gradle.dbunit.tasks.OperationTask) {
     username = "sa"
     password = "sa"
     url = "jdbc:h2:/tmp/h2_test"
     driver = "org.h2.Driver"
     dataTypeFactoryName = "org.dbunit.ext.h2.H2DataTypeFactory"
     sources = [
        new com.ferigma.gradle.dbunit.tasks.vo.OperationSource(
        transaction: true, type: "CLEAN_INSERT", format: "xml",
        file: "$rootDir/db/sample-data.xml")
     ]
  }
  ```

Tip: You can execute this task just before another. A typical use case is to 
populate the database with sample data for tests:

```groovy
test.dependsOn populateTestDb
``` 

## References
- DbUnit: http://dbunit.sourceforge.net
- DbUnit Maven Plugin: http://mojo.codehaus.org/dbunit-maven-plugin/  
