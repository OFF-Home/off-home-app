package com.offhome.app.singleChat



import android.content.Context
import com.offhome.app.data.model.Message
import org.mockito.Mock
import org.mockito.Mockito.*

@Suppress("UNCHECKED_CAST")
class SingleChatViewModelUnitTest {

    @Mock
    private lateinit var mockContext: Context

    var mockedList: ArrayList<Message> = mock(ArrayList::class.java) as ArrayList<Message>
/*
    @Test
    fun getMessages_Sets_List() {
        // `when`(mockContext.getString(R.string.hello_word)).thenReturn(FAKE_STRING)
        val viewModelTest = SingleChatViewModel(ChatRepository(ChatClient()))
        mockedList.add(Message("Message", "0"))
        val returns = MutableLiveData<Result<List<Message>>>(Result.Success(mockedList))
        `when`(viewModelTest.chatRepository.getMessages("0", "1")).thenReturn(returns)
        // assertThat("Data equals", viewModelTest.listMessages, )
    }*/
}
