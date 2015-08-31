package com.gonz.upv.marksanalyzer.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.gonz.common.Tuple;
import com.gonz.upv.marksanalyzer.R;
import com.gonz.upv.marksanalyzer.ui.adapter.PageAdapter;
import com.gonz.upv.marksanalyzer.ui.fragment.ListFragment;
import com.gonz.upv.marksanalyzer.ui.listener.SearchListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {

	private boolean searchActive = false;
	private MenuItem searchAction;
	private AutoCompleteTextView textEdit = null;
	private ViewPager viewPager;

	private HashMap<String, Float> map;
	private ArrayList<Tuple> sortedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		// Get data references
		Bundle b = getIntent().getExtras();
		map = (HashMap<String, Float>) b.getSerializable("map");
		sortedList = (ArrayList<Tuple>) b.getSerializable("sortedList");

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_report, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		searchAction = menu.findItem(R.id.action_search);
		return super.onPrepareOptionsMenu(menu);
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
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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
		searchAction.setIcon(getResources().getDrawable(R.mipmap.ic_close));
		searchActive = true;
	}

	private void closeSearch() {

		// Remove custom view.
		getSupportActionBar().setDisplayShowCustomEnabled(false);
		getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

		clearHighlight();

		// Change search icon accordingly.
		searchAction.setIcon(getResources().getDrawable(R.mipmap.ic_search));
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
}
