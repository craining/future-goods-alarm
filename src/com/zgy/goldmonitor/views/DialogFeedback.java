package com.zgy.goldmonitor.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.Preference;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.util.AppUtil;
import com.zgy.goldmonitor.util.NetworkUtil;
import com.zgy.goldmonitor.util.PhoneUtil;
import com.zgy.goldmonitor.util.SendEmailUtil;

public class DialogFeedback extends Dialog {

	public DialogFeedback(Context context, int theme) {
		super(context, theme);
	}

	public DialogFeedback(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private String title;
		private String positiveButtonText;
		private String negativeButtonText;
		private String neutralButtonText;
		private View contentView;
		private boolean mCancelable;
		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener, neutralButtonClickListener;

		private Button[] mBtnPoints;

		
		public boolean sendFinished = false;
		
		public Builder(Context context) {
			this.context = context;

		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);

			return this;

		}

		public Builder setTitle(String title) {
			this.title = title;

			return this;

		}

		public Builder setContentView(View v) {
			this.contentView = v;

			return this;

		}
	    public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }
		public Builder setPositiveButton(int positiveButtonText,

		DialogInterface.OnClickListener listener) {

			this.positiveButtonText = (String) context

			.getText(positiveButtonText);

			this.positiveButtonClickListener = listener;

			return this;

		}

		public Builder setPositiveButton(String positiveButtonText,

		DialogInterface.OnClickListener listener) {

			this.positiveButtonText = positiveButtonText;

			this.positiveButtonClickListener = listener;

			return this;

		}

		/**
		 * 
		 * @Description:初始化dialog中第二个按钮的文字和点击事件
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 * @see:
		 * @since:
		 * @author: hanlx
		 * @date:2013-2-21
		 */
		public Builder setNegativeButton(int negativeButtonText,

		DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;

			return this;

		}

		public Builder setNegativeButton(String negativeButtonText,

		DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;

			return this;

		}

		/**
		 * 
		 * @Description:初始化dialog中第三个按钮的值和点击事件
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 * @see:
		 * @since:
		 * @author: hanlx
		 * @date:2013-2-21
		 */
		public Builder setNeutralButton(int neutralButtonText, DialogInterface.OnClickListener listener) {
			this.neutralButtonText = (String) context.getText(neutralButtonText);
			this.neutralButtonClickListener = listener;
			return this;
		}

		public Builder setNeutralButton(String neutralButtonText, DialogInterface.OnClickListener listener) {
			this.neutralButtonText = neutralButtonText;
			this.neutralButtonClickListener = listener;
			return this;
		}

		public DialogFeedback create() {
			sendFinished = false;
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final DialogFeedback dialog = new DialogFeedback(context, R.style.dialog);// R.style.dialog

			View layout = inflater.inflate(R.layout.dialog_feedback, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			((TextView) layout.findViewById(R.id.title)).setText(title);

			final EditText editContent = (EditText) layout.findViewById(R.id.edit_feedback_content);
			final EditText editAddr = (EditText) layout.findViewById(R.id.edit_feedback_addr);
			
			editContent.setText(Preference.getInstance().getFeedbackStr());
			editAddr.setText(Preference.getInstance().getFeedbackAddr());
			
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);

				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							
							//暂定为发送反馈
							if(TextUtils.isEmpty(editContent.getText())) {
								Toast.makeText(context, "反馈内容不能为空", Toast.LENGTH_LONG).show();
								return;
							}
							final String content = editContent.getText().toString();
							final String addr = editAddr.getText().toString();
							Preference.getInstance().setFeedbackStr(content);
							Preference.getInstance().setFeedbackAddr(addr);
							
							
							if(!NetworkUtil.isNetworkAvailable(context)) {
								Toast.makeText(context, "当前无网络，请稍后再试", Toast.LENGTH_LONG).show();
								return;
							}
							
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									try {
										StringBuilder sb = new StringBuilder();
										sb.append(content + "\r\nAddr = " + addr + "\r\nVersion = ").append(context.getResources().getString(R.string.version_str)).append(AppUtil.getChannelId()).append(PhoneUtil.getHandsetInfo(context));
										
										Debug.e("", "反馈内容   ：" + sb.toString());
										SendEmailUtil email = new SendEmailUtil();
										email.sendMail("期货通反馈", sb.toString(), "craining@163.com");
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							}).start();
							Toast.makeText(context, "感谢您的反馈！", Toast.LENGTH_LONG).show();
							sendFinished = true;
							positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			} else {
				layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);

			}

			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);

				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);

						}
					});
				}
			} else {
				layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
			}

			if (neutralButtonText != null) {
				Button neutralButton = (Button) layout.findViewById(R.id.third_button);
				neutralButton.setText(neutralButtonText);
				neutralButton.setVisibility(View.VISIBLE);
				if (neutralButtonClickListener != null) {
					neutralButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							neutralButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
						}
					});
				}
			}
			if (positiveButtonText == null && negativeButtonText == null && neutralButtonText == null) {
				layout.findViewById(R.id.positiveButtonLayout).setVisibility(View.GONE);
			}
			dialog.setContentView(layout);
			dialog.setCancelable(mCancelable);
			if(!mCancelable) {
				dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if(keyCode == KeyEvent.KEYCODE_BACK) {
							dialog.dismiss();
						}
						return false;
					}
				});
			}
			return dialog;

		}
	}

}
