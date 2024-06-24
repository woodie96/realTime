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
