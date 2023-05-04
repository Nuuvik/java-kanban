package server;

import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    HttpServer server;

    public HttpTaskServer(TaskManager taskService) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TasksRequests(taskService));

        server.start();
        System.out.println("HttpTaskServer started at http://localhost:8080");
    }

    public void stop() {
        server.stop(0);
    }
}