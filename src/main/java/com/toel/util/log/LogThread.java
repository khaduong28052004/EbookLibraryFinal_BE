package com.toel.util.log;

import org.apache.logging.log4j.Logger;

import com.toel.model.Log;

import org.apache.logging.log4j.LogManager;

import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogThread extends Task {
    private final Logger logger = LogManager.getLogger(LogThread.class);

    @Override
    public Integer call() throws Exception {
        List lstLog = getItems();
        try {
            if (lstLog != null && !lstLog.isEmpty()) {
                save(lstLog);
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            return 0;
        }
        return 1;
    }

    public void save(List<Log> lst) {
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = DatabaseUtils.getConnection();

            stmt = con.prepareStatement("INSERT INTO logs (timestamps, level, tableName, id_Object, action_type, account_id)"+
			" VALUES (CURRENT_TIMESTAMP,?,?,?,?,?)");
            for (Log item : lst) {
                stmt.setString(1, item.getLevel());
                stmt.setString(2, item.getTableName());
                stmt.setInt(3, item.getId_Object());
                stmt.setString(4, item.getAction_type());
                stmt.setInt(5, item.getAccount().getId());

                stmt.execute();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            DatabaseUtils.closeObject(stmt);
            DatabaseUtils.closeObject(con);
        }
    }
}
