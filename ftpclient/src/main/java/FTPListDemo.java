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

		//String server = "172.20.0.1";
		String server = "";
		int port = 4567;
		String user = "ftpuser";
		String pass = "Pw1337,.-";

		System.out.println("Try to connect to the FTP Server!");

		FTPClient ftpClient = new FTPClient();
		// FTPSClient ftpClient = new FTPSClient("TLS", false);

		try {

			ftpClient.connect(server, port);

/*			ftpClient.execPBSZ(0);
			ftpClient.execPROT("P");*/

			System.out.println("____________ 1 ________________");

			showServerReply(ftpClient);

			System.out.println("____________ 2 ________________");

			int replyCode = ftpClient.getReplyCode();
			System.out.println("Server reply code: " + replyCode);

			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Connect failed!");
				return;
			}

			boolean success = ftpClient.login(user, pass);

			System.out.println("success: " + success);

			showServerReply(ftpClient);

			System.out.println("ftpClient: " + ftpClient);

			if (!success) {
				System.out.println("Could not login to the server");
				return;
			}

			System.out.println("Connected!\n");

			// List files and directories
			FTPFile[] files1 = ftpClient.listFiles("/");
			printFileDetails(files1);

			// uses simpler methods
			String[] files2 = ftpClient.listNames("/");
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

	static void listDirectory(FTPClient ftpClient, String parentDir,
			String currentDir, int level) throws IOException {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}
		FTPFile[] subFiles = ftpClient.listFiles(dirToList);
		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".")
						|| currentFileName.equals("..")) {
					// skip parent directory and directory itself
					continue;
				}
				for (int i = 0; i < level; i++) {
					System.out.print("\t");
				}
				if (aFile.isDirectory()) {
					System.out.println("[" + currentFileName + "]");
					listDirectory(ftpClient, dirToList, currentFileName, level + 1);
				} else {
					System.out.println(currentFileName);
				}
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

			System.out.println("Details: " + details);
		}
	}

	private static void printNames(String[] files) {
		if (files != null && files.length > 0) {
			for (String aFile : files) {
				System.out.println("aFile: " + aFile);
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