package br.com.cpmh.plus.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.dashboard.DashboardActivity;

/**
 *
 */
public class SplashScreenActivity extends AppCompatActivity implements Runnable, OnCompleteListener<Void>
{
	private static final String TAG = "SplashScreenActivity";
	private static final int TIMEOUT = 2000;

	private Handler handler;

	private FirebaseRemoteConfig config;

	private FirebaseAuth auth;
	private FirebaseUser user;

	/**
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		handler = new Handler();

		auth = FirebaseAuth.getInstance();

		config = FirebaseRemoteConfig.getInstance();
		config.setDefaults(R.xml.remote_config_defaults);
	}

	/**
	 * @param task
	 */
	@Override
	public void onComplete(@NonNull Task<Void> task)
	{
		if (task.isSuccessful())
		{
			Log.i(TAG, "Remote configs fetched.");

			config.activateFetched();
		}
		else
		{
			Log.i(TAG, "Remote configs not fetched.");

			//Todo: Implement a Crashlytics report here.
		}
	}

	/**
	 *
	 */
	@Override
	public void onStart()
	{
		super.onStart();

		config.fetch().addOnCompleteListener(this);

		user = auth.getCurrentUser();

		handler.postDelayed(this, TIMEOUT);
	}

	/**
	 *
	 */
	@Override
	public void run()
	{
		if (user != null)
		{
			Log.i(TAG, "User authenticated.");
			//Todo: add the re-authentication procedure.
			Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
			startActivity(intent);
			finish();
		}
		else
		{
			Log.i(TAG, "User not authenticated.");

			Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
