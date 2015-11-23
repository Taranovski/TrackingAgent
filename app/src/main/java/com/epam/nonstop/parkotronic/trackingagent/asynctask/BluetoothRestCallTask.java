package com.epam.nonstop.parkotronic.trackingagent.asynctask;

import android.os.AsyncTask;
import android.widget.TextView;

import com.epam.nonstop.parkotronic.trackingagent.dto.Dto;
import com.epam.nonstop.parkotronic.trackingagent.dto.SpotInfo;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Alyx on 23.11.2015.
 */
public class BluetoothRestCallTask extends AsyncTask<String, Integer, Dto> {


    private final TextView discoveryResultEditableText;
    private final String deviceId;
    private final String xCoordinate;
    private final String yCoordinate;
    private final String name;
    private final String address;

    private RestTemplate restTemplate;

    public BluetoothRestCallTask(TextView discoveryResultEditableText,
                                 String deviceId, String xCoordinate,
                                 String yCoordinate, String name,
                                 String address) {

        this.discoveryResultEditableText = discoveryResultEditableText;
        this.deviceId = deviceId;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.name = name;
        this.address = address;


        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Override
    protected Dto doInBackground(String... params) {
        String url = params[0];

        SpotInfo spotInfo = new SpotInfo();

        ResponseEntity<Dto> dtoResponseEntity = restTemplate.postForEntity(params[0], spotInfo, Dto.class);




        return dtoResponseEntity.getBody();
    }

    @Override
    protected void onPostExecute(Dto spotInfo) {
        String string = spotInfo.getString();

        discoveryResultEditableText.setText(string);
    }
}
