/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.yjcpaj4.play_with_us.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public final class DBConnector {

    static class DBAccess {

        static final String HOSTNAME = "127.0.0.1";
        static final String SCHEMA = "fetch_data";
        static final String USERNAME = "root";
        static final String PASSWORD = "@dnpqrpdla";

        static String getURL() {
            return "jdbc:mysql://" + HOSTNAME + ":3306/" + SCHEMA
                    + "?useUnicode=true"
                    + "&characterEncoding=UTF8"
                    + "&jdbcCompliantTruncation=false"
                    + "&useOldUTF8Behavior=true";
        }
    }

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final ThreadLocal<Connection> mConnect = new ThreadLocalConnection();

    private DBConnector() {
    }

    public static Connection connect() {
        return mConnect.get();
    }

    private final static class ThreadLocalConnection extends ThreadLocal<Connection> {

        private static final Collection<Connection> mConnect = new LinkedList();

        @Override
        protected final Connection initialValue() {
            try {
                Class.forName(DRIVER);
                Connection c = DriverManager.getConnection(DBAccess.getURL(), DBAccess.USERNAME, DBAccess.PASSWORD);
                ThreadLocalConnection.mConnect.add(c);
                return c;
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
