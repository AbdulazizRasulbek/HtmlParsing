package uz.drop.htmlparsing.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.drop.htmlparsing.data.Data
import uz.drop.htmlparsing.data.Repository
import uz.drop.htmlparsing.data.RepositoryImpl
import uz.drop.htmlparsing.data.Response

class MainViewModel : ViewModel() {
    private val _htmlContentLiveData = MutableLiveData<Response<String>>()
    val htmlContentLiveData: LiveData<Response<String>> get() = _htmlContentLiveData
    private val _listLiveData = MutableLiveData<Response<List<Data>>>()
    val listLiveData: LiveData<Response<List<Data>>> get() = _listLiveData
    private val repository: Repository = RepositoryImpl()

    fun login(login: String, password: String) {
        Log.d("AAA login ","$login $password")
        CoroutineScope(Dispatchers.IO).launch {
            _htmlContentLiveData.postValue(Response.Loading())
            val response: Response<String> = repository.login(login, password)
            _htmlContentLiveData.postValue(response)
        }
    }

    fun parseHtml(html: String) {
        Log.d("AAA parse html",html)
        CoroutineScope(Dispatchers.IO).launch {
            _listLiveData.postValue(Response.Loading())
            val responseList: Response<List<Data>> = repository.parseHtml(html)
            _listLiveData.postValue(responseList)

        }
    }
}