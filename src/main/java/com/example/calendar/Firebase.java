package com.example.calendar;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Date;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Firebase {

    private FirebaseApp firebaseApp;

    public FirebaseApp create() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream
                        ("C:\\Users\\Home-PC\\Downloads\\calendar" +
                                "\\calendar\\src\\main\\resources\\calendar-bd858-firebase-adminsdk-fbsvc-e1665100ad.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        boolean exists = FirebaseApp.getApps().stream().anyMatch(app -> app.getName().equals("calendar"));

        if (!exists) {
            firebaseApp = FirebaseApp.initializeApp(options, "calendar");
        } else {
            firebaseApp = FirebaseApp.getInstance("calendar");
        }
       return firebaseApp;
    }

    public ArrayList<String> citesteTipulEvenimentelor()
            throws IOException, ExecutionException, InterruptedException {
        if (firebaseApp == null) {
            create();
        }
        ArrayList<String> list = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        ApiFuture<QuerySnapshot> future = db.collection("Evenimente").get();

        System.out.println("****************************************************************");
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            if(!document.getData().isEmpty()){
                list.add(document.getId());
            }
        }
        return  list;
    }

    public Map<String,Object> citesteEvenimentesiDataPrimitiv() throws IOException, ExecutionException, InterruptedException {
        if (firebaseApp == null) {
            create();
        }
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        ApiFuture<QuerySnapshot> future = db.collection("Evenimente").get();

        QuerySnapshot querySnapshot = future.get();
        Map<String, Object> map = new HashMap<>();

        for (QueryDocumentSnapshot document : querySnapshot) {
            // 1. Extragi câmpul de tip String
            map.put(document.getId(),document.getData());
            // 2. Extragi câmpul de tip Timestamp
        }

        return map;
    }



    public Map<String, ArrayList<String>> prelucreazaCuDublaBucla() throws ExecutionException, InterruptedException, IOException {
        if (firebaseApp == null) {
            create();
        }
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        ApiFuture<QuerySnapshot> future = db.collection("Evenimente").get();
        QuerySnapshot querySnapshot = future.get();

        Map<String, ArrayList<String>> map_final = new HashMap<>();
        LocalDate dataCorecta = null;


        // 1. Prima buclă: parcurge documentele din colecție (ex: documentul "Istorice")
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            if (document.exists()) {
                // Extragem toate câmpurile ca un Map (cheie = label-ul diferit, valoare = data)
                Map<String, Object> map = document.getData();
                System.out.println(map);
                System.out.println(document.getId());
                if (map != null) {
                    ArrayList<String> evenimenteDinAcestTip = new ArrayList<>();
                    // 2. A doua buclă: parcurge fiecare cheie (label) și valoare din document
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String labelEveniment = entry.getKey();
                        Object dataEveniment = entry.getValue();
                        if (dataEveniment instanceof Timestamp timestamp) {
                            LocalDate dataCorecta_1 = timestamp.toDate().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();

                            MonthDay ziLunaEveniment = MonthDay.from(dataCorecta_1);
                            MonthDay ziLunaAzi = MonthDay.now();

                            if (ziLunaEveniment.equals(ziLunaAzi)) {
                                evenimenteDinAcestTip.add(labelEveniment);
                            }
                        }

                    }
                        if (!evenimenteDinAcestTip.isEmpty()) {
                            map_final.put(document.getId(), evenimenteDinAcestTip);
                        }
                    }
                }
            }
            return map_final;
        }


    public void test_Tip_Eveniment(ArrayList<String> list, Map<String, Object>  map){
        System.out.println("TIPUL EVENIMENTELOR: ");
        for (String s : list) {
            System.out.print(s + " - ");
        }
        System.out.println("EVENIMENTE");
        for (Map.Entry<String, Object> entry : map.entrySet()){
            String nume = entry.getKey();
            Object data = entry.getValue();

            System.out.print("Nume: " + nume + "\tData: "+data +"\n");
        }
    }
}
