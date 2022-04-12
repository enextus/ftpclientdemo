import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * An example program that demonstrates how to list files and directories
 * on an FTP server using Apache Commons Net API.
 * @author www.codejava.net
 */
public class FTPListDemo {

	public static void main(String[] args) {

		String server = "localhost";
		int port = 4567;
		String user = "ftpuser";
		String pass = "Pw1337,.-";

		System.out.println("Try to connect to the FTP Server!");
		FTPClient ftpClient = new FTPClient();

		try {
			ftpClient.connect(server, port);
			int reply = ftpClient.getReplyCode();
			System.out.println(":: ReplyCode: " + reply);
			// enter the passive mode
			ftpClient.enterLocalPassiveMode();

			System.out.println("____________________________");
			System.out.println("::: " + ftpClient.getStatus());
			System.out.println("____________________________");
			System.out.println("::: " + ftpClient.getLocalAddress());

			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed! FTP server refused connection.");
				return;
			}

			boolean success = ftpClient.login(user, pass);
			if (!success) {
				System.out.println("Could not login to the server");
				ftpClient.disconnect();
				return;
			}

			System.out.println("Login successful? : " + success);
			System.out.println("ftpClient: " + ftpClient);
			showServerReply(ftpClient);
			System.out.println("FTP Server connected!\n");

			FTPFile[] files = ftpClient.listFiles("/");
			for (FTPFile file : files) {
				System.out.println(file.getName());
			}

			// uses simpler methods
			String[] files2 = ftpClient.listNames("/");
			printNames(files2);

			// List files and directories
			FTPFile[] files1 = ftpClient.listFiles("/");
			printFileDetails(files1);

		}
		catch (IOException ex) {
			System.out.println("Oops! Something wrong happened. " + "I/O errortest: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			// logs out and disconnects from server
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private static void printFileDetails(FTPFile[] files) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("\n");
		for (FTPFile file : files) {
			String details = file.getName();
			if (file.isDirectory()) {
				details = "[" + details + "]";
			}
			details += "\t\t" + file.getSize();
			details += "\t\t" + dateFormatter.format(file.getTimestamp().getTime());

			System.out.println("Details: " + details);
		}
	}

	private static void printNames(String[] files) {
		System.out.println("\n");
		if (files != null && files.length > 0) {
			for (String aFile : files) {
				System.out.println("aFile: " + aFile);
			}
		}
	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		System.out.println("\n");
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER REPLY: " + aReply);
			}
		}
	}

}
