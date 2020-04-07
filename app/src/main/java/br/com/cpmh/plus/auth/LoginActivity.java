package br.com.cpmh.plus.auth;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import br.com.cpmh.plus.R;
import br.com.cpmh.plus.ViewPagerAdapter;

/**
 *
 */
public class LoginActivity extends AppCompatActivity
{
	private static final String TAG = "LoginActivity";
	private ViewPager viewPager;

	/**
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ViewPagerAdapter viewPagerAdapter;
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPagerAdapter.addFragment(new SignInFragment());

		viewPager = findViewById(R.id.view_pager);
		viewPager.setAdapter(viewPagerAdapter);
	}

	/**
	 * @param index
	 */
	public void setPage(int index)
	{
		viewPager.setCurrentItem(index);
	}
}
