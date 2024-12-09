package com.toel.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toel.exception.AppException;
import com.toel.exception.ErrorCode;
import com.toel.model.Account;
import com.toel.model.Log;
import com.toel.repository.AccountRepository;
import com.toel.util.log.DatabaseUtils;

import org.apache.logging.log4j.LogManager;

@Service
public class Service_Log {
    @Autowired
    AccountRepository accountRepository;

    public void setLog(Class<?> logClass, Integer accountID, String level, String tableName, Integer id_Object,
            String action_type) {
        // Logger logger = LogManager.getLogger(logClass);
        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_FOUND, "Account"));
        Log log = new Log();
        log.setLevel(level);
        log.setAccount(account);
        log.setAction_type(action_type);
        log.setTableName(tableName);
        log.setId_Object(id_Object);

        com.toel.util.log.LogManager logManager = new com.toel.util.log.LogManager();
        logManager.listen();
        logManager.submit(log);
        // long start = new Date().getTime();

        // logger.info("Async: " + (new Date().getTime() - start) + "ms");
        // start = new Date().getTime();
        // Ghi log không dùng thread
        // save(log,logClass);
        // logger.info("No-async: " + (new Date().getTime() - start) + "ms");
    }

    private void save(Log log, Class<?> logClass) {
        Logger logger = LogManager.getLogger(logClass);
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = DatabaseUtils.getConnection();
            stmt = con.prepareStatement(
                    " INSERT INTO logs (timestamps, level, tableName, id_Object, action_type, account_id) " +
                            " VALUES (CURRENT_TIMESTAMP, ?,?,?,?,?)");
            stmt.setString(1, log.getLevel());
            stmt.setString(2, log.getTableName());
            stmt.setInt(3, log.getId_Object());
            stmt.setString(4, log.getAction_type());
            stmt.setInt(5, log.getAccount().getId());

            stmt.execute();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            DatabaseUtils.closeObject(stmt);
            DatabaseUtils.closeObject(con);
        }
    }
}
