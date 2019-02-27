package com.sc.coffeeprince.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> listFragment;
	private Fragment currentFragment;

	public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> listFragment) {
		super(fragmentManager);
		this.listFragment = listFragment;
	}

	@Override
	public Fragment getItem(int position) {
		return listFragment.get(position);
	}

	@Override
	public int getCount() {
		return listFragment.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((Fragment) object);
		}
		super.setPrimaryItem(container, position, object);
	}

	public Fragment getCurrentFragment() {
		return currentFragment;
	}
}