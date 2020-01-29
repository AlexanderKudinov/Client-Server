import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

// Read/Write - ����������/������ �� 1 �������
// BufferedReader/BufferedWriter - ��� ������ � �������, ���������� ��� Read/Write (��� �������� �������������)
// InputStream/OutputStream - ��� ������ � ���������
// Scanner ����� ������� ������, ��� BufferedReader 


public class Client {

	private static Socket clientSocket;  // ����� ��� ������� (Ip-����� + ����)
	private static BufferedReader reader;  // ����� ��� ������ � �������
	private static BufferedReader in;  // ����� ������ �� ������
	private static BufferedWriter out;  // ����� ������ � �����
	
	public static void main(String[] args) {
		try {
			try {
				System.out.println("������ �������!");
				clientSocket = new Socket("localhost", 8080);  // ����������� ������ � �������
				reader = new BufferedReader(new InputStreamReader(System.in));  // ��� ������ � �������
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // ��� �������� ��������� � �������
				out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));  // ��� �������� ��������� �� ������
				while (true) {
					String word = reader.readLine();  // ��� ��������� � �������
					// ���� ������ ������, �� ��������� �������
					if (word.equals(""))
						break;
					out.write(word + "\n");  // + "\n", ����� � ������� ������� ������ (����� ����� ���������� ����� ����� ������)
					out.flush();
					String serverWord = in.readLine();  // ��� ��������� �� �������
					System.out.println(serverWord);  // ������ ������ �������
				}
			}
			finally {
				clientSocket.close();
				reader.close();
				in.close();
				out.close();
				System.out.println("������ ������!");
			}
		}
		catch (IOException e) {			
			e.printStackTrace();
		}

	}

}
