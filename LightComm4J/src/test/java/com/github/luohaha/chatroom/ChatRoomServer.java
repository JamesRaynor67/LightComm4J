package com.github.luohaha.chatroom;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.github.luohaha.connection.Conn;
import com.github.luohaha.param.ServerParam;
import com.github.luohaha.server.LightCommServer;

public class ChatRoomServer {
	public static void main(String[] args) {
		ServerParam param = new ServerParam("localhost", 8888);
		Set<Conn> conns = new HashSet<>();
		param.setBacklog(128);
		param.setOnAccept(conn -> {
			try {
				String m = conn.getRemoteAddress().toString() + " " + "is online!";
				conns.add(conn);
				conns.forEach(c -> {
					try {
						c.write(m.getBytes());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		param.setOnRead((conn, msg) -> {
			try {
				String m = conn.getRemoteAddress().toString() + " : " + new String(msg);
				conns.forEach(c -> {
					try {
						c.write(m.getBytes());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		param.setOnClose(conn -> {
			try {
				conns.remove(conn);
				String m = conn.getRemoteAddress().toString() + " " + "is offline!";
				conns.forEach(c -> {
					try {
						c.write(m.getBytes());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		LightCommServer server = new LightCommServer(param, 4);
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}