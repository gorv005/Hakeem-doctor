package com.app.hakeem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.app.hakeem.fragment.FragmentSplash;
import com.app.hakeem.util.C;

public class ActivityContainer extends AppCompatActivity {

    private Fragment fragment;
    private TextView tvTitle;
    private Bundle bundle;
    private int fragmentAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        bundle = getIntent().getBundleExtra(C.BUNDLE);
        fragmentAction = getIntent().getIntExtra(C.FRAGMENT_ACTION, C.FRAGMENT_SPLASH);
        fragmnetLoader(fragmentAction, bundle);

    }

    public void fragmnetLoader(int fragmentType, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmentType) {
            case C.FRAGMENT_SPLASH:
                getSupportActionBar().hide();
                fragment = new FragmentSplash();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_SPLASH);
                break;

        }
        fragment.setArguments(bundle);

        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();


    }


    @Override
    public void onBackPressed() {


        getSupportFragmentManager().executePendingTransactions();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

//            Log.e("CountPop", getSupportFragmentManager().getBackStackEntryCount() + "");

            int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(fragmentCount - 2);
            String fragmentTag = backEntry.getName();

            getSupportFragmentManager().popBackStack();

            getSupportFragmentManager().executePendingTransactions();
            if (fragmentTag != null) {
                switch (fragmentTag) {
//                    case C.TAG_FRAGMENT_BOOK_A_FLIGHT:
//                        tvTitle.setText(C.TAG_FRAGMENT_BOOK_A_FLIGHT);
////                        tvSubTitle.setVisibility(View.GONE);
////                        tvTitle2.setVisibility(View.GONE);
//                        break;
//                    case C.TAG_FRAGMENT_SEARCH_FLIGHT:
//                        tvTitle.setText(C.TAG_FRAGMENT_SEARCH_FLIGHT);
//                        break;
//                    case C.TAG_FRAGMENT_OURDESTINATIONS:
//                        tvTitle.setText(getResources().getString(R.string.our_destinations));
//                        break;
//                    case C.TAG_FRAGMENT_MY_ACCOUNT:
//                        tvTitle.setText(getResources().getString(R.string.my_account));
//                        break;
                }
            }


        } else {
            finish();
        }
    }


}
