package com.example.mock.order.common.datasource;

/** Bean names of the two jOOQ configurations: primary (writes) and read replica (reads). */
public final class DataSourceConstant {

    public static final String READ_JOOQ_CONFIGURATION = "readJooqConfiguration";
    public static final String WRITE_JOOQ_CONFIGURATION = "writeJooqConfiguration";

    private DataSourceConstant() {
    }
}
