package com.major.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	
	/**
	 * 读取文件流
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readStream(InputStream in) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		String result = out.toString();
		in.close();
		out.flush();
		out.close();
		return result;
	}

}
