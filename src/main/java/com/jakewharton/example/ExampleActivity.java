package com.jakewharton.example;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.example.adjacent.R;

public class ExampleActivity extends FragmentActivity {
  private static final String TAG = "XXXXXXX";
  private static final String TAG_ONE = "one";
  private static final String TAG_TWO = "two";
  private static final int COLOR_ONE = 0xFF00FF00;
  private static final int COLOR_TWO = 0xFF0000FF;
  private static final String KEY_PAGE = "selected_page";
  private static final int colors[] = { COLOR_ONE, COLOR_TWO };

  private ViewPager pager;
  private int lastPage; // If landscape, page from portrait.

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState != null) {
      lastPage = savedInstanceState.getInt(KEY_PAGE, 0);
    }

    setContentView(R.layout.example_activity);

    FragmentManager fragmentManager = getSupportFragmentManager();

    Fragment one = fragmentManager.findFragmentByTag(TAG_ONE);
    Fragment two = fragmentManager.findFragmentByTag(TAG_TWO);

    pager = (ViewPager) findViewById(R.id.pager);
    if (pager != null) {
      pager.setAdapter(new TwoFragmentAdapter(fragmentManager,
                       getResources().getConfiguration().orientation));
      pager.setCurrentItem(lastPage);
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (pager != null) {
      outState.putInt(KEY_PAGE, pager.getCurrentItem());
    } else {
      outState.putInt(KEY_PAGE, lastPage);
    }
  }

  private static class TwoFragmentAdapter extends FragmentPagerAdapter {
  int orientation;
    public TwoFragmentAdapter(FragmentManager fm, int orientation) {
      super(fm);
      this.orientation = orientation;
    }

    @Override
    public Fragment getItem(int arg0) {
      return ColorFragment.newInstance(colors[arg0]);
    }

    @Override
    public int getCount() {
      return 2;
    }

    @Override
    public float getPageWidth(int position) {
      if (orientation == Configuration.ORIENTATION_PORTRAIT)
        return 1f;
      else
        return 0.5f;
    }
  }

  public static class ColorFragment extends Fragment {
    private static final String KEY_COLOR = "color";
    private static final int DEFAULT_COLOR = 0xFFFF0000;

    public static ColorFragment newInstance(int color) {
      Bundle arguments = new Bundle();
      arguments.putInt(KEY_COLOR, color);

      ColorFragment fragment = new ColorFragment();
      fragment.setArguments(arguments);
      return fragment;
    }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int color = getArguments().getInt(KEY_COLOR);
    Log.d(getClass().getName(), "Creating fragment: " + color);
    setRetainInstance(true);
  }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      Bundle arguments = getArguments();
      int color = arguments.getInt(KEY_COLOR, DEFAULT_COLOR);

      ImageView iv = new ImageView(getActivity());
      iv.setImageDrawable(new ColorDrawable(color));
      return iv;
    }
  }
}
