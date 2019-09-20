package org.fabri1983.refactorexample.joblogger.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is thread-safe. 
 */
public class StandardConsoleRedirector {

	private static Map<Long, StdType> stdTypeByThreadId = new HashMap<>();
	private static Map<Long, PrintStream> previousConsoleByThreadId = new HashMap<>();
	private static Map<Long, PrintStream> newConsoleByThreadId = new HashMap<>();
	private static Map<Long, OutputStream> newOutputByThreadId = new HashMap<>();
	
	public static void saveStdOut() {
		// Preserve current console
		long threadId = Thread.currentThread().getId();
		previousConsoleByThreadId.put(threadId, System.out);
		stdTypeByThreadId.put(threadId, StdType.OUT);
	}

	public static void saveStdErr() {
		// Preserve current console
		long threadId = Thread.currentThread().getId();
		previousConsoleByThreadId.put(threadId, System.err);
		stdTypeByThreadId.put(threadId, StdType.ERR);
	}
	
	public static void setCustomConsole() {
		// Set the standard output to use newConsole
		long threadId = Thread.currentThread().getId();
		ByteArrayOutputStream newOutputStream = new ByteArrayOutputStream();
		newOutputByThreadId.put(threadId, newOutputStream);
		PrintStream newPrintStream = new PrintStream(newOutputStream);
		newConsoleByThreadId.put(threadId, newPrintStream);
		
		StdType stdType = stdTypeByThreadId.get(threadId);
		if (StdType.OUT.equals(stdType)) {
			System.setOut(newPrintStream);
		}
		else if (StdType.ERR.equals(stdType)) {
			System.setErr(newPrintStream);
		}
	}

	public static String getCurrentConsoleContent() {
		return newOutputByThreadId.get(Thread.currentThread().getId()).toString();
	}
	
	public static void restoreStd() {
		// Restore back the standard console output
		long threadId = Thread.currentThread().getId();
		PrintStream previousConsole = previousConsoleByThreadId.remove(threadId);
		StdType stdType = stdTypeByThreadId.get(threadId);
		if (StdType.OUT.equals(stdType)) {
			System.setOut(previousConsole);
		}
		else if (StdType.ERR.equals(stdType)) {
			System.setErr(previousConsole);
		}
	}

	public static void closeCustomConsole() throws IOException {
		// Close buffers
		long threadId = Thread.currentThread().getId();
		newConsoleByThreadId.remove(threadId).close();
		newOutputByThreadId.remove(threadId).close();
		// Remove StdType
		stdTypeByThreadId.remove(threadId);
	}

	private enum StdType {
		OUT, ERR;
	}
}
