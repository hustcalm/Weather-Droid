package com.calm.xuhanwennuan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private static final String GOOGLE_API_URL = "http://www.google.com/ig/api?weather=";

	private static final String NETWORK_ERROR = "网络异常";

	private EditText editText;

	// private Handler messageHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		editText = (EditText) findViewById(R.id.weather_city_edit);

		Button button = (Button) findViewById(R.id.goQuery);

		button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				// 获得用户输入的城市名称

				String city = editText.getText().toString();

				// 必须每次都重新创建一个新的task实例进行查询，否则将提示如下异常信息

				// the task has already been executed (a task can be executed
				// only once)

				new GetWeatherTask().execute(city);

				// 创建一个子线程去做耗时的网络连接工作

				// new Thread() {
				//
				// @Override
				// public void run() {
				//
				// // 活动用户输入的城市名称
				//
				// String city = editText.getText().toString();
				//
				// // 调用Google 天气API查询指定城市的当日天气情况
				//
				// String weather = getWetherByCity(city);
				//
				// // 创建一个Message对象，并把得到的天气信息赋值给Message对象
				//
				// Message message = Message.obtain();
				//
				// message.obj = weather;
				//
				// // 通过Handler发布携带有天气情况的消息
				//
				// messageHandler.sendMessage(message);
				// }
				// }.start();

			}
		});

		// button.setOnClickListener(btnListener);

		// 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到

		// Looper looper = Looper.myLooper();

		// 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper

		// messageHandler = new MessageHandler(looper);

	}

	// // 子类化一个Handler
	//
	// class MessageHandler extends Handler {
	//
	// public MessageHandler(Looper looper) {
	//
	// super(looper);
	//
	// }
	//
	// @Override
	// public void handleMessage(Message msg) {
	//
	// // 处理收到的消息，把天气信息显示在title上
	//
	// setTitle((String) msg.obj);
	//
	// }
	// }

	public String getWetherByCity(String city) {

		HttpClient httpClient = new DefaultHttpClient();

		HttpContext localContext = new BasicHttpContext();

		HttpGet httpGet = new HttpGet(GOOGLE_API_URL + city);

		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {

				httpGet.abort();
			}

			else {
				HttpEntity httpEntity = response.getEntity();

				return parseWeather(httpEntity.getContent());
			}

		} catch (Exception e) {

			Log.e("WeatherReport", "Failed to get weather", e);
		} finally {

			httpClient.getConnectionManager().shutdown();
		}

		return NETWORK_ERROR;

	}

	class GetWeatherTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			String city = params[0];

			// 调用Google 天气API查询指定城市的当日天气情况

			return getWetherByCity(city);

		}

		protected void onPostExecute(String result) {

			// 把doInBackground处理的结果即天气信息显示在title上

			setTitle(result);

		}

	}

	// private void parseWeather(SoapObject detail)
	// throws UnsupportedEncodingException {
	// String date = detail.getProperty(6).toString();
	// weatherToday = "今天：" + date.split(" ")[0];
	// weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];
	// weatherToday = weatherToday + "\n气温："
	// + detail.getProperty(5).toString();
	// weatherToday = weatherToday + "\n风力："
	// + detail.getProperty(7).toString() + "\n";
	// System.out.println("weatherToday is " + weatherToday);
	// Toast.makeText(this, weatherToday, Toast.LENGTH_LONG).show();
	//
	// }

	// Parse InputStream to String
	public static String parseWeather(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
