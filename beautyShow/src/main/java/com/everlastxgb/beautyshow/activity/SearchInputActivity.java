package com.everlastxgb.beautyshow.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.adapter.SearchHistoryAdapter;
import com.everlastxgb.beautyshow.common.CommonMethods;
import com.everlastxgb.beautyshow.common.UIHelper;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.model.FilterConditionModel;
import com.everlastxgb.beautyshow.widget.RangeSeekBar;
import com.everlastxgb.beautyshow.widget.RangeSeekBar.OnRangeSeekBarChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-21 PM 3:16:11
 */
public class SearchInputActivity extends BaseActivity implements OnClickListener, OnRangeSeekBarChangeListener<Number> {
	// Content View Elements

	private View searchHeader;
	private TextView searchTitle;
	private ListView searchHistoryListView;
	private Button clearHistoryButton;

	// condition layout
	private Spinner countrySpinner, professionSpinner, xingzuoSpinner, bloodtypeSpinner;

	private RangeSeekBar<Number> ageSeekBar, heightSeekBar, chestSeekBar, waistSeekBar, hipSeekBar, cupSeekBar;
	private TextView ageTextView, heightTextView, chestTextView, waistTextView, hipTextView, cupTextView;
	private View selectView, resetView;

	// End Of Content View Elements

	private void bindViews() {
		searchHeader = findViewById(R.id.search_input_header_layout);
		searchTitle = (TextView) findViewById(R.id.search_input_header_title);
		searchHistoryListView = (ListView) findViewById(R.id.searchHistoryListView);
		clearHistoryButton = (Button) findViewById(R.id.clearHistoryButton);

		selectView = findViewById(R.id.selectButton);
		resetView = findViewById(R.id.resetButton);

		countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
		professionSpinner = (Spinner) findViewById(R.id.professionSpinner);
		xingzuoSpinner = (Spinner) findViewById(R.id.xingzuoSpinner);
		bloodtypeSpinner = (Spinner) findViewById(R.id.bloodtypeSpinner);

		ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, filterConditionModel.getCountry().toStringList());
		ArrayAdapter<String> professionAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, filterConditionModel.getProfession().toStringList());
		ArrayAdapter<String> xingzuoAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, filterConditionModel.getXingzuo().toStringList());
		ArrayAdapter<String> bloodtypeAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_item_1line, filterConditionModel.getBloodtype().toStringList());

		countrySpinner.setAdapter(countryAdapter);
		professionSpinner.setAdapter(professionAdapter);
		xingzuoSpinner.setAdapter(xingzuoAdapter);
		bloodtypeSpinner.setAdapter(bloodtypeAdapter);

		ageTextView = (TextView) findViewById(R.id.ageTextView);
		heightTextView = (TextView) findViewById(R.id.heightTextView);
		chestTextView = (TextView) findViewById(R.id.chestTextView);
		waistTextView = (TextView) findViewById(R.id.waistTextView);
		hipTextView = (TextView) findViewById(R.id.hipTextView);
		cupTextView = (TextView) findViewById(R.id.cupTextView);

		ageSeekBar = new RangeSeekBar<Number>(10, 100, this);
		ageSeekBar.setOnRangeSeekBarChangeListener(this);
		((ViewGroup) findViewById(R.id.ageLayout)).addView(ageSeekBar);

		heightSeekBar = new RangeSeekBar<Number>(45, 200, this);
		heightSeekBar.setOnRangeSeekBarChangeListener(this);
		((ViewGroup) findViewById(R.id.heightLayout)).addView(heightSeekBar);

		chestSeekBar = new RangeSeekBar<Number>(70, 150, this);
		chestSeekBar.setOnRangeSeekBarChangeListener(this);
		((ViewGroup) findViewById(R.id.chestLayout)).addView(chestSeekBar);

		waistSeekBar = new RangeSeekBar<Number>(40, 100, this);
		waistSeekBar.setOnRangeSeekBarChangeListener(this);
		((ViewGroup) findViewById(R.id.waistLayout)).addView(waistSeekBar);

		hipSeekBar = new RangeSeekBar<Number>(80, 150, this);
		hipSeekBar.setOnRangeSeekBarChangeListener(this);
		((ViewGroup) findViewById(R.id.hipLayout)).addView(hipSeekBar);

		cupSeekBar = new RangeSeekBar<Number>(0, alphet.length - 1, this);
		cupSeekBar.setOnRangeSeekBarChangeListener(this);
		((ViewGroup) findViewById(R.id.cupLayout)).addView(cupSeekBar);

		// resetAllSeekBarSpinner();

		titlebarEditText.setVisibility(View.VISIBLE);
		// titlebarEditText.setFocusable(true);
		titlebarEditText.requestFocus();
		titlebarEditText.setHint(R.string.name_or_pinyin);

		titlebarRightView.setVisibility(View.VISIBLE);
		titlebarRightView.setImageResource(R.drawable.selector_search);
	}

	private void bindClicks() {
		searchHeader.setOnClickListener(this);
		clearHistoryButton.setOnClickListener(this);
		titlebarRightView.setOnClickListener(this);

		selectView.setOnClickListener(this);
		resetView.setOnClickListener(this);
	}

	private SharedPreferences searchPreferences;
	private SharedPreferences filterPreferences;
	private ProgressDialog progressDialog;

	private SearchHistoryAdapter searchHistoryAdapter;

	private HashMap<String, Long> keywordMap = new HashMap<String, Long>();
	private ArrayList<String> keywordList = new ArrayList<String>();
	private static final int MAX_STORE_COUNT = 10;

	private FilterConditionModel filterConditionModel = new FilterConditionModel();

	private final static String FILE_SEARCH_HISTORY = "search_history";
	private final static String FILE_FILTER_CONFIG = "filter_config";

	private final static String KEY_C = "KEY_C";
	private final static String KEY_P = "KEY_P";
	private final static String KEY_X = "KEY_X";
	private final static String KEY_B = "KEY_B";

	private final static String KEY_AGE_1 = "KEY_AGE_1";
	private final static String KEY_AGE_2 = "KEY_AGE_2";

	private final static String KEY_HEIGHT_1 = "KEY_HEIGHT_1";
	private final static String KEY_HEIGHT_2 = "KEY_HEIGHT_2";

	private final static String KEY_CHEST_1 = "KEY_CHEST_1";
	private final static String KEY_CHEST_2 = "KEY_CHEST_2";

	private final static String KEY_WAIST_1 = "KEY_WAIST_1";
	private final static String KEY_WAIST_2 = "KEY_WAIST_2";

	private final static String KEY_HIP_1 = "KEY_HIP_1";
	private final static String KEY_HIP_2 = "KEY_HIP_2";

	private final static String KEY_CUP_1 = "KEY_CUP_1";
	private final static String KEY_CUP_2 = "KEY_CUP_2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_input);

		Object object = getIntent().getSerializableExtra(KEY_MODEL);
		if (object instanceof FilterConditionModel) {
			filterConditionModel = (FilterConditionModel) object;
		}
		int conditionVisi = filterConditionModel.getCountry().getList().size() == 0 ? View.GONE : View.VISIBLE;
		findViewById(R.id.conditionLayout).setVisibility(conditionVisi);

		setTitle("");
		bindViews();
		bindClicks();
		searchPreferences = getSharedPreferences(FILE_SEARCH_HISTORY, Context.MODE_PRIVATE);
		filterPreferences = getSharedPreferences(FILE_FILTER_CONFIG, Context.MODE_PRIVATE);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.loading));
		progressDialog.setCancelable(false);

		readLastFilter();
		readLastSearch();
		initSearchListView();
		// CommonMethods.setListViewHeightBasedOnChildren(searchHistoryListView);
		titlebarEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String content = s + "";
				searchTitle.setText(getString(R.string.search_auto_tip_format, content));
				int visibility = content.length() > 0 ? View.VISIBLE : View.GONE;
				searchHeader.setVisibility(visibility);
				titlebarRightView.setVisibility(visibility);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		titlebarEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 先隐藏键盘
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(self().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					searchHeader.performClick();

					return true;
				}
				return false;
			}
		});

		titlebarEditText.setText("");
		notifyMyData();
		CommonMethods.hideKeyboard(this);
	}

	private void initSearchListView() {
		searchHistoryAdapter = new SearchHistoryAdapter(this, keywordList);
		searchHistoryListView.setSelector(android.R.color.transparent);
		searchHistoryListView.setAdapter(searchHistoryAdapter);

		searchHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startSearching(keywordList.get(position));
			}
		});
		searchHistoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int index = position;
				AlertDialog.Builder builder = new Builder(SearchInputActivity.this);
				String[] items = { getString(R.string.delete) };
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == 0) {
							removeHistory(keywordList.get(index));
							searchHistoryAdapter.notifyDataSetChanged();
						}
						arg0.dismiss();
					}
				});
				builder.show();
				return true;
			}
		});
	}

	private void readLastSearch() {
		try {
			keywordMap = (HashMap<String, Long>) searchPreferences.getAll();
		} catch (Exception e) {
			searchPreferences.edit().clear().commit();
		}
		keywordList = new ArrayList<String>(keywordMap.keySet());
		Collections.sort(keywordList, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return (int) (keywordMap.get(o1) - keywordMap.get(o2));
			}
		});
		if (keywordList.size() > MAX_STORE_COUNT) {
			keywordList = (ArrayList<String>) keywordList.subList(0, MAX_STORE_COUNT - 1);
		}
	}

	private void storeSearch(String keyword) {
		if (keywordMap.containsKey(keyword) || keywordList.size() < MAX_STORE_COUNT) {
		} else {
			String lastKey = keywordList.get(keywordList.size() - 1);
			removeHistory(lastKey);
		}
		addHistory(keyword);
	}

	private void removeHistory(String keyword) {
		keywordMap.remove(keyword);
		keywordList.remove(keyword);
		searchPreferences.edit().remove(keyword).commit();

		notifyMyData();
	}

	private void addHistory(String keyword) {
		long time = System.currentTimeMillis();
		keywordMap.put(keyword, time);
		if (keywordList.contains(keyword)) {
			keywordList.remove(keyword);
		}
		keywordList.add(0, keyword);
		searchPreferences.edit().putLong(keyword, time).commit();

		notifyMyData();
	}

	private void clearHistory() {
		searchPreferences.edit().clear().commit();
		keywordMap.clear();
		keywordList.clear();

		notifyMyData();

	}

	private void notifyMyData() {
		int visibility = keywordList.size() > 0 ? View.VISIBLE : View.GONE;
		clearHistoryButton.setVisibility(visibility);

		searchHistoryAdapter.notifyDataSetChanged();
		CommonMethods.setListViewHeightBasedOnChildren(searchHistoryListView);
	}

	private void startSearching(String keyword) {
		storeSearch(keyword);
		Intent intent = new Intent(this, SearchResultActivity.class);
		intent.putExtra(SearchResultActivity.KEY_NAME, keyword);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v == titlebarRightView || v == searchHeader) {
			String keyword = titlebarEditText.getText().toString().trim();
			if (keyword.equals("")) {
				UIHelper.showToastShort(this, getString(R.string.tips_keyword_noempty));
				return;
			}
			startSearching(keyword);
		} else if (v == clearHistoryButton) {
			clearHistory();
			searchHistoryAdapter.notifyDataSetChanged();
		} else if (v == selectView) {
			storeFilter();
			String country = filterConditionModel.getCountry().getList().get(countrySpinner.getSelectedItemPosition()).getId();
			String professional = filterConditionModel.getProfession().getList().get(professionSpinner.getSelectedItemPosition()).getId();
			String xingzuo = filterConditionModel.getXingzuo().getList().get(xingzuoSpinner.getSelectedItemPosition()).getId();
			String bloodtype = filterConditionModel.getBloodtype().getList().get(bloodtypeSpinner.getSelectedItemPosition()).getId();

			String age = ageTextView.getText().toString().split(" ")[0];
			String height = heightTextView.getText().toString().split(" ")[0];
			String chest = chestTextView.getText().toString().split(" ")[0];
			String waist = waistTextView.getText().toString().split(" ")[0];
			String hip = hipTextView.getText().toString().split(" ")[0];
			String cup = cupTextView.getText().toString().split(" ")[0];

			String findUrl = URLs.buildFindUrl(country, professional, xingzuo, bloodtype, age, height, chest, waist, hip, cup);
			Intent intent = getIntent();
			intent.putExtra(KEY_URL, findUrl);
			setResult(RESULT_OK, intent);
			finish();

		} else if (v == resetView) {
			resetAllSeekBarSpinner();
		}
	}

	private void readLastFilter() {
		if (!filterPreferences.contains(KEY_C)) {
			resetAllSeekBarSpinner();
			return;
		}

		countrySpinner.setSelection(filterPreferences.getInt(KEY_C, 0));
		professionSpinner.setSelection(filterPreferences.getInt(KEY_P, 0));
		xingzuoSpinner.setSelection(filterPreferences.getInt(KEY_X, 0));
		bloodtypeSpinner.setSelection(filterPreferences.getInt(KEY_B, 0));

		if (filterPreferences.contains(KEY_AGE_1)) {

			initSeekBar(ageSeekBar, filterPreferences.getInt(KEY_AGE_1, 0), filterPreferences.getInt(KEY_AGE_2, 0));
			initSeekBar(heightSeekBar, filterPreferences.getInt(KEY_HEIGHT_1, 0), filterPreferences.getInt(KEY_HEIGHT_2, 0));
			initSeekBar(chestSeekBar, filterPreferences.getInt(KEY_CHEST_1, 0), filterPreferences.getInt(KEY_CHEST_2, 0));
			initSeekBar(waistSeekBar, filterPreferences.getInt(KEY_WAIST_1, 0), filterPreferences.getInt(KEY_WAIST_2, 0));
			initSeekBar(hipSeekBar, filterPreferences.getInt(KEY_HIP_1, 0), filterPreferences.getInt(KEY_HIP_2, 0));
			initSeekBar(cupSeekBar, filterPreferences.getInt(KEY_CUP_1, 0), filterPreferences.getInt(KEY_CUP_2, 0));

		}
	}

	private void initSeekBar(RangeSeekBar<Number> bar, int min, int max) {
		bar.setSelectedMinValue(min);
		bar.setSelectedMaxValue(max);
		onRangeSeekBarValuesChanged(bar, min, max);
	}

	private void storeFilter() {

		Editor editor = filterPreferences.edit();
		editor.putInt(KEY_C, countrySpinner.getSelectedItemPosition());
		editor.putInt(KEY_P, professionSpinner.getSelectedItemPosition());
		editor.putInt(KEY_X, xingzuoSpinner.getSelectedItemPosition());
		editor.putInt(KEY_B, bloodtypeSpinner.getSelectedItemPosition());

		editor.putInt(KEY_AGE_1, ageSeekBar.getSelectedMinValue().intValue());
		editor.putInt(KEY_AGE_2, ageSeekBar.getSelectedMaxValue().intValue());

		editor.putInt(KEY_HEIGHT_1, heightSeekBar.getSelectedMinValue().intValue());
		editor.putInt(KEY_HEIGHT_2, heightSeekBar.getSelectedMaxValue().intValue());

		editor.putInt(KEY_CHEST_1, chestSeekBar.getSelectedMinValue().intValue());
		editor.putInt(KEY_CHEST_2, chestSeekBar.getSelectedMaxValue().intValue());

		editor.putInt(KEY_WAIST_1, waistSeekBar.getSelectedMinValue().intValue());
		editor.putInt(KEY_WAIST_2, waistSeekBar.getSelectedMaxValue().intValue());

		editor.putInt(KEY_HIP_1, hipSeekBar.getSelectedMinValue().intValue());
		editor.putInt(KEY_HIP_2, hipSeekBar.getSelectedMaxValue().intValue());

		editor.putInt(KEY_CUP_1, cupSeekBar.getSelectedMinValue().intValue());
		editor.putInt(KEY_CUP_2, cupSeekBar.getSelectedMaxValue().intValue());

		editor.commit();

	}

	private void resetAllSeekBarSpinner() {
		resetSeekBar(ageSeekBar);
		resetSeekBar(heightSeekBar);
		resetSeekBar(chestSeekBar);
		resetSeekBar(waistSeekBar);
		resetSeekBar(hipSeekBar);
		resetSeekBar(cupSeekBar);

		resetSpinner(countrySpinner);
		resetSpinner(professionSpinner);
		resetSpinner(xingzuoSpinner);
		resetSpinner(bloodtypeSpinner);

		filterPreferences.edit().clear().commit();

	}

	private void resetSeekBar(RangeSeekBar<Number> seekBar) {
		initSeekBar(seekBar, seekBar.getAbsoluteMinValue().intValue(), seekBar.getAbsoluteMaxValue().intValue());
	}

	private void resetSpinner(Spinner spinner) {
		spinner.setSelection(0);
	}

	static final char[] alphet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P' };

	@Override
	public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Number minValue, Number maxValue) {
		String seekTip = "";
		if (bar == ageSeekBar) {
			seekTip = getString(R.string.format_range_sui, minValue, maxValue);
			ageTextView.setText(seekTip);
		} else if (bar == heightSeekBar) {
			seekTip = getString(R.string.format_range_cm, minValue, maxValue);
			heightTextView.setText(seekTip);
		} else if (bar == chestSeekBar) {
			seekTip = getString(R.string.format_range_cm, minValue, maxValue);
			chestTextView.setText(seekTip);
		} else if (bar == waistSeekBar) {
			seekTip = getString(R.string.format_range_cm, minValue, maxValue);
			waistTextView.setText(seekTip);
		} else if (bar == hipSeekBar) {
			seekTip = getString(R.string.format_range_cm, minValue, maxValue);
			hipTextView.setText(seekTip);
		} else if (bar == cupSeekBar) {
			char minCup = alphet[minValue.intValue()];
			char maxCup = alphet[maxValue.intValue()];
			seekTip = getString(R.string.format_range, minCup, maxCup);
			cupTextView.setText(seekTip);
		}
	}
	// private RangeSeekBar<Number> ageSeekBar, heightSeekBar, chestSeekBar,
	// waistSeekBar, hipSeekBar, cupSeekBar;
}
