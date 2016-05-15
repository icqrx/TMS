package jp.co.isb.tms;

import jp.co.isb.tms.model.CustomerPicInfo;
import jp.co.isb.tms.util.ImageUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PictureItemView extends LinearLayout {
	private ImageView mIvPicItem;
	private TextView mTvComment;
	
	private CustomerPicInfo mItemInfo;

	public PictureItemView(Context context) {
		super(context);
		LayoutInflater inflate = LayoutInflater.from(getContext());
		inflate.inflate(R.layout.picture_item, this, true);
		
		if (mIvPicItem == null) {
			mIvPicItem = (ImageView) findViewById(R.id.iv_pic_item);
		}
		
		if (mTvComment == null) {
			mTvComment = (TextView) findViewById(R.id.tv_comment);
		}
		
	}

	public void setData(CustomerPicInfo itemInfo) {
		mItemInfo = itemInfo;
		if (mItemInfo == null) {
			return;
		}
		
		if (mIvPicItem != null) {
			//mIvPicItem.setImageBitmap(mItemInfo.getBitmap());
			mIvPicItem.setImageBitmap(ImageUtils.convertStringToBitmap(mItemInfo.getmThumnailData()));
		}
		
		if (mTvComment != null) {
			mTvComment.setText(mItemInfo.getImageComment());
		}
	}

}
