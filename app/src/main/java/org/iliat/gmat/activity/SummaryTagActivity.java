package org.iliat.gmat.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.SummaryTagAdapter;

/**
 * Created by MrBom on 6/11/2016.
 */
public class SummaryTagActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayout.Tab tab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_question_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_score);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Summary By tag");
        init();

        //start animation
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        tabLayout = (TabLayout) findViewById(R.id.sliding_tab);
        viewPager = (ViewPager) findViewById(R.id.view_pager_tag);
    }

    private void bindDataTotopView() {
        tab = tabLayout.newTab();
        tab.setIcon(R.mipmap.grey);
        tabLayout.addTab(tab);

        tab.setIcon(R.mipmap.green);
        tabLayout.addTab(tab);

        tab.setIcon(R.mipmap.yellow);
        tabLayout.addTab(tab);

        tab.setIcon(R.mipmap.red);
        tabLayout.addTab(tab);

        tab.setIcon(R.mipmap.ic_star_black_24dp);
        tabLayout.addTab(tab);

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        SummaryTagAdapter adapter = new SummaryTagAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }
}
