package com.vanpro.zitech125.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.vanpro.zitech125.manage.StatusManage;

public class CompassSensor implements SensorEventListener {
    private static final String TAG = "CompassSensor";

	private SensorManager sensorManager;
	private Sensor gsensor;
	private Sensor msensor;
	private Sensor osensor;
	private float[] mGravity = new float[3];
	private float[] mGeomagnetic = new float[3];
	private float azimuth = 0f;
	private float currectAzimuth = 0;
	private boolean ready = false;

	public CompassSensor(Context context) {
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		osensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}

	public void start() {
//		sensorManager.registerListener(this, gsensor,
//				SensorManager.SENSOR_DELAY_GAME);
//		sensorManager.registerListener(this, msensor,
//				SensorManager.SENSOR_DELAY_GAME);

		sensorManager.registerListener(this,osensor,SensorManager.SENSOR_DELAY_GAME);
	}

	public void stop() {
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		final float alpha = 0.97f;

		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

				float degree = Math.round(event.values[0]);
				degree = (degree + 360) % 360;
//				LogUtil.e(TAG, "azimuth (deg): " + degree);
				StatusManage.getInstance().setRotation(degree);
			}
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

//				mGravity[0] = alpha * mGravity[0] + (1 - alpha)
//						* event.values[0];
//				mGravity[1] = alpha * mGravity[1] + (1 - alpha)
//						* event.values[1];
//				mGravity[2] = alpha * mGravity[2] + (1 - alpha)
//						* event.values[2];
				for(int i = 0 ; i < 3 ; i ++){
					mGravity[i] = event.values[i];
				}
				// mGravity = event.values;
				if(mGeomagnetic[0] != 0) //如果accelerator和magnetic传感器都有数值，设置为真
					ready = true;

				LogUtil.e(TAG, "TYPE_ACCELEROMETER"+Float.toString(mGravity[0]));
			}

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				// mGeomagnetic = event.values;

//				mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
//						* event.values[0];
//				mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
//						* event.values[1];
//				mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
//						* event.values[2];

				for(int i = 0 ; i < 3 ; i ++){
					mGeomagnetic[i] = event.values[i];
				}
				if(mGravity[2] != 0) //检查accelerator和magnetic传感器都有数值，只是换一个轴向检查
					ready = true;
//				LogUtil.e(TAG, "TYPE_MAGNETIC_FIELD"+Float.toString(event.values[0]));

			}

			if(!ready)
				return;

			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				// Log.d(TAG, "azimuth (rad): " + azimuth);
				azimuth = (float) Math.toDegrees(orientation[0]); // orientation
				azimuth = (azimuth + 360) % 360;
//				 LogUtil.e(TAG, "azimuth (deg): " + azimuth);
//				StatusManage.getInstance().setRotation(azimuth);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}