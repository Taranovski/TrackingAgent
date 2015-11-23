package com.epam.nonstop.parkotronic.trackingagent;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.epam.nonstop.parkotronic.trackingagent.dto.Dto;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
        String serverIp = "37.115.158.197";
    public ApplicationTest() {
        super(Application.class);
    }

    public void testConstriction() {
        MainActivity mainActivity = new MainActivity();
    }

    public void testRequestResponse() {

        String url = "http://" +
                serverIp +
                ":8080/nonstop/welcome?name=nonstop";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Dto response = restTemplate.getForObject(url, Dto.class);

        String string = response.getString();
        System.out.println(string);
        assertEquals("hello, nonstop", string);
    }


    public void testRequestResponse1() {

        String url = "http://" +
                serverIp +
                ":8080/nonstop/welcome?name=x:1;y:2";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Dto response = restTemplate.getForObject(url, Dto.class);

        String string = response.getString();
        System.out.println(string);
        assertEquals("hello, x:1;y:2", string);
    }
}