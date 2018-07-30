package net.albertogarrido.rickandmorty.ui.common

import net.albertogarrido.rickandmorty.util.ErrorType

interface CharacterFinderView {

    fun showError(errorType: ErrorType, optionalMessage: String?)

    fun toggleLoading(show: Boolean)
}