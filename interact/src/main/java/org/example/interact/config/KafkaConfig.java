package org.example.interact.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author carey
 */
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic commentQueue(){
        return new NewTopic("commentQueue",1, (short) 1);
    }

    @Bean
    public NewTopic removeCommentQueue(){
        return new NewTopic("removeCommentQueue",1, (short) 1);
    }

    @Bean
    public NewTopic favoriteQueue(){
        return new NewTopic("favoriteQueue",1, (short) 1);
    }

    @Bean
    public NewTopic removeFavoriteQueue(){
        return new NewTopic("removeFavoriteQueue",1, (short) 1);
    }

}
