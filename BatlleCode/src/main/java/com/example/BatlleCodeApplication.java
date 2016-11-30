package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatlleCodeApplication {

	private String url; 
	private String testname; 
	private String password; 
	
	public static void main(String[] args) {
		
		SpringApplication.run(BatlleCodeApplication.class, args);
		
		if(args.length>0){
			
			if(args[0].equals("-p")){
				System.out.println("pong");
			}
			
		} else {
			
			System.out.println("Pas d'argument en entrée");
			
		}
		
	
		
		
		
		
	}
	
	
	
	
}


