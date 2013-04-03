package com.jakewharton.example;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
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

    FragmentTransaction remove = fragmentManager.beginTransaction();
    if (one == null) {
      one = ColorFragment.newInstance(COLOR_ONE);
      Log.d(TAG, "Creating new fragment one.");
    } else {
      remove.remove(one);
      Log.d(TAG, "Found existing fragment one.");
    }
    if (two == null) {
      two = ColorFragment.newInstance(COLOR_TWO);
      Log.d(TAG, "Creating new fragment two.");
    } else {
      remove.remove(two);
      Log.d(TAG, "Found existing fragment two.");
    }
    if (!remove.isEmpty()) {
      remove.commit();
      fragmentManager.executePendingTransactions();
    }

    pager = (ViewPager) findViewById(R.id.pager);
    if (pager != null) {
      pager.setAdapter(new TwoFragmentAdapter(fragmentManager, one, two));
      pager.setCurrentItem(lastPage);
    } else {
      fragmentManager.beginTransaction()
          .add(R.id.left_pane, one, TAG_ONE)
          .add(R.id.right_pane, two, TAG_TWO)
          .commit();
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

  private static class TwoFragmentAdapter extends PagerAdapter {
    private final FragmentManager fragmentManager;
    private final Fragment one;
    private final Fragment two;
    private FragmentTransaction currentTransaction = null;
    private Fragment currentPrimaryItem = null;

    public TwoFragmentAdapter(FragmentManager fragmentManager, Fragment one, Fragment two) {
      this.fragmentManager = fragmentManager;
      this.one = one;
      this.two = two;
    }

    @Override public int getCount() {
      return 2;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      if (currentTransaction == null) {
        currentTransaction = fragmentManager.beginTransaction();
      }

      String tag = (position == 0) ? TAG_ONE : TAG_TWO;
      Fragment fragment = (position == 0) ? one : two;
      currentTransaction.add(container.getId(), fragment, tag);
      if (fragment != currentPrimaryItem) {
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
      }

      return fragment;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      // With two pages, fragments should never be destroyed.
      throw new AssertionError();
    }

    @Override public void setPrimaryItem(ViewGroup container, int position, Object object) {
      Fragment fragment = (Fragment) object;
      if (fragment != currentPrimaryItem) {
        if (currentPrimaryItem != null) {
          currentPrimaryItem.setMenuVisibility(false);
          currentPrimaryItem.setUserVisibleHint(false);
        }
        if (fragment != null) {
          fragment.setMenuVisibility(true);
          fragment.setUserVisibleHint(true);
        }
        currentPrimaryItem = fragment;
      }
    }

    @Override public void finishUpdate(ViewGroup container) {
      if (currentTransaction != null) {
        currentTransaction.commitAllowingStateLoss();
        currentTransaction = null;
        fragmentManager.executePendingTransactions();
      }
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return ((Fragment) object).getView() == view;
    }
  }

  public static class ColorFragment extends Fragment {
    private static final String KEY_COLOR = "color";
    public static final int DEFAULT_COLOR = 0xFFFF0000;

    public static ColorFragment newInstance(int color) {
      Bundle arguments = new Bundle();
      arguments.putInt(KEY_COLOR, color);

      ColorFragment fragment = new ColorFragment();
      fragment.setArguments(arguments);
      return fragment;
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
