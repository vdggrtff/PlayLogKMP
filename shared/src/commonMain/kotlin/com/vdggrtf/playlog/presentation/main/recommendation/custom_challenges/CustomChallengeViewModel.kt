package com.vdggrtf.playlog.presentation.main.recommendation.custom_challenges

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdggrtf.playlog.domain.model.CustomChallengeModel
import com.vdggrtf.playlog.domain.model.GameStatus
import com.vdggrtf.playlog.domain.usecase.main.challenge.GetCustomChallengesUseCase
import com.vdggrtf.playlog.domain.usecase.main.challenge.UpdateChallengeStatusUseCase
import com.vdggrtf.playlog.domain.usecase.main.challenge.VerifyChallengeProofUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChallengeBoardState(
    val isLoading: Boolean = false,
    val challenges: List<CustomChallengeModel> = emptyList(),
    val error: String? = null,
    val isVerifying: Boolean = false, // AI is thinking
    val successMessage: String? = null, // AI approved
)

class ChallengeBoardViewModel (
    savedStateHandle: SavedStateHandle,
    private val getCustomChallengesUseCase: GetCustomChallengesUseCase,
    private val updateChallengeStatusUseCase: UpdateChallengeStatusUseCase,
    private val verifyChallengeProofUseCase: VerifyChallengeProofUseCase,
) : ViewModel() {

    val currentChallengeId: Int? = savedStateHandle.get<Int>("challengeId")
        ?: savedStateHandle.get<String>("challengeId")?.toIntOrNull()

    private val _state = MutableStateFlow(ChallengeBoardState())
    val state = _state.asStateFlow()

    init {
        fetchChallenges()
    }

    private fun fetchChallenges() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getCustomChallengesUseCase().fold(
                onSuccess = { list ->
                    _state.update { it.copy(isLoading = false, challenges = list, error = null) }
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    // 3. THE CORE FUNCTION: AI Verification -> Supabase Insert
    fun verifyChallengeProof(challenge: CustomChallengeModel, imageBytes: ByteArray) {
        viewModelScope.launch {
            _state.update { it.copy(isVerifying = true, error = null, successMessage = null) }
            verifyChallengeProofUseCase(challenge, imageBytes).fold(
                onSuccess = { isApproved ->
                    if (isApproved) {
                        val updateList = _state.value.challenges.map {
                            if (it.id == challenge.id) {
                                it.copy(status = GameStatus.COMPLETED, isCompleted = true)
                            } else it
                        }
                        _state.update {
                            it.copy(
                                isVerifying = false,
                                challenges = updateList,
                                successMessage = "Contract Completed! You earned ${challenge.rewardPoints} E$"
                            )
                        }
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            error = "System error: ${error.message}",
                            successMessage = null
                        )
                    }
                }
            )
        }
    }

    fun updateChallengeStatus(challengeId: Int, newStatus: GameStatus) {
        viewModelScope.launch {
            updateChallengeStatusUseCase(challengeId, newStatus).fold(
                onSuccess = {
                    // Update local state list to instantly reflect changes in UI
                    val updatedList = _state.value.challenges.map {
                        if (it.id == challengeId) {
                            it.copy(status = newStatus, isCompleted = newStatus == GameStatus.COMPLETED)
                        } else it
                    }
                    _state.update { it.copy(challenges = updatedList) }
                },
                onFailure = {error ->
                    println("BOUNTY_BOARD Error updating status: ${error.message}")
                }
            )
        }
    }

    fun refreshChallenges() {
        fetchChallenges()
    }

    fun clearAlerts() {
        _state.update { it.copy(error = null, successMessage = null) }
    }
}