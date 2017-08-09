package com.vanpro.zitech125.ui.extend;

import android.view.View;
import android.view.View.OnClickListener;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.widget.Toolbar;
import com.vanpro.zitech125.ui.widget.ToolbarHelper;


/**
 * 使用了自定义标题栏的activity,所有要使用自定义标题栏的activity都继承自本类
 *
 * Mobidy by Jinsen 16/1/9
 *
 */
public abstract class CustomToolbarActivity extends BaseActivity implements OnClickListener {

	private ToolbarHelper mToolbarHelper;
	private Toolbar mToolbar;

	@Override
	public void setContentView(int layoutResId){
		mToolbarHelper = new ToolbarHelper(this,layoutResId);
		mToolbar = mToolbarHelper.getToolbar();
		setContentView(mToolbarHelper.getContentVew());
		mToolbar.setOnClickListener(this);
		setSupportActionBar(mToolbar);
	}

	public View getContentView(){
		return mToolbarHelper.getContentVew();
	}

	public Toolbar getTitleBar() {
		return this.mToolbar;
	}

	public ToolbarHelper getToolbarHelper(){
		return mToolbarHelper;
	}

	/**
	 * 设置标题文字
	 * (non-Javadoc)
	 * @see android.app.Activity#setTitle(CharSequence)
	 */
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		mToolbar.baseTitle(title);
	}


	/**
	 * 设置标题文字
	 * (non-Javadoc)
	 * @see android.app.Activity#setTitle(int)
	 */
	public void setTitle(int titleId) {
		super.setTitle(titleId);
		mToolbar.baseTitle(getString(titleId));
	}

	/**
	 * 设置底线颜色
	 *
	 * @param bgColor
	 */
	public void setButtomLineColor(int bgColor) {
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
			getSupportFragmentManager().popBackStack();
		} else {
			if (!isTaskRoot())
				finish();
		}
	}


	public void setActionText(int resId){
		mToolbar.setActionText(resId);
	}

	public void setBackIcon(){
		mToolbar.getBackBtn().setImageResource(R.drawable.common_back);
	}

	/**
	 * 设置标题栏返回键图标
	 * @param resId
	 */
	public void setBackIcon(int resId){
		if(mToolbar.getBackBtn() != null)
			mToolbar.getBackBtn().setImageResource(resId);
	}

	/**
	 * 点击toolbar title
	 */
	public void scrollTop(){

	}

	/**
	 * 点击toolbar 右上角按钮事件处理
	 */
	protected void action(){

	}


	public void onClick(View v){
		switch (v.getId()){
			case R.id.toolbar_back_btn:
			case R.id.toolbar_back_tx:
				onBackPressed();
				break;

			case R.id.toolbar_close_btn:
			case R.id.toolbar_close_tx:
				finish();
				break;

			case R.id.toolbar_title:
				scrollTop();
				break;

			case R.id.toolbar_action_btn:
			case R.id.toolbar_action_tx:
				action();
				break;
		}
	}

}
