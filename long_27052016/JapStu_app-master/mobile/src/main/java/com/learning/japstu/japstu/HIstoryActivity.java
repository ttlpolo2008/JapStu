package com.learning.japstu.japstu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.utils.User;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HIstoryActivity extends Activity {

    public User mUser;
    public String username = "user1";
    public long userID = 0;
    public String nickname = "Van Long";

    public boolean bHasHistory = false;

    private ListView listContentView;
    private UsersAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        nickname = bundle.getString("nickname");
        userID = Long.parseLong(bundle.getString("userId"));

        String title = getResources()
                .getString(R.string.title_activity_history);
        ((TextView)findViewById(R.id.left_header_content)).setText(title);
        ((TextView)findViewById(R.id.left_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));
        ((TextView)findViewById(R.id.right_header_content))
                .setText(nickname);
        ((TextView)findViewById(R.id.right_header_content))
                .setTextColor(getResources().getColor(R.color.title_page_color));


        listContentView = (ListView) findViewById(R.id.history_content_list);
        arr =  new ArrayList<UserHistory>();
        System.out.println("Number of arr =    " + arr.size());
        adapter = new UsersAdapter(HIstoryActivity.this, arr);
        adapter.addAll(arr);
        listContentView.setAdapter(adapter);
        // TODO

    }

    ArrayList<UserHistory> arr;

    @Override
    protected void onResume() {
        super.onResume();

        getHistory();
    }

    public void getHistory(){
        new HistoryAsyncTask().execute();
    }

    private class HistoryAsyncTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(HIstoryActivity.this,
                    "Loading", "Loading user informations");
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                mUser = new User(username, userID, nickname);
                bHasHistory = true;
                if(mUser.iLearnedLesson > 0)
                    bHasHistory = true;
            }catch (Exception e){
                System.out.println(e.getMessage().toString());
            }
            return null;
        }

        protected void onPostExecute(Void unused){
            dialog.dismiss();
            mHandler.sendEmptyMessage(0);
        }
    }
    // handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(bHasHistory){
                arr.clear();
                arr.addAll(fromJson(mUser.userHistory.getAsJsonArray()));
                adapter.notifyDataSetChanged();
                Commons.setListViewHeightBasedOnChildren(listContentView);
            }else{
                finish();
            }
        }
    };

    public class UsersAdapter extends ArrayAdapter<UserHistory> {

        private ArrayList<UserHistory> mHistory;
        private Context context;

        public UsersAdapter(Context context, ArrayList<UserHistory> history) {
            super(context, R.layout.sub_layout_history, history);
            this.mHistory = history;
            this.context = context;
        }

        @Override
        public UserHistory getItem(int position) {
            return mHistory.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UserHistory user = getItem(position);
            System.out.println(user.lessonName + "    " + position);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.sub_layout_history, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_history_lesson_name);
            TextView tvTime = (TextView) convertView.findViewById(R.id.tv_history_lesson_time);
            TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_history_lesson_finish);
            TextView tvMark = (TextView) convertView.findViewById(R.id.tv_history_lesson_score);
            tvName.setText(user.lessonName);
            tvTime.setText(user.endDate);
            tvStatus.setText(user.status);
            tvMark.setText(user.scores);
            return convertView;
        }
    }

    public class UserHistory {

        public String lessonName = "";
        public String endDate = "";
        public String status = "";
        public String scores = "";

        public UserHistory(JsonObject object){
            try {
                this.lessonName = object.get("lessonName").getAsString();
                this.endDate = object.get("endDate").getAsString();
                if(object.get("status").getAsBoolean())
                    this.status = "Completed";
                else
                    this.status = "Learning";
                this.scores = object.get("examMark").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public ArrayList<UserHistory> fromJson(JsonArray jsonObjects) {
        ArrayList<UserHistory> users = new ArrayList<UserHistory>();
        for (int i = 0; i < jsonObjects.size(); i++) {
            try {
                users.add(new UserHistory(jsonObjects.get(i).getAsJsonObject()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

}
