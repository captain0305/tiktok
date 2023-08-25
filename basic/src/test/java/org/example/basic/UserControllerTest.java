package org.example.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@SpringBootTest
public class UserControllerTest {
    @Test
    public void testTimeStamps(){
        System.out.println(System.currentTimeMillis()-7*24*60*60*1000);
    }

    @Test
    public void testTimeStamps2(){

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // 将Timestamp转换为Date
        Date date = new Date(timestamp.getTime());
        System.out.println(  timestamp.getTime());
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Date: " + date);
    }
}
