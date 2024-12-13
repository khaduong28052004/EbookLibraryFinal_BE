package com.toel.util.log;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toel.model.Log;

import org.apache.logging.log4j.LogManager;

import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

            stmt = con.prepareStatement(
                    " INSERT INTO logs (timestamps, level, tableName, dataNew, dataOld, action_type, account_id) " +
                            " VALUES (CURRENT_TIMESTAMP, ?,?,?,?,?,?)");
            for (Log log : lst) {
                stmt.setString(1, log.getLevel());
                stmt.setString(2, log.getTableName());
                stmt.setString(3, log.getDataNew());
                stmt.setString(4, log.getDataOld());
                stmt.setString(5, log.getAction_type());
                stmt.setInt(6, log.getAccount().getId());

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
