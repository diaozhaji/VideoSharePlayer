package com.wotlab.sch.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wotlab.sch.data.Response;
import com.wotlab.sch.task.NetworkAsyncTask.TaskCallBack;
import com.wotlab.sch.task.RegisterTask;
import com.wotlab.sch.videoresplayer.R;

public class RegisterActivity extends Activity implements OnClickListener,
		TaskCallBack<Response> {

	private EditText nameEdit;
	private EditText passwordEdit;
	private EditText passwordConfirm;
	private Button registerBtn;
	private Button cancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		setUpView();

	}

	private void setUpView() {
		nameEdit = (EditText) findViewById(R.id.name);
		passwordEdit = (EditText) findViewById(R.id.password);
		passwordConfirm = (EditText) findViewById(R.id.password_confirm);
		registerBtn = (Button) findViewById(R.id.register);
		registerBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.register:
			register();
			break;
		case R.id.cancel:
			cancel();
			break;
		}
	}

	private AsyncTask<Void, Void, Response> mRegisterTask;

	private void register() {
		if (nameEdit.length() == 0 || passwordEdit.length() == 0
				|| passwordConfirm.length() == 0) {
			Toast.makeText(getApplicationContext(), "用户名/密码 不能为空",
					Toast.LENGTH_LONG).show();
		}
		if (!passwordEdit.getText().toString()
				.equals(passwordConfirm.getText().toString())) {
			Log.d("jy", "key1 =" + passwordEdit.getText().toString()
					+ " key2 = " + passwordEdit.getText().toString());
			Toast.makeText(getApplicationContext(), "密码输入不一致",
					Toast.LENGTH_LONG).show();
		} else {
			String name = nameEdit.getText().toString();
			String password = passwordEdit.getText().toString();
			mRegisterTask = new RegisterTask(name, password, this);
			mRegisterTask.execute();
		}
	}

	private void cancel() {
		this.finish();
	}

	@Override
	public void onTaskFinish(Response result) {
		if (result != null) {
			if (result.msg.equals("1")) {
				Toast.makeText(getApplicationContext(), "注册成功",
						Toast.LENGTH_LONG).show();
				this.finish();
			} else {
				Toast.makeText(getApplicationContext(), result.data,
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
