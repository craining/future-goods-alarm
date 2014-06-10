package com.zgy.goldmonitor.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgy.goldmonitor.MainApp;
import com.zgy.goldmonitor.R;

public class DialogList extends Dialog {
	public static class Item {
		public String itemText;
		public boolean isSelected;
		public int tag;
	}
	
	public interface OnListItemSelectedListener {
		public void onListItemSelected(Item item);
	}

	public DialogList(Context context, int theme) {
		super(context, theme);
	}

	public DialogList(Context context) {
		super(context);
	}

	@Override
	public void setCancelable(boolean flag) {
		super.setCancelable(flag);
	}

	public static class Builder {

		private Context context;
		private String title;
		private String positiveButtonText;
		private String negativeButtonText;
		private String neutralButtonText;
		private View contentView;
		private boolean mCancelable;
		private Item[] mItems;
		private OnListItemSelectedListener mListener;
		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener, neutralButtonClickListener;

		public Builder(Context context) {
			this.context = context;

		}

		public Builder setItems(Item[] items) {
			this.mItems = items;
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);

			return this;

		}

		public Builder setTitle(String title) {
			this.title = title;

			return this;

		}

		public Builder setOnListItemSelectedListener(OnListItemSelectedListener listener) {
			this.mListener = listener;

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

		public DialogList create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final DialogList dialog = new DialogList(context, R.style.dialog);// R.style.dialog

			View layout = inflater.inflate(R.layout.dialog_list, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			((TextView) layout.findViewById(R.id.title)).setText(title);

			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);

				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
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
			if (mItems != null && mItems.length > 0) {
				((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
				for (final Item item : mItems) {
					RelativeLayout l = new RelativeLayout(context);
					android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 50, MainApp.getInstance().getResources().getDisplayMetrics()));
					l.setLayoutParams(lp);

					TextView tv = new TextView(context);
					android.widget.RelativeLayout.LayoutParams lptv = new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
							android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
					lptv.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					lptv.addRule(RelativeLayout.CENTER_VERTICAL);
					lptv.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, MainApp.getInstance().getResources().getDisplayMetrics());

					tv.setLayoutParams(lptv);
					tv.setText(item.itemText);
					l.addView(tv);

					if (item.isSelected) {
						ImageView img = new ImageView(context);
						android.widget.RelativeLayout.LayoutParams lpimg = new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
								android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
						lpimg.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						lpimg.addRule(RelativeLayout.CENTER_VERTICAL);
						lpimg.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, MainApp.getInstance().getResources().getDisplayMetrics());
						img.setLayoutParams(lpimg);
						img.setImageResource(R.drawable.ic_selected);
						l.addView(img);
					}

					View v = new View(context);
					v.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
							1, MainApp.getInstance().getResources().getDisplayMetrics())));
					l.setBackgroundResource(R.drawable.selecter_setting_item_bg);
					l.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mListener != null) {
								mListener.onListItemSelected(item);
							}
							dialog.dismiss();
						}
					});

					((LinearLayout) layout.findViewById(R.id.content)).addView(l);
					((LinearLayout) layout.findViewById(R.id.content)).addView(v);
				}
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(contentView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

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
