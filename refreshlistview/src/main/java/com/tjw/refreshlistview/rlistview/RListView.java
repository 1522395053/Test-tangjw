package com.tjw.refreshlistview.rlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.tjw.refreshlistview.R;

public class RListView extends ListView implements OnScrollListener {
	
	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener
	
	// the interface to trigger refresh and load more.
	private RListViewListener mListViewListener;
	
	// -- header view
	private RListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private LinearLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.
	
	// -- footer view
	private RListViewFooter mFooterView;
	private boolean mEnablePullLoad = false;
	private boolean mPullLoading = false;
	private boolean mIsFooterReady = false;
	
	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;
	
	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;
	
	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 40; // when pull up >= 50px
	// at bottom, trigger
	// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
	private boolean mPullLoad;
	// feature.
	
	public RListView(Context context) {
		super(context);
		initWithContext(context);
	}
	
	public RListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}
	
	public RListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}
	
	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);
		
		// init header view
		mHeaderView = new RListViewHeader(context);
		mHeaderViewContent = (LinearLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView, null, false);
		// init footer view
		mFooterView = new RListViewFooter(context);
		
		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
		
		setPullLoadEnable(false);//初始化不支持上啦加载
		mFooterView.findViewById(R.id.xlistview_footer_hint_textview).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!mPullLoading && !mPullRefreshing) {
					mPullLoading = true;
					mFooterView.setState(RListViewFooter.STATE_LOADING);
					startLoadMore();
				}
			}
		});
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (!mIsFooterReady) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}
	
	/**
	 * enable or disable pull down refresh feature.
	 *
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * enable or disable pull up load more feature.
	 *
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
			//make sure "pull up" don't show a line in bottom when listview with one page
			setFooterDividersEnabled(false);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(RListViewFooter.STATE_NORMAL);
			//make sure "pull up" don't show a line in bottom when listview with one page
			setFooterDividersEnabled(true);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}
	
	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing) {
			mHeaderView.setState(RListViewHeader.STATE_SUCCESS);
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	
	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading) {
			mPullLoad = false;
			mPullLoading = false;
			mFooterView.setState(RListViewFooter.STATE_NORMAL);
			resetFooterHeight();
		}
	}
	
	/**
	 * set last refresh time
	 *
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}
	
	private void invokeOnScrolling() {
		
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}
	
	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisibleHeight((int) delta + mHeaderView.getVisibleHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
				mHeaderView.setState(RListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(RListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}
	
	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisibleHeight();
//        LogUtils.i("mHeaderView:"+height+"==mPullRefreshing:"+mPullRefreshing+"==mHeaderViewHeight:"+mHeaderViewHeight);
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}
	
	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
				// more.
				mFooterView.setState(RListViewFooter.STATE_READY);
				mPullLoading = true;
			} else {
				mFooterView.setState(RListViewFooter.STATE_NORMAL);
				mPullLoading = false;
				mPullLoad = false;
			}
		}
		mFooterView.setBottomMargin(height);

//		setSelection(mTotalItemCount - 1); // scroll to bottom
	}
	
	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
			invalidate();
		}
	}
	
	private void startLoadMore() {
//        if (mPullRefreshing || mPullLoading) {
//            return;
//        }
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastY = ev.getRawY();
				if (!mPullRefreshing && getFirstVisiblePosition() == 0) {
					mHeaderView.refreshUpdatedAtValue();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mPullRefreshing) {
					break;
				}

				final float deltaY = ev.getRawY() - mLastY;
				mLastY = ev.getRawY();
				if (!mPullLoading && getFirstVisiblePosition() == 0
						&& (mHeaderView.getVisibleHeight() > 0 || deltaY > 0) && mEnablePullRefresh) {
					// the first item is showing, header has shown or pull down.
					updateHeaderHeight(deltaY / OFFSET_RADIO);
					invokeOnScrolling();
					
				} else if (!mPullRefreshing && !mPullLoad && getLastVisiblePosition() == mTotalItemCount-1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0) && mEnablePullLoad) {
					// last item, already pulled up or want to pull up.
					updateFooterHeight(-deltaY / OFFSET_RADIO);
					
				}
				break;
			case MotionEvent.ACTION_UP:
				mLastY = -1; // reset
				if (!mPullRefreshing && getFirstVisiblePosition() == 0) {
					// invoke refresh
					if (mEnablePullRefresh
							&& mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
						mPullRefreshing = true;
						mHeaderView.setState(RListViewHeader.STATE_REFRESHING);
						if (mListViewListener != null) {
							mListViewListener.onRefresh();
						}
					}
					
				}
				if (mPullLoading && getLastVisiblePosition() == mTotalItemCount - 1) {
					// invoke load more.
					if (mEnablePullLoad
							&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
						mFooterView.setState(RListViewFooter.STATE_LOADING);
						mPullLoad = true;
						startLoadMore();
					}
					
				}
//                if (mHeaderView.getVisibleHeight() > 0) {
//                    LogUtils.i("mHeaderView,Up:"+mHeaderView.getVisibleHeight());
//                }
				resetFooterHeight();
				resetHeaderHeight();
				
				
				break;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisibleHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (getLastVisiblePosition() == getCount() - 1) {
			mFooterView.setState(RListViewFooter.STATE_LOADING);
			startLoadMore();
		}
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
		
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
	                     int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
	
	public void setRListViewListener(RListViewListener l) {
		mListViewListener = l;
	}
	
	/**
	 * 设置是否显示时间
	 *
	 * @param flag
	 */
	public void setShowTimeView(boolean flag) {
		mHeaderView.mShowTimeViewFlag = flag;
		if (flag) {
			mHeaderTimeView.setVisibility(VISIBLE);
		}
	}
	
	
	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		void onXScrolling(View view);
	}
	
	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface RListViewListener {
		void onRefresh();
		void onLoadMore();
	}
	
	/**
	 * 执行刷新
	 */
	public void startRefresh() {
		mPullRefreshing = true;
		mHeaderView.setState(RListViewHeader.STATE_REFRESHING);
		if (mListViewListener != null) {
			mListViewListener.onRefresh();
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, 0, 0, mHeaderViewHeight, SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}
	
}
