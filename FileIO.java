package com.test.sku.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;


public class FileIO {

	private static String fpath ="C:/test/data/";
	private static String savePath = "C:/test/download/";

	public static boolean OkObject(String fname) {
		File f = new File(fpath + fname);      //있는가 없는가 확인 가능. 파일 오브젝트
		if(!f.exists()) {
			System.err.println("지정된 파일이 없습니다");
			return false;
		}
		return true;
	}
	
	public static byte[] upload(String fname) {
		File textFile = new File(fpath + fname);
		int len = (int)textFile.length();              //원래 long형
		try {
			FileInputStream fin = new FileInputStream(textFile);
			byte[]buf = new byte[len];					//파일 크기만큼의 메모리공간 준비	
			/*아마 지워도 되는 4줄 확인을 위해 나둠		성공은 했으나 끝날때 꼭 지우기!!!!!!
				fin.read(buf);
				String str = new String(buf);
				System.out.println(str);
				fin.close();*/
				return buf;
			} catch (IOException e) {			
				e.printStackTrace();
			}
		return null;					
	
	
	}
	
	public boolean download(String fname, byte[] fdata) {
		try {
			FileOutputStream fout = new FileOutputStream(savePath + fname);
			fout.write(fdata);
			fout.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static String read(String fname,byte[] fdata) {
		
		try {			
			File textFile = new File(savePath + fname);
			
			FileInputStream fin = new FileInputStream(textFile);
			
			int len = (int)textFile.length();
			byte[]buf = new byte[len];
			fin.read(buf);
			String str = new String(buf);
			fin.close();
			return str;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fname;
	}
	
	
}
