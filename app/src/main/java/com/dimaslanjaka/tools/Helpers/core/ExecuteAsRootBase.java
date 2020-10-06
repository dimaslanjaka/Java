package com.dimaslanjaka.tools.Helpers.core;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @link http://muzikant-android.blogspot.fr/2011/02/how-to-get-root-access-and-execute.html
 * @example ExecuteAsRootBase.execute(" ls " + BonusActivity.general_filePath + BonusActivity.general_fileName, true);
 */
public abstract class ExecuteAsRootBase {
	private static boolean retval = false;
	private static Process suProcess = null;
	private static DataOutputStream os = null;
	private static DataInputStream osRes = null;

	public static void close() {
		if (suProcess != null) {
			try {
				os.writeBytes("exit\n");
				os.flush();
				os.close();
				osRes.close();
				int exit_code = suProcess.waitFor();
				if (exit_code != 0)
					Log.w("su", "su Process exited with code " + exit_code);


			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			suProcess = null;
			os = null;
			osRes = null;
		}
		retval = false;
	}

	public static boolean canRunRootCommands() {

		try {
			if (retval && suProcess != null) {
				try {
					suProcess.exitValue();
					close();
				} catch (IllegalThreadStateException e) {
					return true;
				} catch (Exception e) {
					// Process != null but not existing anymore
					close();
				}
			}

			suProcess = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh"});

			os = new DataOutputStream(
							suProcess.getOutputStream());
			osRes = new DataInputStream(
							suProcess.getInputStream());

			if (null != os) {
				// Getting the id of the current user to check if this is root
				os.writeBytes("id\n");
				os.flush();

				String currUid = osRes.readUTF(); //.readline()
				if (null == currUid) {
					retval = false;
					Log.d("ROOT", "Can't get root access or denied by user");
				} else if (currUid.contains("uid=0")) {
					retval = true;
					Log.d("ROOT", "Root access granted");
				} else {
					retval = false;
					Log.d("ROOT", "Root access rejected: " + currUid);
				}
			}
		} catch (Exception e) {
			// Can't get root !
			// Probably broken pipe exception on trying to write to output
			// stream (os) after su failed, meaning that the device is not
			// rooted

			retval = false;
			Log.d("ROOT", "Root access rejected [" + e.getClass().getName()
							+ "] : " + e.getMessage());
		}

		return retval;
	}

	public static void execute(String command) {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add(command);
		execute(commands, false);
	}

	public static void execute(ArrayList<String> commands) {
		execute(commands, false);
	}

	public static List<String> execute(String command, boolean show_result) {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add(command);

		List<List<String>> results = execute(commands, show_result);
		if (results != null && results.size() > 0)
			return results.get(0);
		else
			return null;
	}

	public static List<List<String>> execute(ArrayList<String> commands, boolean show_result) {

		try {
			List<List<String>> results = new ArrayList<List<String>>();

			if (null != commands && commands.size() > 0) {
				if (!retval && !canRunRootCommands())
					throw new SecurityException();


				// Execute commands that require root access
				for (String currCommand : commands) {
					Log.d("Sudo command", "Executing \"" + currCommand + "\"");
					os.writeBytes(currCommand + "\n");
					os.flush();
					Thread.sleep(10);

					if (show_result) {
						byte[] buffer = new byte[4096];
						int read;
						String fatStr = "";
						//read method will wait forever if there is nothing in the stream
						//so we need to read it in another way than while((read=stdout.read(buffer))>0)
						while (true) {
							read = osRes.read(buffer);
							fatStr = fatStr + (new String(buffer, 0, read));
							if (read < 4096) {
								//we have read everything
								break;
							}
						}
						results.add(Arrays.asList(fatStr.split("\\r?\\n")));
					}
				}
			}
			return results;

		} catch (IOException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (SecurityException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (Exception ex) {
			Log.w("ROOT", "Error executing internal operation", ex);
		}

		return null;
	}

	public abstract ArrayList<String> getCommandsToExecute();
}