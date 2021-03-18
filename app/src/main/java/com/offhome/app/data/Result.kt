package com.offhome.app.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
// plantilla que venia feta. creada al fer la activity SignUp.
/*sealed class Result/*<out T : Any>*/ {

    data class Success/*<out T : Any>*/(/*val data: T*/val data:Boolean) : Result/*<T>*/()
    data class Error(val exception: Exception) : Result/*<Nothing>*/()
    // data class Error(val exception: Exception) : Result<Exception>() //jo

    override fun toString(): String {
        return when (this) {
            is Success/*<*>*/ -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}*/

data class Result(
    val success: Boolean? = null, // bool innecessari però esque anira checkejant des de la Activity si és null.
    val error: Exception? = null
)
