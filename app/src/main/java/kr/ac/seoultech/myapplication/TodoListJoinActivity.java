package kr.ac.seoultech.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import kr.ac.seoultech.myapplication.http.UserHttpService;

public class TodoListJoinActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etPassword;
    private EditText etLoginId;

    private UserHttpService userHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_join);

        etLoginId = (EditText) findViewById(R.id.et_login_id);
        etPassword = (EditText) findViewById(R.id.et_password);
        etName = (EditText) findViewById(R.id.et_name);

        findViewById(R.id.btn_join).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);

        userHttpService = new UserHttpService(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_join : {
                String loginId = etLoginId.getText().toString();
                String password = etPassword.getText().toString();
                String name = etName.getText().toString();

                userHttpService.join(loginId, password, name);
                finish();

            }
            case R.id.btn_cancel : {
                finish();
            }
        }

    }
}
