/*
package ru.avolan.Regenerator.tasks;

import ru.avolan.Regenerator.RegeneratorPlugin;
import ru.avolan.Regenerator.Handlers.MsgType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.avolan.Regenerator.Handlers.MsgType;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQLThreadPoolJob {
    RegeneratorPlugin plugin;
    // пул, в котором содержатся все соединения
    private static HikariDataSource dbPool;

    // в этом сервисе будем параллельно выполнять запросы
    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    public void main(String[] args) throws SQLException {

        // конфигурируем пул
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        props.setProperty("dataSource.url", "jdbc:mysql://localhost/hello");
        props.setProperty("dataSource.user", "user");
        props.setProperty("dataSource.password", "password");
        props.setProperty("poolName", "MyFirstPool");
        props.setProperty("maximumPoolSize", "5"); // в этом пуле будет максимум 5 соединений
        props.setProperty("minimumIdle", "1"); // как минимум одно активное соединение там будет жить постоянно
        dbPool = new HikariDataSource(new HikariConfig(props));
        doTheJob();
    }

    private void doTheJob() throws SQLException {

        // сколько запросов будем делать параллельно
        int selects = 5;

        // этот объект позволит нам дождаться выполнения всех запросов,
        // выполняющихся в параллельных потоках чтобы подсчитать общее время
        // выполнения всех запросов
        CountDownLatch waitLatch = new CountDownLatch(selects);

        long startTime = System.nanoTime();
        for ( int i = 0; i < selects; ++i ) {
            executor.submit(new ThreadPoolJob(dbPool, waitLatch));
        }

        try {
            // ждём когда все воркеры закончат
            waitLatch.await();
        } catch ( InterruptedException e ) {
            plugin.utils.throwMessage(MsgType.WARNING, "latch was broken by interruption request");
        }
        long timeElapsed = System.nanoTime() - startTime;
        System.out.println("All queries was executed in: " + (timeElapsed / 1000000000) + " sec");

    }

    // класс-воркер, который будет выполнять запрос
    public static class ThreadPoolJob implements Runnable {

        private final HikariDataSource dbPool;

        private final CountDownLatch waitLatch;

        ThreadPoolJob(HikariDataSource dbPool, CountDownLatch waitLatch) {
            this.dbPool = dbPool;
            this.waitLatch = waitLatch;
        }

        @Override
        public void run() {
            String tName = Thread.currentThread().getName();
            System.out.println(tName + ": Start query");
            try (
                    Connection conn = dbPool.getConnection();
                    Statement stmt = conn.createStatement();
            ) {
                // здесь мы не получаем и не работаем с ResultSet, поэтому не
                // описываем его в try-with-resources
                stmt.execute("SELECT SLEEP(5)");
                System.out.println(tName + ": Query completed");
            } catch ( SQLException e ) {
                e.printStackTrace();
            } finally {
                waitLatch.countDown();
            }
        }
    }

}
*/
