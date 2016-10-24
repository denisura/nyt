package com.github.denisura.nytseacher.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.databinding.ActivityFragmentBinding;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    public Fragment mActivityFragment;
    // Store the binding
    protected ActivityFragmentBinding binding;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getLayoutResId());
        setSupportActionBar(binding.toolbar);

        FragmentManager fm = getSupportFragmentManager();
        mActivityFragment = fm.findFragmentById(R.id.fragment_container);

        if (mActivityFragment == null) {
            mActivityFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mActivityFragment)
                    .commit();
        }
    }
}