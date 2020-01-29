import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

// Read/Write - считывание/запись по 1 символу
// BufferedReader/BufferedWriter - для работы с текстом, надстройка над Read/Write (для удобного использования)
// InputStream/OutputStream - для работы с остальным
// Scanner имеет меньший буффер, чем BufferedReader 


public class Client {

	private static Socket clientSocket;  // сокет для общения (Ip-адрес + порт)
	private static BufferedReader reader;  // ридер для чтения с консоли
	private static BufferedReader in;  // поток чтение из сокета
	private static BufferedWriter out;  // поток записи в сокет
	
	public static void main(String[] args) {
		try {
			try {
				System.out.println("Клиент запущен!");
				clientSocket = new Socket("localhost", 8080);  // запрашиваем доступ у сервера
				reader = new BufferedReader(new InputStreamReader(System.in));  // для чтения с консоли
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // для принятия сообщений с сервера
				out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));  // для отправки сообщений на сервер
				while (true) {
					String word = reader.readLine();  // ждёт сообщения с консоли
					// Если пустая строка, то закрываем клиента
					if (word.equals(""))
						break;
					out.write(word + "\n");  // + "\n", чтобы в сервере считать строку (иначе будет бесконечно ждать конца строки)
					out.flush();
					String serverWord = in.readLine();  // ждёт сообщения от сервера
					System.out.println(serverWord);  // печать ответа сервера
				}
			}
			finally {
				clientSocket.close();
				reader.close();
				in.close();
				out.close();
				System.out.println("Клиент закрыт!");
			}
		}
		catch (IOException e) {			
			e.printStackTrace();
		}

	}

}
