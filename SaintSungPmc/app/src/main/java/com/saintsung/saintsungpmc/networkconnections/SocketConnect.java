package com.saintsung.saintsungpmc.networkconnections;


import android.util.Log;
import org.apache.http.conn.ConnectTimeoutException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SocketConnect {
    public static String socketAddress = "socketAddress";
    public static String socketPort = "socketPort";
    public static String socketError = "socketError";
    String string, stringSocketAddress, responsePacket;
    int intSocketPort;
    String  strArray;
    // 连接服务器,发送登陆/下载信息.
    public String sendDate(String requestPacket) {
        String [] stringArray=new String(strArray).split(";");
        if (stringArray == null) {
            return null;
        } else {
            Socket socket = null;
            DataOutputStream out = null;
            DataInputStream in = null;
            stringSocketAddress = stringArray[0];
            intSocketPort = Integer.parseInt(stringArray[1]);
            try {
                socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(
                        stringSocketAddress, intSocketPort);
                socket.connect(socketAddress, 10000);
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(requestPacket);
                out.flush();
                in = new DataInputStream(socket.getInputStream());
                responsePacket = in.readUTF();
                return responsePacket;
            } catch (Exception e) {
                if (e instanceof SocketTimeoutException) {
                    return "响应超时";

                } else if (e instanceof ConnectTimeoutException) {
                    return "请求超时";

                } else if (e instanceof UnknownHostException) {
                    return "无法连接服务器";

                } else if (e instanceof IOException) {
                    return "网络异常";
                }
                responsePacket = e.toString();
                return responsePacket;
            } finally {
                // 因数据量可能偏大先保存到临时文件中
                try {
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.gc();
            }
        }
    }
	/*
	 * //连接服务器,发送登陆信息. private void sendDate(){ try { //应用Server的IP和端口建立Socket对象
	 * socket = new Socket("210.22.164.146", 7013); //将信息通过这个对象来发送给Server out =
	 * new DataOutputStream(socket.getOutputStream());
	 * out.writeUTF(requestPacket); out.flush(); //读取服务器端数据 in = new
	 * DataInputStream(socket.getInputStr
	 * eam()); responsePacket=in.readUTF();
	 * //因数据量可能偏大先保存到临时文件中 } catch (UnknownHostException e) {
	 * e.printStackTrace(); } catch (SocketTimeoutException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace();
	 * }catch(Exception e){ e.printStackTrace(); }finally{ try{ if(in!=null)
	 * in.close(); if(out!=null) out.close(); if(socket!=null) socket.close();
	 * System.gc(); }catch(Exception e){ //待解疑问:关闭输入输出流后还能捕获抛异常么
	 * e.printStackTrace(); } } }
	 */
}
