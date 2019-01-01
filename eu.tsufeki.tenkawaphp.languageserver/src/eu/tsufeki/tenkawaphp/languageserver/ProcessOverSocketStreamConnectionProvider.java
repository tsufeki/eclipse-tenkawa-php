package eu.tsufeki.tenkawaphp.languageserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.function.IntFunction;

import org.eclipse.lsp4e.server.StreamConnectionProvider;

public class ProcessOverSocketStreamConnectionProvider implements StreamConnectionProvider {

    private IntFunction<List<String>> commandFactory;
    private String workingDir;
    private Process process;

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ProcessOverSocketStreamConnectionProvider(IntFunction<List<String>> commandFactory, String workingDir) {
        this.workingDir = workingDir;
        this.commandFactory = commandFactory;
    }

    @Override
    public void start() throws IOException {
        var serverSocket = new ServerSocket(0);
        var socketThread = startSocketThread(serverSocket);
        var process = startProcess(serverSocket);

        while (process.isAlive() && socketThread.isAlive()) {
            try {
                socketThread.join(100);
            } catch (InterruptedException e) {
            }
        }

        if (!process.isAlive()) {
            throw new IOException("Process not started");
        }

        if (socket == null) {
            throw new IOException("Unable to make socket connection");
        }

        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    private Thread startSocketThread(ServerSocket serverSocket) {
        Thread socketThread = new Thread(() -> {
            try {
                serverSocket.setSoTimeout(10000);
                socket = serverSocket.accept();
            } catch (IOException e) {
                TenkawaPhpPlugin.logError(e);
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    TenkawaPhpPlugin.logError(e);
                }
            }
        });

        socketThread.start();
        return socketThread;
    }

    private Process startProcess(ServerSocket serverSocket) throws IOException {
        var port = serverSocket.getLocalPort();
        var command = commandFactory.apply(port);
        var processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(workingDir));
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        process = processBuilder.start();
        return process;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public InputStream getErrorStream() {
        return process != null ? process.getErrorStream() : null;
    }

    @Override
    public void stop() {
        if (process != null) {
            process.destroy();
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                TenkawaPhpPlugin.logError(e);
            }
        }
    }
}
