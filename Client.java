package com.test.sku.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	static Scanner kbd = new Scanner(System.in);
	public static void main(String[] args) {
		//클라이언트 소켓을 이용하여 서버에 접속한다
		try {
			Socket s = new Socket("220.67.113.238", 1234);	//접속 요청
   
		   InputStream in =s.getInputStream();
		   ObjectInputStream oin = new ObjectInputStream(in);
		   ChatMsg cm = (ChatMsg)oin.readObject();
		   System.out.println(cm.msg + ":");
		   String uid = kbd.next();
		   String pwd = kbd.nextLine();
   
		   //출력스트림
		   ChatMsg cm2 = new ChatMsg(true, uid, pwd);
		   OutputStream out = s.getOutputStream();
		   ObjectOutputStream oos = new ObjectOutputStream(out);
		   oos.writeObject(cm2);
		   oos.flush();
		   	
		   cm = (ChatMsg)oin.readObject();		//chatthread 시작부분을 받아옴
		   
		   if(cm.msg.equals("로그인 성공")){
			   new InputTread(uid,oin).start();
			   while(true) {
				   cm = new ChatMsg();
				   System.out.println("귓속말(s) 공개메시지(p):");
				   String m = kbd.nextLine().trim();
				   if(m.equalsIgnoreCase("x")) {
				   System.out.println("채팅을 종료합니다");//equalsIgnoreCase:대소문자 상관없이
				   break;
				   }
				   else if(m.equalsIgnoreCase("s")) {
					   System.out.println("수신자 아이디");
					   String rec = kbd.nextLine();
					   System.out.println("메시지:");
					   String msg = kbd.nextLine();	
					   
					   System.out.println("첨부(a):");
					   String fname = kbd.nextLine();	
					   
					   cm = new ChatMsg();		//여기다가 파일추가
					   cm.uid = uid;			//송신자
					   cm.isSecret = true;		//귓속말
					   cm.to = rec;				//수신자 에게
					   cm.msg = msg;			//내용 메시지
					   
					   if(!fname.equals("") && fname!=null) {
						  boolean ok = FileIO.OkObject(fname);
						  if(ok = true) {
								System.out.println("존재합니다");	//성공
								byte[] bt = FileIO.upload(fname);
								cm.fname = fname;
								cm.fdata = bt;
								System.out.println("성공적으로 보냅습니다");																
								
							}else {
								System.out.println("파일이 존재하지 않습니다");
							}
						  
					   }
					   
					   oos.writeObject(cm);
					   oos.flush();					   
					   continue;
					   
				   }else if(m.equals("y")) {
					   String fname = InputTread.chatMsg.fname;
					   byte[] fdata = InputTread.chatMsg.fdata;
					   boolean saved = new FileIO().download(fname, fdata);
					   if(saved) {
						   System.out.println("다운로드 성공");
						   System.out.println(FileIO.read(fname, fdata));}
					   else System.err.println("다운로드 실패");
				   }
								
				
				   System.out.println("메시지:");
				   String msg =kbd.nextLine().trim();

				   cm.uid = uid;
				   cm.msg = msg;
				   oos.writeObject(cm);
				   oos.flush();   
				   
				  
			   }
			
			oos.close();
			//ois.close();
			s.close();
		   }else {
		   		System.out.println("로그인 실패");		  
		   }
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		//여러개의 예외를 발생할때는 어덯게해? catch를 여러개 한다
		//개선된 for문과 geneics,
		System.out.println("클라이언트 종료");
		
	}
	
}
