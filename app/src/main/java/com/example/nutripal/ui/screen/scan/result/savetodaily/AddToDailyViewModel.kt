package com.example.nutripal.ui.screen.scan.result.savetodaily

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutripal.data.model.DailyNutrition
import com.example.nutripal.data.model.NutritionFacts
import com.example.nutripal.data.remote.request.Content
import com.example.nutripal.data.remote.request.GenerationConfig
import com.example.nutripal.data.remote.request.Part
import com.example.nutripal.data.remote.request.SafetySetting
import com.example.nutripal.data.remote.request.StreamGenerateContentRequest
import com.example.nutripal.data.remote.request.SystemInstruction
import com.example.nutripal.data.remote.response.vertex.VertexResponse
import com.example.nutripal.data.remote.retrofit.ApiConfig
import com.example.nutripal.data.remote.retrofit.RefreshAuthTokenService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddToDailyViewModel @Inject constructor(
    private val refreshAuthTokenService: RefreshAuthTokenService,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _selectedProduct = MutableStateFlow("")
    val selectedProduct: StateFlow<String> = _selectedProduct

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _saveState = MutableStateFlow<Boolean?>(null)
    val saveState: StateFlow<Boolean?> = _saveState

    private val _protein = MutableStateFlow(0)
    val protein: StateFlow<Int> = _protein

    private val _fat = MutableStateFlow(0)
    val fat: StateFlow<Int> = _fat

    private val _isRefreshEnabled = MutableStateFlow(false)
    val isRefreshEnabled: StateFlow<Boolean> = _isRefreshEnabled

    private val _carbohydrate = MutableStateFlow(0)
    val carbohydrate: StateFlow<Int> = _carbohydrate

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _userProfile = MutableStateFlow<Map<String, String>?>(null)
    private val _accessToken = MutableStateFlow<String?>(null)

    private var initialNutritionFacts: NutritionFacts? = null

    private val _recommendation = MutableStateFlow("")
    val recommendation: StateFlow<String> = _recommendation

    init {
        fetchRefreshToken()
        fetchUserProfile()
    }

    private fun fetchRefreshToken() {
        viewModelScope.launch {
            try {
                val response = refreshAuthTokenService.getRefreshToken()
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    _accessToken.value = tokenResponse?.accessToken?.token
                }
            } catch (e: Exception) {
                Log.e("AddToDailyViewModel", "Error fetching refresh token", e)
            }
        }
    }


    fun refreshRecommendation(currentNutritionFacts: NutritionFacts) {
        _isLoading.value = true
        val accessToken = _accessToken.value ?: run {
            _isLoading.value = false
            return
        }
        val userProfile = _userProfile.value ?: run {
            _isLoading.value = false
            return
        }

        val promptText = """
            Diet plan: weightloss, Weight: ${userProfile["weight"]} kg, Height: ${userProfile["height"]} cm, 
            Sex: ${userProfile["gender"]}, Age: ${userProfile["age"]}, Activity level: ${userProfile["activityLevel"]}. 
            Food: 1 serving contains ${currentNutritionFacts.carbohydrate}g carbs, 
            ${currentNutritionFacts.protein}g protein, ${currentNutritionFacts.fat}g fat.
        """.trimIndent()

        val request = StreamGenerateContentRequest(
            contents = listOf(
                Content(
                    role = "user",
                    parts = listOf(Part(text = promptText))
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
        val call = vertexApiService.generateContent(authHeader = "Bearer $accessToken", request = request)
        call.enqueue(object : Callback<List<VertexResponse>> {
            override fun onResponse(
                call: Call<List<VertexResponse>>,
                response: Response<List<VertexResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { vertexResponse ->
                        val recommendationText = vertexResponse.flatMap {
                            it.candidates.flatMap { candidate ->
                                candidate.content.parts.mapNotNull { part -> part.text }
                            }
                        }.joinToString("\n")

                        _recommendation.value = recommendationText
                        _isRefreshEnabled.value = false
                    }
                } else {
                    _recommendation.value = "Gagal mendapatkan rekomendasi"
                }
            }

            override fun onFailure(call: Call<List<VertexResponse>>, t: Throwable) {
                _isLoading.value = false
                _recommendation.value = "Terjadi kesalahan saat memanggil API"
            }
        })
    }

    private fun fetchUserProfile() {
        auth.currentUser?.uid?.let { userId ->
            firestore.collection("profiles").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        _userProfile.value = mapOf(
                            "weight" to (document.getString("weight") ?: "unknown"),
                            "height" to (document.getString("height") ?: "unknown"),
                            "gender" to (document.getString("gender") ?: "unknown"),
                            "age" to (document.getString("age") ?: "unknown"),
                            "activityLevel" to (document.getString("activityLevel") ?: "unknown")
                        )
                    }
                }
        }
    }

    fun updateProduct(product: String) {
        _selectedProduct.value = product
    }

    fun updateProtein(value: Int) {
        _protein.value = value
    }

    fun updateFat(value: Int) {
        _fat.value = value
    }

    fun updateCarbohydrate(value: Int) {
        _carbohydrate.value = value
    }

    fun saveNutritionFacts(nutritionFacts: NutritionFacts) {
        _carbohydrate.value = nutritionFacts.carbohydrate
        _protein.value = nutritionFacts.protein
        _fat.value = nutritionFacts.fat
    }

    fun updateRefreshEnabled(
        initialCarbohydrate: Int,
        currentCarbohydrate: Int,
        initialProtein: Int,
        currentProtein: Int,
        initialFat: Int,
        currentFat: Int
    ) {
        initialNutritionFacts = NutritionFacts(initialCarbohydrate, initialProtein, initialFat)
        _isRefreshEnabled.value = currentCarbohydrate != initialCarbohydrate ||
                currentProtein != initialProtein ||
                currentFat != initialFat
    }

    fun saveDailyNutrition(imageUri: String, recommendation: String) {
        viewModelScope.launch {
            val user = auth.currentUser
            val uid = user?.uid ?: return@launch

            val timestamp = com.google.firebase.Timestamp.now()

            val dailyNutrition = DailyNutrition(
                id = UUID.randomUUID().toString(),
                uid = uid,
                product = _selectedProduct.value,
                image = imageUri,
                protein = _protein.value,
                fat = _fat.value,
                carbohydrate = _carbohydrate.value,
                recommendation = recommendation,
                timestamp = timestamp
            )

            firestore.collection("daily_nutritions")
                .add(dailyNutrition)
                .addOnSuccessListener {
                    _saveState.value = true
                }
                .addOnFailureListener { exception ->
                    _saveState.value = false
                }
        }
    }
}