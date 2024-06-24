<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8;" pageEncoding="utf-8" %>
<html>
<head>
    <title>WebSocket Example</title>
    <script type="text/javascript">
        var ws;

        function connect() {
            ws = new WebSocket("ws://localhost:8080/websocket");

            ws.onopen = function() {
                log('Connected');
            };

            ws.onmessage = function(event) {
                log('Received: ' + event.data);
            };

            ws.onclose = function() {
                log('Disconnected');
            };
        }

        function send() {
            var message = document.getElementById('message').value;
            ws.send(message);
            log('Sent: ' + message);
        }

        function log(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(message));
            console.appendChild(p);
        }

        window.onload = function() {
            connect();
        };
    </script>
</head>
<body>
    <h1>WebSocket Example</h1>
    <input type="text" id="message" placeholder="Message to send">
    <button onclick="send()">Send</button>
    <div id="console" style="border:1px solid black; height:200px; overflow:auto;"></div>
</body>
</html>
