package com.example.pancho.accountmanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pancho.accountmanager.model.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private String TAG = this.getClass().getSimpleName();
    private AccountManager mAccountManager;
    @SuppressWarnings("rawtypes")
    private List<Item> list = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter;
    public static final String DEMO_ACCOUNT_NAME = "Demo Account";
    public static final String DEMO_ACCOUNT_PASSWORD = "Demo123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAccountManager = AccountManager.get(this);

        initRecycler();

    }

    private void initRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        recycler.setItemViewCacheSize(20);
        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Item> getData() {
        ArrayList<Item> accountsList = new ArrayList<Item>();

        // Getting all registered Our Application Accounts;
        try {
            Account[] accounts = AccountManager.get(this).getAccountsByType(getString(R.string.auth_type));
            for (Account account : accounts) {
                Item item = new Item(account.type, account.name);
                accountsList.add(item);
            }
        } catch (Exception e) {
            Log.i(TAG, "Exception:" + e);
        }

        // For all registered accounts;
        /*
         * try { Account[] accounts = AccountManager.get(this).getAccounts();
		 * for (Account account : accounts) { Item item = new Item(
		 * account.type, account.name); accountsList.add(item); } } catch
		 * (Exception e) { Log.i("Exception", "Exception:" + e); }
		 */
        return accountsList;
    }

    @OnClick({R.id.btnCreate, R.id.btnShow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:
                Intent createIntent = new Intent(this, AuthenticatorActivity.class);
                startActivity(createIntent);
                break;
            case R.id.btnShow:
                list = getData();
                recyclerAdapter = new RecyclerAdapter(list);
                recycler.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

                break;
        }
    }

    void createDemoAccount() {
        Account account = new Account(DEMO_ACCOUNT_NAME, getString(R.string.auth_type));
        boolean accountCreated = mAccountManager.addAccountExplicitly(account, DEMO_ACCOUNT_PASSWORD, null);
        if (accountCreated) {
            showMessage("Account Created");
        }
    }
}
