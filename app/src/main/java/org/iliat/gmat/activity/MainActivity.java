package org.iliat.gmat.activity;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.iliat.gmat.R;
import org.iliat.gmat.dialog.DownloadImageDialog;
import org.iliat.gmat.fragment.HomeFragment;
import org.iliat.gmat.fragment.QuestionPackFragment;
import org.iliat.gmat.interf.OnDownloadFinished;
import org.iliat.gmat.interf.ScreenManager;
import org.iliat.gmat.utils.QuestionHelper;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ScreenManager, FragmentManager.OnBackStackChangedListener {

    FragmentManager mFragmentManager;
    QuestionPackFragment questionPackFragment;
    HomeFragment homeFragment;
    ActionBarDrawerToggle toggle;

    public void goToActivity(Class activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        getApplicationContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                syncState();
            }
        };
        if(drawer != null){
            drawer.setDrawerListener(toggle);
        }

        toggle.syncState();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null && toolbar != null){
            navigationView.setNavigationItemSelectedListener(this);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getFragmentManager().getBackStackEntryCount() > 1){
                        getFragmentManager().popBackStack();
                    } else {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.START);
                    }

                }
            });
        }
        getIntances();
        getFragmentManager().addOnBackStackChangedListener(this);
        homeFragment = new HomeFragment();
        openFragment(homeFragment, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void syncActionBarArrowState() {
        int backStackEntryCount =
                getFragmentManager().getBackStackEntryCount();
        Log.d("getBackStackEntryCount", ""+backStackEntryCount);
        toggle.setDrawerIndicatorEnabled(backStackEntryCount == 1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null){
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    }


    private void updateQuestion() {
        QuestionHelper questionHelper = new QuestionHelper();
        questionHelper.setOnDownloadFinished(new OnDownloadFinished() {
            @Override
            public void downloadFinish() {
                //reload fragment
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.detach(questionPackFragment).attach(questionPackFragment).commit();
            }
        });
        questionHelper.downloadQuestionInServer();
        showDialogFragment(new DownloadImageDialog(), "DOWNLOAD_IMAGE_DIALOG");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("asdd","sssss11");
        if (toggle.isDrawerIndicatorEnabled() &&
                toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == android.R.id.home && getFragmentManager().getBackStackEntryCount()>1) {
            getFragmentManager().popBackStack();
            Log.d("asdd","sssss");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement
    }

    private void getIntances() {
        mFragmentManager = getFragmentManager();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_general) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            updateQuestion();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null){
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out);
        fragmentTransaction.replace(R.id.view_fragment, fragment).addToBackStack(fragment.getClass().getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment, String tag) {
        dialogFragment.show(mFragmentManager, tag);
    }

    @Override
    public boolean back() {
        if (mFragmentManager.getBackStackEntryCount() > 1) {
            Log.d("HungTD",""+mFragmentManager.getBackStackEntryCount());
            mFragmentManager.popBackStack();
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void setTitleOfActionBar(String titles) {
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titles);
        }
    }

    @Override
    public void onBackStackChanged() {
        syncActionBarArrowState();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getFragmentManager().popBackStack();
        return true;
    }
}
