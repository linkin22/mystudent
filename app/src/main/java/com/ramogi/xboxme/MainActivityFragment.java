package com.ramogi.xboxme;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * @author appsrox.com
 *
 */
public class MainActivityFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private SimpleCursorAdapter adapter;
	private GoogleAccountCredential credential;
	private String displayname;
	private String email;
	private SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		//Bundle bundle = getIntent().getExtras();

		//email = bundle.getString("emailadd");
		//displayname = bundle.getString("name");



		
		adapter = new SimpleCursorAdapter(getActivity(),
				R.layout.main_list_item,
				null, 
				new String[]{DataProvider.COL_NAME, DataProvider.COL_COUNT},
				new int[]{R.id.text1, R.id.text2},
				0);
		
		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				switch(view.getId()) {
				case R.id.text2:
					int count = cursor.getInt(columnIndex);
					if (count > 0) {
						((TextView)view).setText(String.format("%d new message%s", count, count==1 ? "" : "s"));
					}
					return true;					
				}
				return false;
			}
		});
		
		setListAdapter(adapter);
		
		//ActionBar actionBar = getActionBar();
		//actionBar.setDisplayShowTitleEnabled(false);
		
/*		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(this, R.array.dropdown_arr, android.R.layout.simple_list_item_1);
		actionBar.setListNavigationCallbacks(dropdownAdapter, new ActionBar.OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				getLoaderManager().restartLoader(0, getArgs(itemPosition), MainActivityOriginal.this);
				return true;
			}
		});*/
		
		getLoaderManager().initLoader(0, null, this);
	}

    public MainActivityFragment(){

    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

		return inflater.inflate(com.android.internal.R.layout.list_content_simple,
				container, false);

    }
    */

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.maintwo, menu);
		//return true;
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.maintwo, menu);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			AddContactDialog dialog = new AddContactDialog();
            Log.v("dialog called  ", dialog.toString());
			dialog.show(getFragmentManager(), "AddContactDialog");
			return true;
			
		case R.id.action_settings:
			Intent intent = new Intent(getActivity(), SettingsActivity.class);
			startActivity(intent);
			return true;			
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		//intent.putExtra("emailadd",email);
		//intent.putExtra("name", displayname);
		intent.putExtra(Common.PROFILE_ID, String.valueOf(id));
		startActivity(intent);
	}	
	
	//----------------------------------------------------------------------------

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(getActivity(),
				DataProvider.CONTENT_URI_PROFILE, 
				new String[]{DataProvider.COL_ID, DataProvider.COL_NAME, DataProvider.COL_COUNT}, 
				null, 
				null, 
				DataProvider.COL_ID + " DESC");

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}	

}
