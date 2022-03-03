package app.prachang.gmail_clone.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.prachang.android_common.apputils.Loading
import app.prachang.android_common.apputils.UiStates
import app.prachang.dummy_data.gmail.MailsDataTable
import app.prachang.gmail_clone.repository.EmailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val emailRepository: EmailRepository
) : ViewModel() {

    private val _gmailList = MutableStateFlow<UiStates<List<MailsDataTable>>>(Loading())
    val gmailList = _gmailList.asStateFlow()
    fun getGmail() = viewModelScope.launch {
        _gmailList.value = emailRepository.getEmails()
    }
}