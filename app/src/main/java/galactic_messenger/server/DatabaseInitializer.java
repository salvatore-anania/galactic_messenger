package galactic_messenger.server;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
 
@Configuration
public class DatabaseInitializer {
 
    @Autowired JdbcTemplate jdbcTemplate;
     
    @Bean
    CommandLineRunner loadDatabase() {
        return new CommandLineRunner() {
             
            @Override
            public void run(String... args) throws Exception {
                try {
                    jdbcTemplate.execute("drop table users");
                } catch (Exception e) {
                    
                }
                jdbcTemplate.execute("create table users (id int primary key auto_increment, name varchar(30),password varchar(255))");
                
                jdbcTemplate.execute("insert into users (name, password) values ('Will Smith','password')");
                 
                jdbcTemplate.execute("insert into users (name, password) values ('Bill Gates','password')");
            }
        };
    }
}