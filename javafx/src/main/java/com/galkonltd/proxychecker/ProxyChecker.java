package com.galkonltd.proxychecker;

import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." � Sydney Harris
 * Created on 10/31/2015
 *
 * @author Galkon
 */
public class ProxyChecker {

	private static final ExecutorService PROXY_WRITER = Executors.newSingleThreadExecutor();
	private static final Logger LOGGER = Logger.getLogger(ProxyChecker.class.getName());
	private static ExecutorService PROXY_CHECKER;
	private static HashMap<String, Proxy> proxyMap;
	private static ProxyCheckerUI gui;
	private static final Date date = new Date();
	private static int deadProxies;
	private static int workingProxies;

	public ProxyChecker(int threadCount) {
		deadProxies = workingProxies = 0;
		proxyMap = new HashMap<>();
		PROXY_CHECKER = Executors.newFixedThreadPool(threadCount);
		final ProxyChecker instance = this;
		EventQueue.invokeLater(() -> {
			try {
				gui = new ProxyCheckerUI(instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * parse proxies from file
	 *
	 * @param file file proxies
	 * @throws IOException exception
	 */
	public static void parseProxies(String file) throws IOException {
		File proxyFile = new File(file);
		if (!proxyFile.exists()) {
			throw new RuntimeException("Unable to find proxy file: " + proxyFile.getAbsolutePath());
		}
		LOGGER.info("Parsing proxies...");
		gui.updateConsoleLog("Parsing proxies...");
		FileInputStream stream = new FileInputStream(file);
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		String line;
		int passed = 0;
		int failed = 0;
		int duplicates = 0;
		int filtered = 0;
		while ((line = in.readLine()) != null) {
			if (line.contains(":")) {
				String[] args = line.split(":");
				if (args.length == 2) {
					String host = args[0];
					int port = Integer.parseInt(args[1]);
					if (Main.filteredPorts.contains(port)) {
						filtered++;
						continue;
					}
					Proxy proxy = new Proxy(host, port);
					if (proxyMap.containsKey(proxy.getHost() + ":" + proxy.getPort())) {
						duplicates++;
						continue;
					}
					proxyMap.put(proxy.getHost() + ":" + proxy.getPort(), proxy);
					passed++;
				} else {
					failed++;
				}
			}
		}
		in.close();
		LOGGER.info("Parsed proxy list. " + passed + " passed & " + failed + " failed with " + duplicates + " duplicates.");
		gui.updateConsoleLog("Parsed proxy list. " + passed + " passed & " + failed + " failed with " + duplicates + " duplicates.");
		gui.updateTotalProxies(proxyMap.size());
	}

	public static void verifyProxy(Proxy proxy) {
		PROXY_CHECKER.submit(() -> {
			try {
				LOGGER.info("Checking proxy: " + proxy.toString());
				gui.updateConsoleLog("Checking proxy: " + proxy.toString());
				if (proxy.check()) {
					writeWorkingProxy(proxy);
					workingProxies++;
					gui.updateLiveProxies(workingProxies);
				} else {
					deadProxies++;
					gui.updateDeadProxies(deadProxies);
				}
				LOGGER.info("Proxy checking status: checked " + (workingProxies + deadProxies) + "/" + proxyMap.size() + " proxies, " + workingProxies + " working, " + deadProxies + " dead.");
				gui.updateProgress(workingProxies + deadProxies, proxyMap.size());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public void verifyProxies() throws IOException {
		LOGGER.info("Checking " + proxyMap.size() + " proxies...");
		gui.updateConsoleLog("Checking " + proxyMap.size() + " proxies...");
		if (Main.checkGoogle) {
			LOGGER.info("Checking connection to google.com for proxy verification...");
			gui.updateConsoleLog("Checking connection to google.com for proxy verification...");
		}
		proxyMap.values().forEach(ProxyChecker::verifyProxy);
	}

	private static void writeWorkingProxy(Proxy proxy) throws IOException {
		PROXY_WRITER.submit(() -> {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter("working_" + (date.getMonth() + 1) + "-" + date.getDay() + "-" + date.getTime() + ".txt", true));
				if (workingProxies > 0) {
					out.write("\n");
				}
				out.write(proxy.getHost() + ":" + proxy.getPort());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}


}
