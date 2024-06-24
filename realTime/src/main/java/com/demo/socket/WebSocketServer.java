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
	/*
	 * Set<T> 를 사용하는 이유 > 세션의 중복을 막기 위해서 사용
	 * Set<T> 내부적으로  HashMap을 사용하여 구현되어있지만 HashMap은 키의 중복을 허용함
	 * */
	//웹 소켓 연결된 클라이언트의 세션을 관리하기 위한 것
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	
	//웹 소켓 연결된 클라이언트의 세션을 저장하기 위한 집합(Set) 객체.
	//Session 객체는 웹 소켓 연결에 대한 정보를 가지고 있음
	//Collections.synchronizedSet(new HashSet<Session>()) > clients 집합을 스레드 안전하게 만들기 위한 것
	//HashSet은 동기화되지 않은 집합 구현체이므로, 멀티 스레드 환경에서 사용할 경우 데이터 경합(race condition) 문제가 발생할 수 있음
	//Collections.synchronizedSet(...) 메서드를 사용하면 HashSet을 래핑하여 스레드 안전한 집합 구현체를 만들 수 있음

	
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
