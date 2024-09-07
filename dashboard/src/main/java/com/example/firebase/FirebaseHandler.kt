package com.example.firebase

import android.content.Context
import com.example.login.data.models.Estacionamento
import com.example.login.data.models.Reserva
import com.example.login.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

class FirebaseHandler(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun createUser(email: String, password: String, nome: String, cpf: String, onComplete: (Task<AuthResult>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val userData = hashMapOf(
                        "id" to user.uid,
                        "nome" to nome,
                        "cpf" to cpf,
                        "email" to email,
                        "senha" to password,
                    )
                    db.collection("usuario").document(user.uid).set(userData).addOnCompleteListener { firestoreTask ->
                        onComplete(task)
                    }
                }
            } else {
                onComplete(task)
            }
        }
    }

    fun loginUser(email: String, password: String, onComplete: (Task<AuthResult>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            onComplete(task)
        }
    }

    fun updateUserPlan(planoId: String, onComplete: (Task<Void>) -> Unit) {
        val user = auth.currentUser
        user?.let {
            db.collection("usuario").document(user.uid)
                .update("plano", planoId)
                .addOnCompleteListener { task ->
                    onComplete(task)
                }
        }
    }

    fun makeReservation(estacionamentoId: String, horaReserva: Timestamp, onComplete: (Task<DocumentReference>) -> Unit) {
        val user = auth.currentUser
        user?.let {
            val reservationData = hashMapOf(
                "usuarioID" to user.uid,
                "estacionamentoID" to estacionamentoId,
                "horaReserva" to horaReserva
            )
            db.collection("reservas").add(reservationData).addOnCompleteListener { task ->
                onComplete(task)
            }
        }
    }

    fun getUserReservations(userId: String, onComplete: (List<Reserva>?, Exception?) -> Unit) {
        db.collection("reservas")
            .whereEqualTo("usuarioID", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val reservations = snapshot.documents.mapNotNull { it.toObject(Reserva::class.java) }
                onComplete(reservations, null)
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception)
            }
    }

    fun getEstacionamento(estacionamentoId: String, onComplete: (Estacionamento?, Exception?) -> Unit) {
        db.collection("estacionamento").document(estacionamentoId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val estacionamento = documentSnapshot.toObject(Estacionamento::class.java)
                    onComplete(estacionamento, null)
                } else {
                    onComplete(null, Exception("Estacionamento não encontrado"))
                }
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception)
            }
    }

    fun getEstacionamentos(onComplete: (List<Estacionamento>?, Exception?) -> Unit) {
        db.collection("estacionamento")
            .get()
            .addOnSuccessListener { snapshot ->
                val estacionamentos = snapshot.documents.mapNotNull { document ->
                    // Converte o documento em um objeto Estacionamento e atribui o ID do documento
                    val estacionamento = document.toObject(Estacionamento::class.java)
                    estacionamento?.copy(id = document.id)
                }
                onComplete(estacionamentos, null)
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception)
            }
    }

    fun fetchUserDetails(userId: String, onComplete: (User?, Exception?) -> Unit) {
        db.collection("usuario").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    onComplete(user, null)
                } else {
                    onComplete(null, Exception("User document does not exist"))
                }
            }
            .addOnFailureListener { exception ->
                onComplete(null, exception)
            }
    }

    fun logoutUser() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
