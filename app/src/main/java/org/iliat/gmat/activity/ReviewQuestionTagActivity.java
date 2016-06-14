package org.iliat.gmat.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.iliat.gmat.R;
import org.iliat.gmat.adapter.SummaryTagAdapter;

public class ReviewQuestionTagActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private SummaryTagAdapter summaryTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_question_tag);

        //init components
        init();

        //add toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Review By Tag");

        //add animation
        overridePendingTransition(R.anim.trans_in,R.anim.trans_out);

        //
        setIconForTab();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindDataToViewPager();
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
        toolbar = (Toolbar) findViewById(R.id.tool_bar_tag);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void setIconForTab() {
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.grey));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.green));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.yellow));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.red));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_star_black_24dp));
    }

    private void bindDataToViewPager(){
        summaryTagAdapter=new SummaryTagAdapter(getSupportFragmentManager());
        viewPager.setAdapter(summaryTagAdapter);
    }

    private void addListener(){
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in,R.anim.trans_back_out);
    }
}
