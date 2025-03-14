package com.alibaba.polardbx.qatest.ddl.auto.mpp.monitor;

import com.alibaba.polardbx.optimizer.config.table.ScaleOutPlanUtil;
import com.alibaba.polardbx.qatest.DDLBaseNewDBTestCase;
import com.alibaba.polardbx.qatest.ddl.auto.mpp.base.PkRangeTestParam;
import com.alibaba.polardbx.qatest.ddl.auto.mpp.pkrange.PkTest;
import io.airlift.slice.DataSize;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@NotThreadSafe
@RunWith(Parameterized.class)
public class PauseHandlyOnLargeTableTest extends DDLBaseNewDBTestCase {

    final static Log log = LogFactory.getLog(PkTest.class);
    private String tableName = "";
    private static final String createOption = " if not exists ";

    public PauseHandlyOnLargeTableTest(boolean crossSchema) {
        this.crossSchema = crossSchema;
    }

    @Parameterized.Parameters(name = "{index}:crossSchema={0}")
    public static List<Object[]> initParameters() {
        return Arrays.asList(new Object[][] {
            {false}});
    }

    @Before
    public void init() {
        this.tableName = schemaPrefix + randomTableName("empty_table", 4);
    }

    @Override
    public boolean usingNewPartDb() {
        return true;
    }

    @Test
    public void testPauseAndContinueForLargeTable1() throws Exception {
        String schemaName = "pk_range_test";
        String originalTableName = "large_table_auto";
        String gsiName = randomTableName("gsi_large_", 8);
        // prepare data
        String createTableStmt =
            "create table if not exists " + " %s(a int NOT NULL AUTO_INCREMENT,b int, c varchar(32), PRIMARY KEY(a)"
                + ") PARTITION BY HASH(a) PARTITIONS %d";
        int partNum = 8;
        int gsiPartNum = 8;
        int eachPartRows = 200_000;
        Boolean enablePkRange = true;
        Boolean enableLocalIndexLater = true;
        String addGsiStmt = "alter table %s add global index %s(b) covering(c) partition by hash(b) partitions %d";
        PkRangeTestParam pkRangeTestParam =
            new PkRangeTestParam(schemaName, originalTableName, gsiName, createTableStmt, addGsiStmt, partNum,
                gsiPartNum, eachPartRows, enablePkRange, enableLocalIndexLater);
        String pkRangeSize = "32kB";
        String taskRangeSize = "8MB";
        String totalSize = "80MB";
        long expectedPkRangeNum =
            DataSize.convertToByte(totalSize) / DataSize.convertToByte(taskRangeSize);
        pkRangeTestParam.setExpectedPkRangeNum(expectedPkRangeNum / 2);
        pkRangeTestParam.setTaskRangeSize(taskRangeSize);
        pkRangeTestParam.setPkRangeSize(pkRangeSize);
        pkRangeTestParam.setExpectedLocalIndexConcurrency(3);
        pkRangeTestParam.setCheckInterval(2000L);
        pkRangeTestParam.setSleepInterval(2000L);
        PkRangeTestParam.controlTest(log, pkRangeTestParam, tddlConnection);
    }

    @Test
    public void testPauseAndContinueForLargeTable2() throws Exception {
        String schemaName = "pk_range_test";
        String originalTableName = "large_table_auto";
        String gsiName = randomTableName("gsi_large_", 8);
        // prepare data
        String createTableStmt =
            "create table if not exists " + " %s(a int NOT NULL AUTO_INCREMENT,b int, c varchar(32), PRIMARY KEY(a)"
                + ") PARTITION BY HASH(a) PARTITIONS %d";
        int partNum = 8;
        int gsiPartNum = 8;
        int eachPartRows = 400_000;
        Boolean enablePkRange = true;
        Boolean enableLocalIndexLater = true;
        String addGsiStmt = "alter table %s add global index %s(b) covering(c) partition by hash(b) partitions %d";
        PkRangeTestParam pkRangeTestParam =
            new PkRangeTestParam(schemaName, originalTableName, gsiName, createTableStmt, addGsiStmt, partNum,
                gsiPartNum, eachPartRows, enablePkRange, enableLocalIndexLater);
        String pkRangeSize = "128kB";
        String taskRangeSize = "16MB";
        String totalSize = "160MB";
        long expectedPkRangeNum =
            DataSize.convertToByte(totalSize) / DataSize.convertToByte(taskRangeSize);
        pkRangeTestParam.setExpectedPkRangeNum(expectedPkRangeNum / 2);
        pkRangeTestParam.setTaskRangeSize(taskRangeSize);
        pkRangeTestParam.setPkRangeSize(pkRangeSize);
        pkRangeTestParam.setExpectedLocalIndexConcurrency(3);
        pkRangeTestParam.setCheckInterval(2000L);
        pkRangeTestParam.setSleepInterval(2000L);
        PkRangeTestParam.controlTest(log, pkRangeTestParam, tddlConnection);
    }

}