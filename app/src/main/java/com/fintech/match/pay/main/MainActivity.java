package com.fintech.match.pay.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fintech.match.pay.App;
import com.fintech.match.pay.R;
import com.fintech.match.pay.data.source.TradeLog;
import com.fintech.match.pay.data.source.local.MatchPayDatabase;
import com.fintech.match.pay.data.source.local.TradeLogDao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ListView listview;

    LogAdapter mAdapter;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listview = findViewById(R.id.listview);


        mAdapter = new LogAdapter(getApplicationContext(), R.layout.item_lv_array);
        listview.setAdapter(mAdapter);

        Observable<List<TradeLog>> observable = Observable.create(emitter -> emitter.onNext(getlogs()));
        disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tradeLogs -> {
                    mAdapter.addAll(tradeLogs);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> throwable.printStackTrace());

    }

    static class LogAdapter extends ArrayAdapter<TradeLog> {

        private int mResourceId;

        public LogAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.mResourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(mResourceId, null);
            }

            TradeLog log = getItem(position);

            TextView viewById = convertView.findViewById(R.id.lv_tv_log);
            if (log.isPost()) {
                viewById.setTextColor(Color.parseColor("#1D8C21"));
            } else {
                viewById.setTextColor(Color.parseColor("#F92E2A"));
            }
            viewById.setText(log.getDesc());

            return convertView;
        }
    }

    private List<TradeLog> getlogs() {
        MatchPayDatabase database = MatchPayDatabase.getInstance(App.getAppContext());
        TradeLogDao dao = database.logDao();

        List<TradeLog> all = dao.getAll();
        return all;
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
