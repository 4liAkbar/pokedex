package com.miniproject.pokedex.config.exception

import com.miniproject.pokedex.config.property.GlobalConstants


class AppException(vararg defaultMessage: String) :
	RuntimeException(defaultMessage.joinToString(GlobalConstants.MESSAGE_SEPARATOR))