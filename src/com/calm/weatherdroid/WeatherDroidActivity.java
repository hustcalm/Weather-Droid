package com.calm.weatherdroid;

import java.io.UnsupportedEncodingException;  

import android.app.Activity;  
import android.os.Bundle;  
import android.view.View;  
import android.widget.Button;  
import android.widget.EditText;
import android.widget.Toast;  
  
import org.ksoap2.SoapEnvelope;  
import org.ksoap2.serialization.SoapObject;  
import org.ksoap2.serialization.SoapSerializationEnvelope;  
//import org.ksoap2.transport.AndroidHttpTransport;  
import org.ksoap2.transport.HttpTransportSE;  
  
public class WeatherDroidActivity extends Activity {  
  
    private Button queryButton;  
    private EditText cityName;
    
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        
        cityName = (EditText)this.findViewById(R.id.weather_city_edit);
        
        queryButton = (Button)this.findViewById(R.id.goQuery);  
        queryButton.setOnClickListener(new Button.OnClickListener() {  
            public void onClick(View v) {  
                  String city = "北京";  
                  city = cityName.getText().toString();
                  
                  getWeather(city);    
            }  
  
        });  
    }  
  
    private static final String NAMESPACE = "http://WebXml.com.cn/";  
  
    // WebService地址  
    private static String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";  
  
    private static final String METHOD_NAME = "getWeatherbyCityName";  
  
    private static String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";  
  
    private String weatherToday;  
  
    private SoapObject detail;  
  
    public void getWeather(String cityName) {  
        try {  
            System.out.println("rpc------");  
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);  
            System.out.println("rpc" + rpc);  
            System.out.println("cityName is " + cityName);  
            rpc.addProperty("theCityName", cityName);  
  
              
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
            envelope.bodyOut = rpc;  
            envelope.dotNet = true;  
            envelope.setOutputSoapObject(rpc);  
              
            HttpTransportSE ht = new HttpTransportSE(URL);  
  
            //AndroidHttpTransport ht = new AndroidHttpTransport(URL);  
            ht.debug = true;  
  
            ht.call(SOAP_ACTION, envelope);  
            //ht.call(null, envelope);  
  
            //SoapObject result = (SoapObject)envelope.bodyIn;  
            //detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");  
  
            detail =(SoapObject) envelope.getResponse();  
              
            //System.out.println("result" + result);  
            System.out.println("detail" + detail);  
            Toast.makeText(this, detail.toString(), Toast.LENGTH_LONG).show();  
            parseWeather(detail);  
  
            return;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    private void parseWeather(SoapObject detail)  
            throws UnsupportedEncodingException {  
        String date = detail.getProperty(6).toString();  
        weatherToday = "今天：" + date.split(" ")[0];  
        weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];  
        weatherToday = weatherToday + "\n气温："  
                + detail.getProperty(5).toString();  
        weatherToday = weatherToday + "\n风力："  
                + detail.getProperty(7).toString() + "\n";  
        System.out.println("weatherToday is " + weatherToday);  
        Toast.makeText(this, weatherToday, Toast.LENGTH_LONG).show();  
  
    }  
}  

