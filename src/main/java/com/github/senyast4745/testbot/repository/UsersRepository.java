package com.github.senyast4745.testbot.repository;

import com.github.senyast4745.testbot.Main;
import com.github.senyast4745.testbot.utils.PostgresJDBCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UsersRepository  {

    private static PostgresJDBCUtils jdbcUtils;

    private static final Logger log = LoggerFactory.getLogger(UsersRepository.class);

    private static final String createTableSQL = "create table if not exists users (\r\n" +
            " id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
            "  tamtam_user_id bigint not null,\r\n" + "  git_hub_repo varchar(127) not null\r\n" + "  ) ;";

    private static final String insertNewUser = "insert into users (tamtam_user_id, git_hub_repo) \r\nvalues(%d , '%s') ;";

    private static final String selectTamTamUser = "select tamtam_user_id from users where git_hub_repo = '%s' ;";
    private static final String selectGitHubRepositories = "select git_hub_repo from users where tamtam_user_id = %d ;";

    public static void init() {
        try {
            List<String> properties = Main.getProperty("jdbc.postres.url","jdbc.postgres.username","jdbc.postgres.password");
            jdbcUtils = PostgresJDBCUtils.getInstance(properties.get(0), properties.get(1), properties.get(2));
            jdbcUtils.createTable(createTableSQL);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Long> getTamTamUser(String gitHubUserId) throws SQLException {

        if(jdbcUtils == null){
            init();
        }
         return jdbcUtils.executeQuery(String.format(selectTamTamUser, gitHubUserId));
    }
    public static List<String> getGitHubRepositories(long userId) throws SQLException {

        if(jdbcUtils == null){
            init();
        }
         return jdbcUtils.executeQuery(String.format(selectGitHubRepositories, userId));
    }

    public static void insertNewTamTamUser(long tamTamUserId, String gitHubUserUd) throws SQLException {

        if(jdbcUtils == null){
            init();
        }
        if (!getTamTamUser(gitHubUserUd).contains(tamTamUserId)) {
            log.info("INSERT NEW DATA");
            jdbcUtils.executeUpdate(String.format(insertNewUser, tamTamUserId, gitHubUserUd));
        }

    }
}

