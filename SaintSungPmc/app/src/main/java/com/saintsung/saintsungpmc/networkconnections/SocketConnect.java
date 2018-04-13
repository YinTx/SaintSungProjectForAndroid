package com.saintsung.saintsungpmc.networkconnections;



import com.saintsung.saintsungpmc.configure.Constant;

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
    int intSocketPort;
    // 连接服务器,发送登陆/下载信息.
    public String sendDate(String intSocketPort, String requestPacket) {

        if (intSocketPort.equals("") || requestPacket.equals("")) {
            return null;
        } else {
            Socket socket = null;
            DataOutputStream out = null;
            DataInputStream in = null;

            this.intSocketPort = Integer.parseInt(intSocketPort);
            try {
                socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(Constant.AddressIp, this.intSocketPort);
                socket.connect(socketAddress, 10000);
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(requestPacket);
                out.flush();
                in = new DataInputStream(socket.getInputStream());
                return in.readUTF();
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

                return e.toString();
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
}
