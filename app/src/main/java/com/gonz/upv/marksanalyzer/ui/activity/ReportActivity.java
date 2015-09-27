package com.gonz.upv.marksanalyzer.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.gonz.common.Tuple;
import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.adapter.PageAdapter;
import com.gonz.upv.marksanalyzer.ui.fragment.AboutDialogFragment;
import com.gonz.upv.marksanalyzer.ui.fragment.ListFragment;
import com.gonz.upv.marksanalyzer.ui.listener.SearchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

	private boolean searchActive = false;
	private MenuItem searchAction;
	private MenuItem settingsAction;
	private AutoCompleteTextView textEdit = null;
	private ViewPager viewPager;

	private Menu menu;

	private HashMap<String, Float> map;
	private ArrayList<Tuple> sortedList;

	private String dni, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		// Get data references
		Bundle b = getIntent().getExtras();
		map = (HashMap<String, Float>) b.getSerializable("map");
		sortedList = (ArrayList<Tuple>) b.getSerializable("sortedList");
		dni = b.getString("dni");
		pass = b.getString("pass");

		// Set toolbar, tabs and ViewPager
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1_title));
		tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2_title));
		tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3_title));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		this.viewPager = (ViewPager) findViewById(R.id.pager);
		final PageAdapter adapter = new PageAdapter(
				getSupportFragmentManager(), tabLayout.getTabCount(), getIntent().getExtras());
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

			@Override
			public void onTabSelected(TabLayout.Tab tab) {

				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		viewPager.setCurrentItem(1);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setLanguaje();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		menu.clear();
		getMenuInflater().inflate(R.menu.menu_report, menu);
		searchAction = menu.findItem(R.id.action_search);
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

			case R.id.action_search:
				if(searchActive)
					closeSearch();
				else
					openSearch();
				return true;

			case R.id.action_about:
				new AboutDialogFragment().show(getFragmentManager(), "ABOUT DIALOG");
				return true;

			case R.id.action_settings:
				Bundle b = new Bundle();
				b.putString("dni", dni);
				b.putString("pass", pass);
				startActivity(new Intent(this, SettingsActivity.class).putExtras(b));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openSearch() {

		// Set custom view on action bar.
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.search_bar);
		actionBar.setTitle("");

		// Set AutocompleteTextView listener, adapter and hint.
		textEdit = (AutoCompleteTextView) actionBar.getCustomView()
				.findViewById(R.id.searchTextView);
		textEdit.setOnItemClickListener(new SearchListener(this));
		String[] list = new String[sortedList.size()]; int i = 0;
		for (Tuple t : sortedList)
			list[i++] = t.getName();
		ArrayAdapter<String> arr = new ArrayAdapter<String>(this,
				R.layout.support_simple_spinner_dropdown_item, list);
		textEdit.setAdapter(arr);

		// Search edit text field setup.
		EditText textEdit = (EditText) actionBar.getCustomView().findViewById(R.id.searchTextView);
		textEdit.setHint(getIntent().getExtras().getString("name"));
		textEdit.requestFocus();

		// Change search icon accordingly.
		searchAction.setIcon(getResources().getDrawable(R.mipmap.ic_action_clear));
		searchActive = true;
	}

	private void closeSearch() {

		// Remove custom view.
		getSupportActionBar().setDisplayShowCustomEnabled(false);
		getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

		clearHighlight();

		// Change search icon accordingly.
		searchAction.setIcon(getResources().getDrawable(R.mipmap.ic_action_search));
		searchActive = false;

	}

	public void highlight(String name) {
		viewPager.setCurrentItem(2);
		for(Fragment f : getSupportFragmentManager().getFragments())
			if (f instanceof ListFragment) {
				ListFragment lf = (ListFragment) f;
				lf.highlight(name);
				break;
			}
	}

	public void clearHighlight() {
		for(Fragment f : getSupportFragmentManager().getFragments())
			if (f instanceof ListFragment) {
				ListFragment lf = (ListFragment) f;
				lf.clearHighlight();
				break;
			}
	}

	private void setLanguaje() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (preferences.contains(MainActivity.KEY_LANGUAJE)) {
			String localeCode = preferences.getString(MainActivity.KEY_LANGUAJE, "es");
			Locale locale = new Locale(localeCode);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
		}
	}

}
