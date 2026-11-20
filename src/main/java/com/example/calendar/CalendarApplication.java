package com.example.calendar;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.internal.NonNull;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@Controller
public class CalendarApplication {

	public static void main(String[] args)
			throws ExecutionException, InterruptedException, IOException {
		SpringApplication.run(CalendarApplication.class, args);

	}

	@GetMapping("/")
	public static String home(Model model) throws IOException, ExecutionException, InterruptedException {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);
		String newDay = localDate.format(formatter);
		model.addAttribute("localDate",newDay);

		Firebase firebase = new Firebase();
		firebase.create();

		Map<String, ArrayList<String>> list = firebase.prelucreazaCuDublaBucla();
		model.addAttribute("list", list);

		System.out.println(list);

		return "index.html";
	}

	@GetMapping("/about.html")
	public String about(){
		return "about.html";
	}    







}
