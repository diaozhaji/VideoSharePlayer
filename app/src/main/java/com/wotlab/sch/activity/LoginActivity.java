package com.wotlab.sch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wotlab.sch.data.Response;
import com.wotlab.sch.rtmp.CameraVideoActivity;
import com.wotlab.sch.task.LoginTask;
import com.wotlab.sch.task.NetworkAsyncTask.TaskCallBack;
import com.wotlab.sch.util.APIs;
import com.wotlab.sch.videoresplayer.MainActivity;
import com.wotlab.sch.videoresplayer.R;

public class LoginActivity extends Activity implements OnClickListener,
		TaskCallBack<Response> {

	private EditText nameEdit;
	private EditText passwordEdit;
	private TextView registerView;
	private Button loginBtn;
    private View progressContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		setUpView();

	}

	private void setUpView() {
		nameEdit = (EditText) findViewById(R.id.name);
		passwordEdit = (EditText) findViewById(R.id.password);
		registerView = (TextView) findViewById(R.id.register);
        progressContainer = findViewById(R.id.progress_container);
		registerView.setOnClickListener(this);
		loginBtn = (Button) findViewById(R.id.login);
		loginBtn.setOnClickListener(this);

        /* 测试用 */
        Button pushBtn = (Button) findViewById(R.id.push);
        pushBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login:
			login();
			break;
		case R.id.register:
			toRegister();
			break;
        case R.id.push:
            push();
            break;
		}
	}

    private void push() {
        Intent intent = new Intent(this, CameraVideoActivity.class);
        startActivity(intent);
    }

    private void toRegister() {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	private AsyncTask<Void, Void, Response> mLoginTask;

	private void login() {
		Log.d("jy", "login()");
		if (nameEdit.length() == 0 || passwordEdit.length() == 0) {
			Toast.makeText(getApplicationContext(), "用户名/密码 不能为空",
					Toast.LENGTH_LONG).show();
		} else {
            showProgress();
			String name = nameEdit.getText().toString();
			String password = passwordEdit.getText().toString();
			Log.d("jy", "name = " + name);
			Log.d("jy", "name = " + password);
			mLoginTask = new LoginTask(APIs.getLoginUrl(name, password), this);
			mLoginTask.execute();
		}
	}

	@Override
	public void onTaskFinish(Response result) {
		if (result != null) {
			if (result.msg.equals("1")) {
				Toast.makeText(getApplicationContext(), "登陆成功",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				Toast.makeText(getApplicationContext(), result.data,
						Toast.LENGTH_SHORT).show();
			}
		}
        hideProgress();
	}

    private void showProgress(){
        progressContainer.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        progressContainer.setVisibility(View.GONE);
    }

}
