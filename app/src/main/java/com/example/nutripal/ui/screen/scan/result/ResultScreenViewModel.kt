package com.example.nutripal.ui.screen.scan.result

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.data.remote.request.Content
import com.example.nutripal.data.remote.request.GenerationConfig
import com.example.nutripal.data.remote.request.Part
import com.example.nutripal.data.remote.request.SafetySetting
import com.example.nutripal.data.remote.request.StreamGenerateContentRequest
import com.example.nutripal.data.remote.request.SystemInstruction
import com.example.nutripal.data.remote.response.Profile
import com.example.nutripal.data.remote.response.vertex.VertexResponse
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.example.nutripal.data.remote.retrofit.RefreshAuthTokenService
import com.example.nutripal.data.repository.OCRRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ResultScreenViewModel @Inject constructor(
    private val ocrRepository: OCRRepository,
    private val refreshAuthTokenService: RefreshAuthTokenService,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : ViewModel() {

    var accessToken: String? by mutableStateOf(null)
        private set

    private var userProfile: Map<String, String>? by mutableStateOf(null)

    private val _generatedRecommendation = MutableLiveData<String?>()
    val generatedRecommendation: LiveData<String?> = _generatedRecommendation

    private var pendingImageUrl: String? = null
    private var pendingNutritionFacts: NutritionFacts? = null

    init {
        fetchRefreshToken()
        auth.currentUser?.uid?.let { userId ->
            fetchUserProfile(userId) { profile ->
                profile?.let {
                    userProfile = mapOf(
                        "weight" to it.weight,
                        "height" to it.height,
                        "gender" to (it.gender ?: ""),
                        "age" to it.age,
                        "activityLevel" to (it.activityLevel ?: "")
                    )
                }
            }
        }
    }

    private fun fetchRefreshToken() {
        viewModelScope.launch {
            try {
                val response = refreshAuthTokenService.getRefreshToken()
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    if (tokenResponse != null) {
                        accessToken = tokenResponse.accessToken?.token
                        Log.d("ResultScreenViewModel", "Access token fetched: $accessToken")
                    } else {
                        Log.e("ResultScreenViewModel", "Token response is null")
                    }
                } else {
                    Log.e("ResultScreenViewModel", "Failed to fetch token: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ResultScreenViewModel", "Error fetching refresh token", e)
            }
        }
    }

    private fun fetchUserProfile(userId: String, callback: (Profile?) -> Unit) {
        firestore.collection("profiles").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profile = Profile(
                        name = document.getString("name") ?: "",
                        gender = document.getString("gender") ?: "",
                        activityLevel = document.getString("activityLevel") ?: "",
                        profilePicture = document.getString("profilePicture") ?: "",
                        age = document.getString("age") ?: "",
                        weight = document.getString("weight") ?: "",
                        height = document.getString("height") ?: ""
                    )
                    callback(profile)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching profile", exception)
                callback(null)
            }
    }

    var nutritionFacts: NutritionFacts? by mutableStateOf(null)
        private set

    private var isSaving = false

    private fun saveNutritionFactsToHistory(
        nutritionFacts: NutritionFacts,
        recommendation: String,
        imageUrl: String
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e("ResultScreenViewModel", "No user logged in")
            return
        }

        val uniqueId = UUID.randomUUID().toString()
        val currentDate = Calendar.getInstance().time

        val historyEntry = hashMapOf(
            "id" to uniqueId,
            "uid" to currentUser.uid,
            "carbohydrate" to nutritionFacts.carbohydrate,
            "protein" to nutritionFacts.protein,
            "fat" to nutritionFacts.fat,
            "recommendation" to recommendation,
            "image_url" to imageUrl,
            "timestamp" to Timestamp(currentDate)
        )

        firestore.collection("nutrition_history")
            .add(historyEntry)
            .addOnSuccessListener { documentReference ->
                Log.d("ResultScreenViewModel", "Nutrition facts saved with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("ResultScreenViewModel", "Error saving nutrition facts", e)
            }
    }

    private suspend fun uploadImageToStorage(bitmap: Bitmap): String {
        val storageRef = storage.reference.child("images/${UUID.randomUUID()}.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        return try {
            val uploadTask = storageRef.putBytes(data).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("ResultScreenViewModel", "Error uploading image", e)
            throw e
        }
    }

    fun extractNutritionFromBitmap(bitmap: Bitmap) {
        if (isSaving) {
            Log.w("ResultScreenViewModel", "Already saving data, skipping this call.")
            return
        }

        isSaving = true
        viewModelScope.launch {
            try {
                // Upload image to Firebase Storage
                val imageUrl = uploadImageToStorage(bitmap)
                pendingImageUrl = imageUrl

                // Extract nutritional values
                val extractedValues = ocrRepository.extractNutritionalValues(bitmap)
                val nutritionFacts = NutritionFacts(
                    carbohydrate = extractedValues["Karbohidrat"]?.toIntOrNull() ?: 0,
                    protein = extractedValues["Protein"]?.toIntOrNull() ?: 0,
                    fat = extractedValues["Lemak"]?.toIntOrNull() ?: 0
                )

                pendingNutritionFacts = nutritionFacts
                this@ResultScreenViewModel.nutritionFacts = nutritionFacts
                Log.d("ResultScreenViewModel", "Created NutritionFacts: $nutritionFacts")

                generateContentFromPrompt()
            } catch (e: Exception) {
                Log.e("ResultScreenViewModel", "Error processing bitmap", e)
                isSaving = false
            }
        }
    }

     private fun generateContentFromPrompt() {
        if (accessToken == null) {
            Log.d("ResultScreenViewModel", "Access token is not available, fetching now...")
            viewModelScope.launch {
                fetchRefreshToken()
                accessToken?.let { token ->
                    Log.d("ResultScreenViewModel", "Access token retrieved successfully")
                    makeGenerateContentRequest(token)
                } ?: Log.e("ResultScreenViewModel", "Failed to retrieve access token")
            }
        } else {
            Log.d("ResultScreenViewModel", "Using existing access token")
            makeGenerateContentRequest(accessToken!!)
        }
    }
    private fun makeGenerateContentRequest(authToken: String) {
        Log.d("ResultScreenViewModel", "Starting API call with authToken: $authToken")
        val userProfile = userProfile ?: return
        val weight = userProfile["weight"] ?: "unknown"
        val height = userProfile["height"] ?: "unknown"
        val gender = userProfile["gender"] ?: "unknown"
        val age = userProfile["age"] ?: "unknown"
        val activityLevel = userProfile["activityLevel"] ?: "unknown"

        val promptText = """
            Diet plan: weightloss, Weight: $weight kg, Height: $height cm, Sex: $gender, Age: $age, Activity level: $activityLevel. 
            Food: 1 serving contains ${nutritionFacts?.carbohydrate}g carbs, ${nutritionFacts?.protein}g protein, ${nutritionFacts?.fat}g fat.
        """.trimIndent()

        val request = StreamGenerateContentRequest(
            contents = listOf(
                Content(
                    role = "user",
                    parts = listOf(
                        Part(
                            text = promptText
                        )
                    )
                )
            ),
            systemInstruction = SystemInstruction(
                parts = listOf(
                    Part(
                        text = "give output 20 words consisting of the BMR, needed calories per day, calories for the food, maximum serving for the food based on the diet plan and give recommendation. Give all output in id"
                    )
                )
            ),
            generationConfig = GenerationConfig(
                temperature = 1.0f,
                maxOutputTokens = 8192,
                topP = 0.95f
            ),
            safetySettings = listOf(
                SafetySetting("HARM_CATEGORY_HATE_SPEECH", "OFF"),
                SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "OFF"),
                SafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "OFF"),
                SafetySetting("HARM_CATEGORY_HARASSMENT", "OFF")
            )
        )

        val vertexApiService = ApiConfig.postVertexService()
        val call = vertexApiService.generateContent(authHeader = "Bearer $authToken", request = request)
        call.enqueue(object : Callback<List<VertexResponse>> {
            override fun onResponse(
                call: Call<List<VertexResponse>>,
                response: Response<List<VertexResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { vertexResponse ->
                        val allTextParts = StringBuilder()

                        for ((index, vertexResponseItem) in vertexResponse.withIndex()) {
                            vertexResponseItem.candidates.forEachIndexed { candidateIndex, candidate ->
                                candidate.content.parts.forEachIndexed { partIndex, part ->
                                    val text = part.text
                                    if (text != null && text.isNotEmpty()) {
                                        allTextParts.append("$text\n")
                                    }
                                }
                            }
                        }

                        val recommendationText = allTextParts.toString()
                        _generatedRecommendation.value = recommendationText

                        // Save to history only when recommendation is successfully retrieved
                        pendingNutritionFacts?.let { nutritionFacts ->
                            pendingImageUrl?.let { imageUrl ->
                                saveNutritionFactsToHistory(
                                    nutritionFacts,
                                    recommendationText,
                                    imageUrl
                                )
                            }
                        }
                    }
                } else {
                    Log.e("ResultScreenViewModel", "Failed to generate content: ${response.errorBody()?.string()}")
                    _generatedRecommendation.value = "Gagal mendapatkan rekomendasi"
                }
                isSaving = false
            }

            override fun onFailure(call: Call<List<VertexResponse>>, t: Throwable) {
                Log.e("ResultScreenViewModel", "Error in API call", t)
                _generatedRecommendation.value = "Terjadi kesalahan saat memanggil API"
                isSaving = false
            }
        })
    }
}
