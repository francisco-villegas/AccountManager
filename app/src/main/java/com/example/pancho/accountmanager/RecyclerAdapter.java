package com.example.pancho.accountmanager;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pancho.accountmanager.model.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
	private List<Item> appsList;
	private Context context;

	public RecyclerAdapter(List<Item> appsList) {
		this.appsList = appsList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		context = parent.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder view, int position) {
		final Item item = appsList.get(position);

		view.button_remove.setTag(position);
		view.button_remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				AccountManager mAccountManager;
				mAccountManager = AccountManager.get(context);
				Account account = new Account(item.getValue(), context.getString(R.string.auth_type));
				mAccountManager.removeAccount(account, ((Activity) context), new AccountManagerCallback<Bundle>() {
					@Override
					public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
						if (accountManagerFuture.isDone())
							Toast.makeText(context, "Removed " + item.getValue(), Toast.LENGTH_SHORT).show();
						appsList.remove(Integer.parseInt(String.valueOf(v.getTag())));
						notifyDataSetChanged();
					}
				}, null);
			}
		});

		view.key.setText(item.getKey());
		view.value.setText(item.getValue());
	}

	@Override
	public int getItemCount() {
		return appsList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		@Nullable
		@BindView(R.id.key)
		TextView key;

		@Nullable
		@BindView(R.id.value)
		TextView value;

		@Nullable
		@BindView(R.id.button_remove)
		TextView button_remove;


		public ViewHolder(View itemView) {
			super(itemView);

			ButterKnife.bind(this,itemView);
		}
	}
}
