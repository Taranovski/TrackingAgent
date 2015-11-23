package com.epam.nonstop.parkotronic.trackingagent.asynctask;

import android.os.AsyncTask;
import android.widget.TextView;

import com.epam.nonstop.parkotronic.trackingagent.dto.Dto;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Alyx on 22.11.2015.
 */
public class RestCallTask extends AsyncTask<String, Integer, Dto> {
    private RestTemplate restTemplate;
    private Dto response;
    private TextView editableResult;

    public RestCallTask(TextView editableResult) {
        this.editableResult = editableResult;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());


    }

    @Override
    protected Dto doInBackground(String... params) {

        response = restTemplate.getForObject(params[0], Dto.class);

        return response;
    }

    @Override
    protected void onPostExecute(Dto s) {

        String string = s.getString();

        editableResult.setText(string);
    }
}
