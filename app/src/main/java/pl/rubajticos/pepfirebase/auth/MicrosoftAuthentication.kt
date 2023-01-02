package pl.rubajticos.pepfirebase.auth

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

@Singleton
class MicrosoftAuthentication @Inject constructor() {

    suspend fun auth(): Flow<Unit> = callbackFlow {
        val successListener =
            OnSuccessListener<AuthResult> { Timber.d("MRMR Success: ${it?.user?.email}") }
        val failureListener =
            OnFailureListener { Timber.d("MRMR Failure: ${it.localizedMessage}") }

        val pendingResult = FirebaseAuth.getInstance().pendingAuthResult
        pendingResult?.let {
            it.addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener)
        } ?: run {
            Timber.d("MRMR No pending result")
        }

        awaitClose {
            Timber.d("MRMR Await close :) ")
        }
    }
}