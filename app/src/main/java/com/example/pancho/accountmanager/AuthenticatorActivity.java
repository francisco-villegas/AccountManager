package com.example.pancho.accountmanager;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.pancho.accountmanager.util.CONSTANTS.*;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private static final String TAG = "AuthenticatorActivity";

    @BindView(R.id.accountName)
    EditText accountName;
    @BindView(R.id.accountPassword)
    EditText accountPassword;

    private AccountManager mAccountManager;
    private String mAuthTokenType;
    String authtoken = "123456789"; // this
    String password = "12345";

    String accountNameS;

    public Account findAccount(String accountNameS) {
        for (Account account : mAccountManager.getAccounts())
            if (TextUtils.equals(account.name, accountNameS) && TextUtils.equals(account.type, getString(R.string.auth_type))) {
                System.out.println("FOUND");
                return account;
            }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);

        initAccount();
    }

    private void initAccount() {
        mAccountManager = AccountManager.get(getBaseContext());

        // If this is a first time adding, then this will be null
        accountNameS = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);

        if (mAuthTokenType == null)
            mAuthTokenType = getString(R.string.auth_type);

        findAccount(accountNameS);

        System.out.println(mAuthTokenType + ", accountName : " + accountNameS);
    }

    void userSignIn() {

        // You should probably call your server with user credentials and get
        // the authentication token here.
        // For demo, I have hard-coded it.
        authtoken = "123456789";

        accountNameS = accountName.getText().toString().trim();
        password = accountPassword.getText().toString().trim();

        if (accountNameS.length() > 0) {
            Bundle data = new Bundle();
            data.putString(AccountManager.KEY_ACCOUNT_NAME, accountNameS);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAuthTokenType);
            data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
            data.putString(PARAM_USER_PASS, password);

            // Some extra data about the user
            Bundle userData = new Bundle();
            userData.putString("UserID", "25");
            data.putBundle(AccountManager.KEY_USERDATA, userData);

            //Make it an intent to be passed back to the Android Authenticator
            final Intent res = new Intent();
            res.putExtras(data);

            //Create the new account with Account Name and TYPE
            final Account account = new Account(accountNameS, mAuthTokenType);

            //Add the account to the Android System
            if (mAccountManager.addAccountExplicitly(account, password, userData)) {
                // worked
                Log.d(TAG, "Account added");
                mAccountManager.setAuthToken(account, mAuthTokenType, authtoken);
                setAccountAuthenticatorResult(data);
                setResult(RESULT_OK, res);
                finish();
            } else {
                // guess not
                Log.d(TAG, "Account NOT added");
            }

        }
    }

    @OnClick(R.id.btnSignIn)
    public void onViewClicked() {
        userSignIn();
    }
}
