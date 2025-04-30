package dqcs.dataqualityservice.config.clickhouse;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

public class ClickhouseBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private final List<Object[]> batch;

    public ClickhouseBatchPreparedStatementSetter(List<Object[]> batch) {
        this.batch = batch;
    }


    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Object[] row = batch.get(i);

        ps.setString(1, (String) row[0]);
        ps.setString(2, (String) row[1]);
        ps.setTimestamp(3, (Timestamp) row[2]);
        ps.setInt(4, (Integer) row[3]);
        ps.setString(5, (String) row[4]);

        if (row[5] != null) {
            ps.setString(6, row[5].toString());
        } else {
            ps.setNull(6, Types.VARCHAR);
        }

        ps.setString(7, (String) row[6]);
        ps.setString(8, (String) row[7]);
        ps.setInt(9, (Integer) row[8]);
        ps.setString(10, (String) row[9]);

        if (row[10] != null) {
            ps.setString(11, (String) row[10]);
        } else {
            ps.setNull(11, Types.VARCHAR);
        }
        ps.setString(12, (String) row[11]);
    }
    @Override
    public int getBatchSize() {
        return batch.size();
    }
}
