/*
 * Copyright [2013-2021], Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.calcite.sql;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeFieldImpl;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorScope;

/**
 * @author ximing.yd
 * @date 2022/1/5 3:22 下午
 */
public class SqlShowPartitionsHeatmap extends SqlShow {

    private static final SqlSpecialOperator OPERATOR = new SqlShowPartitionsHeatmapOperator();

    public SqlShowPartitionsHeatmap(SqlParserPos pos, List<SqlSpecialIdentifier> specialIdentifiers, SqlNode timeRange, SqlNode type) {
        super(pos, specialIdentifiers, Arrays.asList(timeRange, type));
    }

    public static SqlShowPartitionsHeatmap create(SqlParserPos pos, List<SqlSpecialIdentifier> specialIdentifiers,
                                                  SqlNode timeRange, SqlNode type) {
        return new SqlShowPartitionsHeatmap(pos, specialIdentifiers, timeRange, type);
    }


    @Override
    protected boolean showWhere() {
        return false;
    }

    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Override
    public SqlKind getShowKind() {
        return SqlKind.SHOW_PARTITONS_HEATMAP;
    }

    public static class SqlShowPartitionsHeatmapOperator extends SqlSpecialOperator {

        public SqlShowPartitionsHeatmapOperator() {
            super("SHOW_PARTITIONS_HEATMAP", SqlKind.SHOW_PARTITONS_HEATMAP);
        }

        @Override
        public RelDataType deriveType(SqlValidator validator, SqlValidatorScope scope, SqlCall call) {
            final RelDataTypeFactory typeFactory = validator.getTypeFactory();
            List<RelDataTypeFieldImpl> columns = new LinkedList<>();
            columns.add(new RelDataTypeFieldImpl("HEATMAP", 0, typeFactory.createSqlType(SqlTypeName.VARCHAR)));

            return typeFactory.createStructType(columns);
        }
    }

}
