import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// hello this is a change in the file

public class Client {
    private Socket socket;
    private DataInputStream receiveBuffer;
    private DataOutputStream sendBuffer;

    public void start(String address, int port) {
        try {
            String register = "register:(?<id>\\d+):(?<name>[a-zA-Z]+):(?<money>\\d+)";
            String login = "login:(?<id>\\d+)";
            String showPrice = "get\\sprice:(?<name>\\S+)";
            String showQuantity = "get\\squantity:(?<name>\\S+)";
            String money = "get money";
            String chargeMoney = "charge:(?<money>\\d+)";
            String buyShoes = "purchase:(?<name>\\S+):(?<number>\\d+)";
            socket = new Socket(address, port);
            receiveBuffer = new DataInputStream(socket.getInputStream());
            sendBuffer = new DataOutputStream(socket.getOutputStream());

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String command = scanner.nextLine();
                sendBuffer.writeUTF(command);

                if (getMatcher(command, register).matches()) {
                    Matcher matcher = getMatcher(command, register);
                    if (matcher.find()) {
                        sendBuffer.writeUTF(matcher.group("id"));
                        sendBuffer.writeUTF(matcher.group("name"));
                        sendBuffer.writeUTF(matcher.group("money"));
                        String response = receiveBuffer.readUTF();
                        System.out.println(response);
                    }
                } else if (getMatcher(command, login).matches()) {
                    Matcher matcher = getMatcher(command, login);
                    if (matcher.find()) {
                        sendBuffer.writeUTF(matcher.group("id"));
                        String response = receiveBuffer.readUTF();
                        System.out.println(response);
                    }
                } else if (command.equals("logout")) {
                    String response = receiveBuffer.readUTF();
                    System.out.println(response);
                } else if (getMatcher(command, showPrice).matches()) {
                    Matcher matcher = getMatcher(command, showPrice);
                    if (matcher.find()) {
                        sendBuffer.writeUTF(matcher.group("name"));
                        String response = receiveBuffer.readUTF();
                        System.out.println(response);
                    }
                } else if (getMatcher(command, showQuantity).matches()) {
                    Matcher matcher = getMatcher(command, showQuantity);
                    if (matcher.find()) {
                        sendBuffer.writeUTF(matcher.group("name"));
                        String response = receiveBuffer.readUTF();
                        System.out.println(response);
                    }
                } else if (command.equals(money)) {
                    String response = receiveBuffer.readUTF();
                    System.out.println(response);
                } else if (getMatcher(command, chargeMoney).matches()) {
                    Matcher matcher = getMatcher(command, chargeMoney);
                    if (matcher.find()) {
                        sendBuffer.writeUTF(matcher.group("money"));
                        String response = receiveBuffer.readUTF();
                        System.out.println(response);
                    }
                } else if (getMatcher(command, buyShoes).matches()) {
                    Matcher matcher = getMatcher(command, buyShoes);
                    if (matcher.find()) {
                        sendBuffer.writeUTF(matcher.group("name"));
                        sendBuffer.writeUTF(matcher.group("number"));
                        String response = receiveBuffer.readUTF();
                        System.out.println(response);
                    }
                } else System.out.println("Invalid command.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.start("localhost", 5000);
    }

    private static Matcher getMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}