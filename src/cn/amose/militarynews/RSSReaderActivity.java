package cn.amose.militarynews;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.amose.militarynews.fetch.UrlFetcher;
import cn.amose.militarynews.rss.RssFeed;
import cn.amose.militarynews.rss.RssItem;
import cn.amose.militarynews.rss.RssReader;

public class RSSReaderActivity extends Activity {
	EditText mRSSUrlEt;
	TextView mRSSTitleTv;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj instanceof List<?>) {
					List<String> urlList = (List<String>) msg.obj;
					StringBuilder sbUrl = new StringBuilder();
					for (int size = urlList.size() - 1; size >= 0; size--) {
						sbUrl.append(urlList.get(size)).append("<br/>");
					}
					mRSSTitleTv.setText(Html.fromHtml(sbUrl.toString()));
					if (urlList.size() > 0) {
						rssfeed(urlList.get(0));
					}
				}
				break;
			case 1:
				mRSSTitleTv.setText(Html.fromHtml(msg.obj.toString()));
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mRSSUrlEt = (EditText) findViewById(R.id.et_rss_url);
		mRSSTitleTv = (TextView) findViewById(R.id.tv_rss_title);
		mRSSUrlEt.setText("http://rss.sina.com.cn/news/marquee/ddt.xml");
	}

	public void OnFetchClick(View v) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<String> urlList = UrlFetcher.fetch();
				Message msg = new Message();
				msg.obj = urlList;
				msg.what = 0;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void rssfeed(final String urlStr) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// urlStr = "http://rss.cnn.com/rss/edition.rss";
				try {
					StringBuilder sbRssTitle = new StringBuilder();
					URL url = new URL(urlStr);
					RssFeed feed = RssReader.read(url);
					ArrayList<RssItem> rssItems = feed.getRssItems();
					for (RssItem rssItem : rssItems) {
						Log.i("RSS Reader", rssItem.getTitle());
						sbRssTitle.append(rssItem.getTitle()).append("<br/>");
					}
					Message msg = new Message();
					msg.obj = sbRssTitle.toString();
					msg.what = 1;
					mHandler.sendMessage(msg);
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	public void OnRSSClick(View view) {

		final String urlStr = mRSSUrlEt.getText().toString().trim();
		if (urlStr.equals("")) {
			return;
		}
		rssfeed(urlStr);
	}

}
