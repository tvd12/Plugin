package com.tvd.gamview.ext.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BashUtils {

	public static void run(String pCommand) {
		Runtime run = Runtime.getRuntime();
		Process pr;
		try {
			pr = run.exec(pCommand);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(
					new InputStreamReader(pr.getInputStream()));
			String line = buf.readLine();
			while ( line != null ) {
			  System.out.println(line);
			  line = buf.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		


	}
	
}
