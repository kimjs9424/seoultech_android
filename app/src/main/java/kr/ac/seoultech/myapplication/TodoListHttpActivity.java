package kr.ac.seoultech.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import kr.ac.seoultech.myapplication.adapter.TodoAdapter;
import kr.ac.seoultech.myapplication.model.Todo;

public class TodoListHttpActivity extends AppCompatActivity {

    private ListView listView;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_http);

        listView = (ListView) findViewById(R.id.list_view);

        adapter = new TodoAdapter(this, R.layout.list_item_todo, new ArrayList<Todo>());
        listView.setAdapter(adapter);


        if (checkAccessToken() == false) {
            //토큰 없으면 회원가입 activity 실행
            startActivity(new Intent(this, TodoListJoinActivity.class));

        }

    }

    private boolean checkAccessToken() {
        SharedPreferences sp = getSharedPreferences("LOGIN_SETTING", Context.MODE_PRIVATE);
        if (sp.getString("token", null) != null) {
            return true;
        } else {
            return  false;
        }

    }
}
