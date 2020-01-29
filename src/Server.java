import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static Socket clientSocket;  // ����� ��� ������� (Ip-����� + ����)
	private static ServerSocket server;
	private static BufferedReader in;  // ����� ������ �� ������
	private static BufferedWriter out;  // ����� ������ � �����
	
	
	public static void main(String[] args) {
			try {
				String word = "";
				try {
					server = new ServerSocket(8080); // ����������� � �������
					System.out.println("������ �������!");
					clientSocket = server.accept();  // ��� ��������� ������� (������� ����)
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
					try {
						while (true) {
							word = in.readLine();
							Calculator calculator = new Calculator();
							out.write(calculator.calculate(word) + "\n");
							out.flush();
						}
					} finally {
						clientSocket.close();
						in.close();
						out.close();
					}
				} finally {
					System.out.println("������ ������!");
					server.close();		
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
	}

}
