package com.zgy.goldmonitor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileUtil {
	/**
	 * 从文件读取数组
	 * 
	 * @Description:
	 * @param file
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-10-10
	 */
	public static ArrayList<String> readArray(File file) {
		ArrayList<String> result = new ArrayList<String>();
		if(file.exists()) {
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
				result = (ArrayList<String>) in.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

//	/**
//	 * 把数组写入文件
//	 * 
//	 * @Description:
//	 * @param email
//	 * @param file
//	 * @see:
//	 * @since:
//	 * @author: zhuanggy
//	 * @date:2012-10-10
//	 */
//	public static void writeString2Array(String email, File file) {
//		// 绑定账号时，先读取已经存储的绑定成功过的账号，若不存在当前绑定的账号，则添加到其中。
//		ArrayList<String> a = new ArrayList<String>();
//		if (file.exists()) {
//			a = readArray(file);
//		}
//		if (a != null && !(a.contains(email))) {
//			a.add(email);
//			try {
//				if (file.exists()) {
//					file.delete();
//					file.createNewFile();
//				}
//				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
//				out.writeObject(a);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	
	/**
	 * 把数组写入文件
	 * 
	 * @Description:
	 * @param email
	 * @param file
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-10-10
	 */
	public static void writeArray(ArrayList<String> a, File file) {
		if (a != null ) {
			try {
				if (file.exists()) {
					file.delete();
					file.createNewFile();
				}
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(a);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
