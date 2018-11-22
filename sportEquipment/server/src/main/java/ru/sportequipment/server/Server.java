package ru.sportequipment.server;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.ActionFactory;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Visitor;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.entity.enums.RoleEnum;
import ru.sportequipment.exception.ClientException;
import ru.sportequipment.exception.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static Logger logger = LogManager.getLogger(Server.class);
    private static final int DEFAULT_PORT_NUMBER = 8844;
    private static final int SAME_TIME_REQUESTS_COUNT = 1;

    private static AtomicInteger connectionsCount = new AtomicInteger(0);
    private HashMap<Integer, ClientThread> clientThreads;
    private int portNumber;
    private AtomicBoolean isServerWorking;
    private Semaphore semaphore;

    public Server() {
        this.portNumber = DEFAULT_PORT_NUMBER;
        this.clientThreads = new HashMap<>();
        this.isServerWorking = new AtomicBoolean(false);
        this.semaphore = new Semaphore(SAME_TIME_REQUESTS_COUNT);
    }

    public Server(int port) {
        this.portNumber = port;
        this.clientThreads = new HashMap<>();
        this.isServerWorking = new AtomicBoolean(false);
        this.semaphore = new Semaphore(SAME_TIME_REQUESTS_COUNT);
    }

    public void start() throws ServerException {
        isServerWorking.set(true);
        logger.info("Server started");
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (isServerWorking.get()) {
                logger.info("Server is waiting for Clients on port number: " + portNumber);

                Socket socket = null;
                try {
                    /*This method waits until a client connects to the server on the given port.*/
                    socket = serverSocket.accept();
                /*When the ServerSocket invokes accept(), the method does not return until a client connects.
                 After a client does connect, the ServerSocket creates a new Socket on an unspecified port and
                 returns a reference to this new Socket. A TCP connection now exists between the client and
                 the server, and communication can begin.*/
                } catch (IOException e) {
                    throw new ServerException("Can not accept Server Socket.", e);
                }
                if (!isServerWorking.get()) {
                    break;
                }

                try {
                    ClientThread client = new ClientThread(socket);
                    clientThreads.put(client.getClientId(), client);
                    client.start();
                } catch (ClientException e) {
                    logger.error("Can not log in client: " + e);
                }
            }
        } catch (IOException e) {
            throw new ServerException("Can not init ServerSocket on port number: " + portNumber, e);
        }

        isServerWorking.set(false);
        logger.info("Server stopped!");

        clientThreads.forEach((id, client) -> {
            try {
                client.disconnect();
            } catch (ClientException e) {
                logger.error("Error while closing clients socket's threads. ", e);
            }
        });

    }

    private void removeClient(int clientId) {
        clientThreads.remove(clientId);
    }


    class ClientThread extends Thread {
        private Socket socket;
        private ObjectInputStream socketInput;
        private ObjectOutputStream socketOutput;

        private Integer clientId;
        private Visitor visitor;

        public ClientThread(Socket socket) throws ClientException {
            this.clientId = connectionsCount.incrementAndGet();
            this.socket = socket;

            try {
                socketOutput = new ObjectOutputStream(socket.getOutputStream());
                socketInput = new ObjectInputStream(socket.getInputStream());

                CommandRequest request = receiveRequest(socketInput);
                visitor = new Visitor();
                visitor.setRole(RoleEnum.GUEST);

            } catch (IOException e) {
                throw new ClientException("Error while creating new Input / output Streams: " + e);
            }
        }

        @Override
        public void run() {
            boolean keepGoing = true;
            CommandRequest request;
            while (keepGoing) {
                try {
                    request = receiveRequest(socketInput);
                } catch (ClientException e) {
                    logger.error(e);
                    break;
                }

                CommandResponse response = null;
                ActionFactory actionFactory = new ActionFactory();
                ActionCommand command = actionFactory.defineCommand(request);


                response = command.execute(request, response);

                try {
                    if (response != null) {
                        answer(response, clientId);
                    } else {
                        answer(new CommandResponse(ResponseStatus.BAD_REQUEST), clientId);
                    }
                } catch (InterruptedException e) {
                    logger.error("Can not send response " + e);
                }
            }

            try {
                semaphore.acquire();
                removeClient(clientId);
            } catch (InterruptedException e) {
                logger.error("Can not remove client before disconnection. " + e);
            }
            semaphore.release();

            try {
                disconnect();
            } catch (ClientException e) {
                logger.error("Can not disconnect. " + e);
            }
        }

        private void answer(CommandResponse response, int clientId) throws InterruptedException {

            semaphore.acquire();

            ClientThread client = clientThreads.get(clientId);
            try {
                client.sendResponse(response);
            } catch (ClientException e) {
                logger.error("Cannot answer to client with id = " + clientId);
                clientThreads.remove(clientId);
            }

            semaphore.release();
        }


        private void disconnect() throws ClientException {
            try {
                if (socketInput != null) {
                    socketInput.close();
                }
            } catch (IOException e) {
                throw new ClientException("Error while closing socketInput" + e);
            }
            try {
                if (socketOutput != null) {
                    socketOutput.close();
                }
            } catch (IOException e) {
                throw new ClientException("Error while closing socketOutput" + e);
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new ClientException("Error while closing socket" + e);
            }
            connectionsCount.decrementAndGet();
        }

        private void sendResponse(CommandResponse response) throws ClientException {
            if (!socket.isConnected()) {
                disconnect();
                throw new ClientException("Socket is closed!");
            }

            try {
                socketOutput.writeObject(response);
            } catch (IOException e) {
                throw new ClientException("Error while sending message from " + visitor);
            }
        }

        private CommandRequest receiveRequest(ObjectInputStream inputStream) throws ClientException {
            try {
                return (CommandRequest) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new ClientException("Can not receive request!");
            }
        }

        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

        public ObjectInputStream getSocketInput() {
            return socketInput;
        }

        public void setSocketInput(ObjectInputStream socketInput) {
            this.socketInput = socketInput;
        }

        public ObjectOutputStream getSocketOutput() {
            return socketOutput;
        }

        public void setSocketOutput(ObjectOutputStream socketOutput) {
            this.socketOutput = socketOutput;
        }

        public int getClientId() {
            return clientId;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }

        public Visitor getVisitor() {
            return visitor;
        }

        public void setVisitor(Visitor visitor) {
            this.visitor = visitor;
        }
    }
}
