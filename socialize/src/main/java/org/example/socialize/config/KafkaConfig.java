package org.example.socialize.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/29/16:03
 * @Description:
 */
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic FollowQueue(){
        return new NewTopic("FollowQueue",1, (short) 1);
    }

    @Bean
    public NewTopic FollowerQueue(){
        return new NewTopic("FollowerQueue",1, (short) 1);
    }

//    @Bean
//    public NewTopic MessageQueue(){
//        return new NewTopic("MessageQueue",1, (short) 1);
//    }


}
