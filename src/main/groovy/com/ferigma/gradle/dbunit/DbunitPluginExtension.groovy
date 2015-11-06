package com.ferigma.gradle.dbunit

/**
 * This is an extension object for grouping properties together. This is used to add a configuration
 * block to the Gradle project.
 *
 * @author Sion Williams
 */
class DbunitPluginExtension {
    static final String DEFAULT_DATA_TYPE_FACTORY_NAME =
            "org.dbunit.dataset.datatype.DefaultDataTypeFactory"
    static final String DEFAULT_METADA_HANDLER_NAME =
            "org.dbunit.database.DefaultMetadataHandler"

    /** The driver. */
    String driver

    /** The username. */
    String username

    /** The password. */
    String password

    /** The url. */
    String url

    /** The schema name that tables can be found under. */
    String schema

    /**
     * Set the DataType factory to add support for non-standard database vendor
     * data types.
     */
    String dataTypeFactoryName = DEFAULT_DATA_TYPE_FACTORY_NAME

    /** Enable or disable usage of JDBC batched statement by DbUnit */
    boolean supportBatchStatement = false

    /**
     * Enable or disable multiple schemas support by prefixing table names with
     * the schema name.
     */
    boolean useQualifiedTableNames = false

    /**
     * Enable or disable the warning message displayed when DbUnit encounter an
     * unsupported data type.
     */
    boolean datatypeWarning = false

    /** escapePattern */
    String escapePattern

    /** skipOracleRecycleBinTables */
    boolean skipOracleRecycleBinTables = false

    /**
     * Skip the execution when true, very handy when using together with
     * maven.test.skip.
     */
    boolean skip = false

    /** Class name of metadata handler. */
    String metadataHandlerName = DEFAULT_METADA_HANDLER_NAME

    /** Be case sensitive when handling tables. */
    boolean caseSensitiveTableNames = false
}
