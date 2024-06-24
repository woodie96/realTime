package com.demo.socket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/* @ServerEndpoint
 * 웹 소켓 연결을 처리하는 클래스
 * 웹 소켓 연결 수명 주기 메서드를 구현
 * 메서드들은 웹 소켓 연결의 생성, 메시지 수신, 연결 종료 등의 이벤트를 처리
 * 웹 소켓 세션 객체를 통해 클라이언트와의 통신을 처리
 * 
 * 어노테이션 없이 Endpoint 인터페이스의 추상 메서드 구현 가능 extends Endpoint
 * 단점 : 애노테이션 방식에 비해 구현이 다소 복잡하고 엔드포인트 설정이 프로그래밍 방식으로 이루어져야 한다는 단점
 * */
@ServerEndpoint("/websocket")
public class WebSocketServer{
	private static Set<Session> clients = 
	        Collections.synchronizedSet(new HashSet<Session>());
	
	    @OnOpen
	    public void onOpen(Session session) throws IOException {
	        clients.add(session);
	        System.out.println("New session opened: " + session.getId());
	    }
	
	    @OnClose
	    public void onClose(Session session) throws IOException {
	        clients.remove(session);
	        System.out.println("Session closed: " + session.getId());
	    }
	
	    @OnMessage
	    public void onMessage(String message, Session session) throws IOException {
	        System.out.println("Message received: " + message);
	        for (Session client : clients) {
	            if (!client.equals(session)) {
	                client.getBasicRemote().sendText(message);
	            }
	        }
	    }
	
	    @OnError
	    public void onError(Session session, Throwable throwable) {
	        System.out.println("Error: " + throwable.getMessage());
	    }
}
