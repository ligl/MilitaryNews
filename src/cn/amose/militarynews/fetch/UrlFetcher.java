package cn.amose.militarynews.fetch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.amose.militarynews.util.Constant;

public class UrlFetcher {
	public static List<String> fetch() {
		List<String> urlList = new ArrayList<String>();
		try {
			// TODO set url pool and loop it
			URL url = new URL(Constant.SERVER_URL_ZXQ_NET);
			try {
				URLConnection conn = url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					urlList.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return urlList;
	}

}
