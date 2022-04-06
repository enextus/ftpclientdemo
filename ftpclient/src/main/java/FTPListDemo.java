import java.io.IOException;
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

		String server = "172.20.0.1";
		int port = 4567;
		String user = "ftpuser";
		String pass = "Pw1337,.-";

/*		scamp.ftp.user=ftpuser
		scamp.ftp.password=Pw1337,.-
		scamp.ftp.url=192.168.12.12
		scamp.ftp.port=4567*/

		System.out.println("Try to connect to the FTP Server!");

		FTPClient ftpClient = new FTPClient();

		try {

			ftpClient.connect(server, port);
			showServerReply(ftpClient);

			int replyCode = ftpClient.getReplyCode();
			System.out.println("replyCode" + replyCode);

			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed");
				return;
			}

			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);

			if (!success) {
				System.out.println("Could not login to the server");
				return;
			}

			System.out.println("Connected\n");

			FTPFile[] files0 = ftpClient.mlistDir("/home/ftpuser");

			System.out.println("Connected 2\n");

			// List files and directories
			FTPFile[] files1 = ftpClient.listFiles("/home/ftpuser");
			printFileDetails(files1);

			// uses simpler methods
			String[] files2 = ftpClient.listNames();
			printNames(files2);

		}
		catch (IOException ex) {
			System.out.println("Oops! Something wrong happened");
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
		for (FTPFile file : files) {
			String details = file.getName();
			if (file.isDirectory()) {
				details = "[" + details + "]";
			}
			details += "\t\t" + file.getSize();
			details += "\t\t" + dateFormatter.format(file.getTimestamp().getTime());

			System.out.println(details);
		}
	}

	private static void printNames(String[] files) {
		if (files != null && files.length > 0) {
			for (String aFile : files) {
				System.out.println(aFile);
			}
		}
	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}
}