package br.edu.ifsuldeminas.mch.apptransportecargas.db;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifsuldeminas.mch.apptransportecargas.model.User;

public class UserDAO {
    private static final String USERS = "usuario";
    private static final String NOME = "nome";
    private static final String USERNAME = "username";
    private static final String SENHA = "senha";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DAOObserver observer;

    public UserDAO(DAOObserver observer) {
        this.observer = observer;
    }

    public boolean saveUser(User user) {
        Map<String, Object> usersMap = usersToMap(user);
        firestore.collection(USERS).add(usersMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        observer.saveOK();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        observer.saveErro();
                    }
                });
        return true;
    }

    private Map<String, Object> usersToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put(NOME, user.getNome());
        map.put(USERNAME, user.getUsername());
        map.put(SENHA, user.getSenha());
        return map;
    }

    public void loadUsers() {
        CollectionReference usersCollection = firestore.collection(USERS);
        usersCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<User> usersList = new ArrayList<>();
                        queryDocumentSnapshots.forEach(document -> {
                            User user = document.toObject(User.class);
                            usersList.add(user);
                        });
                        observer.loadOk(usersList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        observer.loadErro();
                    }
                });
    }

    public void updateUserPassword(String username, String newPassword) {
        CollectionReference usersCollection = firestore.collection(USERS);
        usersCollection.whereEqualTo(USERNAME, username).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        observer.updateErro();
                        return;
                    }
                    queryDocumentSnapshots.getDocuments().forEach(document -> {
                        document.getReference().update(SENHA, newPassword)
                                .addOnSuccessListener(aVoid -> {
                                    observer.updateOK();
                                    loadUsers();
                                })
                                .addOnFailureListener(e -> observer.updateErro());
                    });
                })
                .addOnFailureListener(e -> observer.updateErro());
    }


}
