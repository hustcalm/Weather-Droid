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

	private static final String NETWORK_ERROR = "�����쳣";

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

				// ����û�����ĳ�������

				String city = editText.getText().toString();

				// ����ÿ�ζ����´���һ���µ�taskʵ�����в�ѯ��������ʾ�����쳣��Ϣ

				// the task has already been executed (a task can be executed
				// only once)

				new GetWeatherTask().execute(city);

				// ����һ�����߳�ȥ����ʱ���������ӹ���

				// new Thread() {
				//
				// @Override
				// public void run() {
				//
				// // ��û�����ĳ�������
				//
				// String city = editText.getText().toString();
				//
				// // ����Google ����API��ѯָ�����еĵ����������
				//
				// String weather = getWetherByCity(city);
				//
				// // ����һ��Message���󣬲��ѵõ���������Ϣ��ֵ��Message����
				//
				// Message message = Message.obtain();
				//
				// message.obj = weather;
				//
				// // ͨ��Handler����Я���������������Ϣ
				//
				// messageHandler.sendMessage(message);
				// }
				// }.start();

			}
		});

		// button.setOnClickListener(btnListener);

		// �õ���ǰ�̵߳�Looperʵ�������ڵ�ǰ�߳���UI�߳�Ҳ����ͨ��Looper.getMainLooper()�õ�

		// Looper looper = Looper.myLooper();

		// �˴��������Բ���Ҫ����Looper����Ϊ HandlerĬ�Ͼ�ʹ�õ�ǰ�̵߳�Looper

		// messageHandler = new MessageHandler(looper);

	}

	// // ���໯һ��Handler
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
	// // �����յ�����Ϣ����������Ϣ��ʾ��title��
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

			// ����Google ����API��ѯָ�����еĵ����������

			return getWetherByCity(city);

		}

		protected void onPostExecute(String result) {

			// ��doInBackground����Ľ����������Ϣ��ʾ��title��

			setTitle(result);

		}

	}

	// private void parseWeather(SoapObject detail)
	// throws UnsupportedEncodingException {
	// String date = detail.getProperty(6).toString();
	// weatherToday = "���죺" + date.split(" ")[0];
	// weatherToday = weatherToday + "\n������" + date.split(" ")[1];
	// weatherToday = weatherToday + "\n���£�"
	// + detail.getProperty(5).toString();
	// weatherToday = weatherToday + "\n������"
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
