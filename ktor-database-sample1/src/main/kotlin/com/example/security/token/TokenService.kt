package com.example.security.token

/**
 * this interface will abstract out how jwt token is generated and verified
 * Service is a way to call a class  that is stateless and just provide functionality for other classes
 */
interface TokenService {
    fun generate(
        config:TokenConfig,
        vararg claims: TokenClaim
    ):String
}

